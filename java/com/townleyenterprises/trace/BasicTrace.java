//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003, Andrew S. Townley
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
// Name:	BasicTrace.java
// Created:	Fri Dec  5 00:58:43 GMT 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

/**
 * This class implements a proposal by Phil Hourihane (one of my
 * colleagues at Meridian) as a compromise to still have the tracing
 * capabilites, but reduce the number of lines of code required to
 * provide complete (or mostly complete) coverage of the exits from
 * methods expected to throw exceptions.  Personally, I still feel
 * that this approach is no substitute for the fully instrumented
 * version, but I can see why it might be desirable to implement.
 * <p>
 * This class should be used in the following manner to instrument a
 * method:
 * <pre>
 * 	...
 * 	private static BasicTrace _trace = new BasicTrace("MyClass", 0);
 *	...
 * 	
 * 	public int someMethod(String param1, int param2) throws Exception
 * 	{
 * 		final String[] pnames = new String[] { "param1", "param2" };
 * 		_trace.methodStart("someMethod", pnames,
 * 			new Object[] { param1, new Integer(param2) });
 *
 * 		int rc = 0;
 * 		try
 * 		{
 * 			// code that could throw an exception
 * 			...
 * 			return _trace.methodReturn(rc);
 * 		}
 * 		finally
 * 		{
 * 			_trace.methodExit();
 * 		}
 * 	}
 * </pre>
 * </p>
 * <p>
 * The idea here is that you still get the entry and exit in the
 * normal case, but you don't have to attempt to check for all
 * possible exceptions which could be thrown (and upset the method
 * name stack of the MethodTrace class).
 * </p>
 * 
 * @version $Id: BasicTrace.java,v 1.1 2004/11/27 17:32:50 atownley Exp $
 * @author Phil Hourihane (API specification)
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public class BasicTrace extends MethodTrace
{
	/**
	 * The constructor initializes the trace core instance.
	 */

	public BasicTrace(String name)
	{
		super(name, 0);
	}

	/**
	 * The constructor initializes the trace core instance.
	 */

	public BasicTrace(String name, int maturity)
	{
		super(name, maturity);
	}

	/**
	 * This constructor also sets the threshold when the method
	 * information will be printed (overriding the default value
	 * of 1).
	 *
	 * @param name the trace name
	 * @param maturity the maturity of the class
	 * @param threshold the default threshold
	 */

	public BasicTrace(String name, int maturity, int threshold)
	{
		super(name, maturity, threshold);
		_mt = threshold;
	}

	/**
	 * This method should be called to indicate a normal exit from
	 * the method (either as a result of an exception, or from no
	 * exception being thrown.
	 */

	public void methodExit()
	{
		Object[] arr = { popCurrentMethod() };
		tprintln(_mt, TRACE_EXIT_FMT, arr);
	}

	private int _mt = 1;

	/** This method indicates a normal trace return */
	public static final String TRACE_EXIT_FMT = "{0}() exiting.";
}
