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
// Name:	ErrorTrace.java
// Author:	Andrew S. Townley <adz1092 at netscape dot net>
// Created:	Mon Jun 11 18:41:17 CDT 2001
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.EmptyStackException;
import java.util.Properties;
import java.util.Stack;

/**
 * This class implements generic trace facilities.  The general usage
 * should be for classes who will support tracing to implement the
 * Traceable interface and then create a static attribute which will
 * be used for trace messages of that class.
 * <p>
 * The maturity level of the class to be traced may be specified,
 * allowing author-defined filtering of trace messages for classes
 * that have been more exhaustively tested.  The maturity level
 * specifies the trace threshold at which messages of this class
 * should be displayed.
 * </p>
 * <p>
 * Since version 3.0, instances of this class are now independently
 * controlable for the trace level.  This means that certain classes
 * can be always disabled, even if they are fully instrumented.
 * </p>
 * <p>
 * This class is now thread safe.
 * </p>
 *
 * @since 1.0
 * @version $Id: ErrorTrace.java,v 1.1 2004/11/27 17:32:50 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public class ErrorTrace extends MethodTrace
{
	/**
	 * This is the empty constructor for an ErrorTrace instance.
	 * It assigns some defaults for all the attributes.  The
	 * classname is undefined, and the maturity level will be 0.
	 */

	public ErrorTrace()
	{
		super("unknown class", 0);
	}

	/**
	 * Helper method for the default maturity level
	 *
	 * @param className the name to appear in the logs
	 */

	public ErrorTrace(String className)
	{
		super(className, 0);
	}

	/**
	 * This is the normal constructor for the ErrorTrace class
	 * that allows specification of the class name to be traced
	 * and also allows setting the maturity level.
	 *
	 * @param className the name to appear in the trace logs
	 * @param maturity the maturity level of the class
	 */

	public ErrorTrace(String className, int maturity)
	{
		super(className, maturity);
	}

	public void methodReturn()
	{
		super.methodReturn();
		popCurrentMethod();
	}

	public String methodReturn(String arg)
	{
		super.methodReturn(arg);
		popCurrentMethod();
		return arg;
	}

	public boolean methodReturn(boolean arg)
	{
		super.methodReturn(arg);
		popCurrentMethod();
		return arg;
	}

	public double methodReturn(double arg)
	{
		super.methodReturn(arg);
		popCurrentMethod();
		return arg;
	}

	public long methodReturn(long arg)
	{
		super.methodReturn(arg);
		popCurrentMethod();
		return arg;
	}

	public Traceable methodReturn(Traceable arg)
	{
		super.methodReturn(arg);
		popCurrentMethod();
		return arg;
	}

	public Object methodReturn(Object arg)
	{
		super.methodReturn(arg);
		popCurrentMethod();
		return arg;
	}

	public Throwable methodThrow(Throwable ex, boolean print)
	{
		super.methodThrow(ex, print);
		popCurrentMethod();
		return ex;
	}
}
