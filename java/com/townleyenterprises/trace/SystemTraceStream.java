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
// Name:	SystemTraceStream.java
// Created:	Fri Dec  5 00:21:32 GMT 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

import java.io.PrintStream;

/**
 * This class provides a TraceStream implementation which is based on
 * System.err and is the default trace stream used unless otherwise
 * configured.
 *
 * @version $Id: SystemTraceStream.java,v 1.1 2004/11/27 17:32:50 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public class SystemTraceStream implements TraceStream
{
	/**
	 * This class is actually a singleton, so we don't want any
	 * instances created by anyone other than us.
	 */

	private SystemTraceStream()
	{
	}

	/**
	 * This is the singleton's factory method for creating or
	 * obtaining a reference.
	 *
	 * @return a TraceStream
	 */

	public static TraceStream getInstance()
	{
		return _instance;
	}

	/**
	 * This method writes the message to the trace output, but
	 * doesn't include the newline.
	 *
	 * @param message the message to write
	 */

	public synchronized void print(Object message)
	{
		System.err.print(message);
	}

	/**
	 * This method writes a line to the trace output.
	 *
	 * @param message the message to write
	 */

	public synchronized void println(Object message)
	{
		System.err.println(message);
	}

	/**
	 * This method is used to write an exception's stack trace to
	 * the trace output.
	 *
	 * @param ex the exception to print
	 */

	public synchronized void printStackTrace(Throwable ex)
	{
		ex.printStackTrace(System.err);
	}

	/**
	 * This method returns the print stream associated with this
	 * output.
	 *
	 * @return a PrintStream instance
	 */

	public PrintStream getPrintStream()
	{
		return System.err;
	}

	/**
	 * This method closes the trace stream.
	 */

	public synchronized void close()
	{
	}

	/** our singleton instance */
	private static TraceStream _instance = new SystemTraceStream();
}
