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
// Name:	FileTraceStream.java
// Created:	Fri Dec  5 00:26:15 GMT 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This class provides an implementation of the TraceStream interface
 * in terms of a system file.
 *
 * @version $Id: FileTraceStream.java,v 1.1 2004/11/27 17:32:50 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public class FileTraceStream implements TraceStream
{
	/**
	 * The constructor takes the name of the file and the append
	 * mode for the output stream.
	 *
	 * @param filename the name of the file
	 * @param append true to append; false to overwrite
	 * @exception IOException
	 * 	if there was a problem creating the file
	 */

	public FileTraceStream(String filename, boolean append)
			throws IOException
	{
		_stream = new PrintStream(new FileOutputStream(filename, append));
	}

	/**
	 * This method writes the message to the trace output, but
	 * doesn't include the newline.
	 *
	 * @param message the message to write
	 */

	public synchronized void print(Object message)
	{
		_stream.print(message);
		_stream.flush();
	}

	/**
	 * This method writes a line to the trace output.
	 *
	 * @param message the message to write
	 */

	public synchronized void println(Object message)
	{
		_stream.println(message);
		_stream.flush();
	}

	/**
	 * This method is used to write an exception's stack trace to
	 * the trace output.
	 *
	 * @param ex the exception to print
	 */

	public synchronized void printStackTrace(Throwable ex)
	{
		ex.printStackTrace(_stream);
		_stream.flush();
	}

	/**
	 * This method returns the print stream associated with this
	 * output.
	 *
	 * @return a PrintStream instance
	 */

	public PrintStream getPrintStream()
	{
		return _stream;
	}

	/**
	 * This method closes the trace stream.
	 */

	public synchronized void close()
	{
		_stream.flush();
		_stream.close();
	}

	private PrintStream _stream = null;
}
