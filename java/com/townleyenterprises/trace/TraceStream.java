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
// Name:	TraceStream.java
// Created:	Thu Dec  4 22:36:25 GMT 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

import java.io.PrintStream;

/**
 * This interface is implemented by classes which can write trace
 * information.  It is a subset of the PrintStream interface.
 *
 * @version $Id: TraceStream.java,v 1.1 2004/11/27 17:32:50 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public interface TraceStream
{
	/**
	 * This method writes the message to the trace output, but
	 * doesn't include the newline.
	 *
	 * @param message the message to write
	 */

	void print(Object message);

	/**
	 * This method writes a line to the trace output.
	 *
	 * @param message the message to write
	 */

	void println(Object message);

	/**
	 * This method is used to write an exception's stack trace to
	 * the trace output.
	 *
	 * @param ex the exception to print
	 */

	void printStackTrace(Throwable ex);

	/**
	 * This method returns the print stream associated with this
	 * output.
	 *
	 * @return a PrintStream instance
	 */

	PrintStream getPrintStream();

	/**
	 * This method closes the trace stream.
	 */

	void close();
}
