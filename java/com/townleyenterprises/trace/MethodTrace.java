//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2001-2003, Andrew S. Townley
// All rights reserved.
// 
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 
//     * Redistributions of source code must retain the above
//     copyright notice, this list of conditions and the following
//     disclaimer.
// 
//     * Redistributions in binary form must reproduce the above
//     copyright notice, this list of conditions and the following
//     disclaimer in the documentation and/or other materials provided
//     with the distribution.
// 
//     * Neither the names Andrew Townley and Townley Enterprises,
//     Inc. nor the names of its contributors may be used to endorse
//     or promote products derived from this software without specific
//     prior written permission.  
// 
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.
//
// Name:	MethodTrace.java
// Created:	Fri Dec  5 00:40:25 GMT 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * This class augments the trace core with a way to track the name of
 * the currently traced method.  This class is now thread
 * safe.
 */

public class MethodTrace extends TraceCore
{
	/**
	 * The constructor initializes the trace core instance as well
	 * as specifies the default value at which the method
	 * information should be printed.
	 *
	 * @param methodTraceLevel set the level when the method
	 * information is printed (default is 1)
	 */

	protected MethodTrace(String name, int maturity,
				int methodTraceLevel)
	{
		super(name, maturity);
		_mt = methodTraceLevel;
	}

	/**
	 * The constructor initializes the trace core instance as well
	 * as setting the default value for method information to 1.
	 */

	protected MethodTrace(String name, int maturity)
	{
		this(name, maturity, 1);
	}

	/**
	 * This method should be called at the beginning of each
	 * method to be traced with the method name.
	 *
	 * @param name the name of the method, without the '()'
	 */

	public void methodStart(String name)
	{
		Object[] arr = { name };
		tprintln(_mt, TRACE_START_FMT, arr);
		_method.push(name);
	}

	/**
	 * This version of methodStart prints all of the arguments
	 * together with the parameter names.
	 *
	 * @param name the method name
	 * @param pnames the parameter names
	 * @param params the parameters
	 */

	public void methodStart(String name, String[] pnames,
			Object[] params)
	{
		if(pnames.length != params.length)
		{
			throw new RuntimeException("Argument length doesn't match parameter name length.");
		}

		_method.push(name);
		
		StringBuffer buf = new StringBuffer(name);
		buf.append("(");
		for(int i = 0; i < pnames.length; ++i)
		{
			buf.append(pnames[i]);
			buf.append(" = ");
			if(params[i] instanceof String)
			{
				buf.append("'");
				buf.append(params[i]);
				buf.append("'");
			}
			else
			{
				buf.append(params[i]);
			}
			if(i < pnames.length - 1)
				buf.append(", ");
		}
		buf.append(")");

		tprintln(_mt, buf.toString());
	}

	/**
	 * This version of methodStart prints all of the arguments
	 * without the parameter names.
	 *
	 * @param name the method name
	 * @param params the parameters
	 */

	public void methodStart(String name, Object[] params)
	{
		_method.push(name);
		
		StringBuffer buf = new StringBuffer(name);
		buf.append("(");
		for(int i = 0; i < params.length; ++i)
		{
			if(params[i] instanceof String)
			{
				buf.append("'");
				buf.append(params[i]);
				buf.append("'");
			}
			else
			{
				buf.append(params[i]);
			}
			if(i < params.length - 1)
				buf.append(", ");
		}
		buf.append(")");

		tprintln(_mt, buf.toString());
	}

	/**
	 * This method should be called to specify a method argument
	 * parameter.
	 *
	 * @param name the parameter name
	 * @param arg the parameter value
	 */

	public void methodArg(String name, String arg)
	{
		Object[] arr = { name, arg };
		tprintln(_mt, TRACE_ARG_STR_FMT, arr);
	}
	
	public void methodArg(String name, int arg)
	{
		Object[] arr = { name, new Integer(arg) };
		tprintln(_mt, TRACE_ARG_GEN_FMT, arr);
	}

	public void methodArg(String name, boolean arg)
	{
		Object[] arr = { name, new Boolean(arg) };
		tprintln(_mt, TRACE_ARG_GEN_FMT, arr);
	}

	public void methodArg(String name, double arg)
	{
		Object[] arr = { name, new Double(arg) };
		tprintln(_mt, TRACE_ARG_GEN_FMT, arr);
	}

	public void methodArg(String name, Traceable arg)
	{
		Object foo = arg;
		if(foo == null)
			foo = "(null)";
		else
			foo = arg.traceString();

		Object[] arr = { name, foo };
		tprintln(_mt, TRACE_ARG_GEN_FMT, arr);
	}

	public void methodArg(String name, Object arg)
	{
		Object foo = arg;
		if(foo == null)
			foo = "(null)";
		else
			foo = arg.toString();

		Object[] arr = { name, foo };
		tprintln(_mt, TRACE_ARG_GEN_FMT, arr);
	}

	public void methodArgs(String[] pnames, Object[] params)
	{
		if(pnames.length != params.length)
		{
			throw new RuntimeException("Argument length doesn't match parameter name length.");
		}

		for(int i =  0; i < pnames.length; ++i)
		{
			methodArg(pnames[i], params[i]);
		}
	}

	/**
	 * This is a utility method to return the current method
	 * scope.  If the scope is invalid, the "invalid scope"
	 * message is returned instead of the method name.
	 *
	 * @return the current method
	 */

	private String getCurrentMethod()
	{
		String m;
		try
		{
			m = (String)_method.peek();
		}
		catch(EmptyStackException e)
		{
			throw new RuntimeException("MethodTrace eror:  no method scope (empty stack).  Probable programming error (missing throw for traced exception)");
		}

		return m;
	}

	/**
	 * This method should be called to specify that the method is
	 * about to return, with no value.
	 */

	public void methodReturn()
	{
		Object[] arr = { getCurrentMethod() };
		tprintln(_mt, TRACE_RETURN_FMT, arr);
	}

	/**
	 * This method should be called to specify that the method is
	 * about to return, with the indicated value.
	 *
	 * @param return value
	 */

	public String methodReturn(String arg)
	{
		Object[] arr = { getCurrentMethod(), arg == null ? "null" : arg };
		if(arg != null)
			tprintln(_mt, TRACE_RETURN_STR_FMT, arr);
		else
			tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);

		return arg;
	}

	public boolean methodReturn(boolean arg)
	{
		Object[] arr = { getCurrentMethod(), new Boolean(arg) };
		tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);

		return arg;
	}

	public float methodReturn(float arg)
	{
		Object[] arr = { getCurrentMethod(), new Float(arg) };
		tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);
		return arg;
	}

	public double methodReturn(double arg)
	{
		Object[] arr = { getCurrentMethod(), new Double(arg) };
		tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);
		return arg;
	}

	public int methodReturn(int arg)
	{
		Object[] arr = { getCurrentMethod(), new Integer(arg) };
		tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);
		return arg;
	}

	public long methodReturn(long arg)
	{
		Object[] arr = { getCurrentMethod(), new Long(arg) };
		tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);
		return arg;
	}

	public Traceable methodReturn(Traceable arg)
	{
		Object[] arr = { getCurrentMethod(), arg == null ? "null" : arg.traceString() };
		tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);
		return arg;
	}

	public Object methodReturn(Object arg)
	{
		Object[] arr = { getCurrentMethod(), arg == null ? "null" : arg.toString() };
		tprintln(_mt, TRACE_RETURN_GEN_FMT, arr);
		return arg;
	}

	/**
	 * This method is used to specify that the method is about to
	 * throw an exception.
	 *
	 * @param ex the exception that is to be thrown
	 */

	public void methodThrow(Throwable ex)
	{
		try
		{
			methodThrow(ex, false);
		}
		catch(Throwable t)
		{
			// ignored for backward compatibility
		}
	}

	/**
	 * This method is used to specify that the method is about to
	 * throw an exception.
	 *
	 * @since 2.0
	 * @param ex the exception that is to be thrown
	 * @param print if true, print the stack trace of the
	 * 	exception automatically.
	 * @return the throwable instance is returned from this method
	 * 	in order to reduce programming errors.
	 */

	public Throwable methodThrow(Throwable ex, boolean print)
	{
		Object[] arr = { getCurrentMethod(), ex.toString() };
		tprintln(_mt, TRACE_THROW_FMT, arr);

		if(print)
		{
			printStackTrace(_mt, ex);
		}
		
		return ex;
	}

	/**
	 * This method is used to pop the method name off the stack
	 */

	protected String popCurrentMethod()
	{
		String m;
		try
		{
			m = (String)_method.pop();
		}
		catch(EmptyStackException e)
		{
			throw new RuntimeException("MethodTrace eror:  no method scope (empty stack).  Probable programming error (missing throw for traced exception)");
		}

		return m;
	}

	/**
	 * This class is used to keep a separate stack for each thread
	 * which accesses the instance.
	 */

	private static class ThreadStack extends ThreadLocal
	{
		public ThreadStack()
		{
			super.set(new Stack());
		}

		public boolean empty()
		{
			Stack s = getStack();
			synchronized(s)
			{
				return s.empty();
			}
		}

		public Object peek()
		{
			Stack s = getStack();
			synchronized(s)
			{
				return s.peek();
			}
		}

		public Object pop()
		{
			Stack s = getStack();
			synchronized(s)
			{
				return s.pop();
			}
		}

		public Object push(Object item)
		{
			Stack s = getStack();
			synchronized(s)
			{
				return s.push(item);
			}
		}

		public int search(Object o)
		{
			Stack s = getStack();
			synchronized(s)
			{
				return s.search(o);
			}
		}

		private Stack getStack()
		{
			Stack s = (Stack)super.get();
			if(s == null)
				s = _defaultStack;

			return s;
		}
	}

	/** This is the message format used for method start */
	public static final String TRACE_START_FMT = "{0}()";

	/** This is the message format used for methods that return no values */
	public static final String TRACE_RETURN_FMT = "{0}() returning.";

	/**
	 * This is the message format used for methods returning object values
	 */

	public static final String TRACE_RETURN_GEN_FMT = "{0}() returning:  {1}";

	/**
	 * This is the message format used for methods returning
	 * string values
	 */

	public static final String TRACE_RETURN_STR_FMT = "{0}() returning:  \"{1}\"";

	/**
	 * This is the message format for method arguments that are
	 * strings
	 */

	public static final String TRACE_ARG_STR_FMT = "\t{0} = \"{1}\"";

	/**
	 * This is the message format for method other method
	 * arguments
	 */

	public static final String TRACE_ARG_GEN_FMT = "\t{0} = {1}";

	/** This is the message format for throwing exceptions */
	public static final String TRACE_THROW_FMT = "{0}() throwing exception:  {1}";
	
	/** the method call stack for the class being traced*/
	private ThreadStack				_method = new ThreadStack();

	/** the default stack if we're calling cross-thread methods */
	private static Stack			_defaultStack = new Stack();

	/** the value at which the method information should be
	 * printed (default == 1) */
	private int				_mt;
}
