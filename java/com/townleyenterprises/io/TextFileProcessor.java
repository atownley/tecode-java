//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2004, Andrew S. Townley
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
// File:	TextFileProcessor.java
// Created:	Mon Jan 26 13:44:28 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * This class provides a pluggable mechanism for processing line-based
 * text files with a given character encoding.  The class breaks up
 * the input file into lines of text.  The actual processing of the
 * line is provided by providing an instance of @{link LineProcessor}
 * apropriate for the task.
 * <p>
 * An example of echoing the file to the console:
 * </p>
 * <pre>
 * try
 * {
 * 	// create a text file processor using the default encoding
 * 	TextFileProcessor fp = new TextFileProcessor("myfile.dat");
 * 	fp.processFile(new AbstractLineProcessor() {
 * 		public void processLine(String line) throws Exception
 * 		{
 * 			super.processLine(line);
 * 			System.out.println(line);
 * 		}
 *	});
 * }
 * catch(FileNotFoundException e)
 * {
 * 	e.printStackTrace();
 * }
 * </pre>
 *
 * @version $Id: TextFileProcessor.java,v 1.1 2004/01/26 18:49:36 atownley Exp $
 * @author <a href="mailto:adz1092@nestscape.net">Andrew S. Townley</a>
 * @since 2.1
 */

public class TextFileProcessor
{
	/**
	 * The constructor initializes the input file using the default
	 * encoding.
	 *
	 * @param name the name of the file to process
	 */

	public TextFileProcessor(String name)
	{
		// the implementation doesn't reuse the overloaded
		// constructor because the default encoding will
		// always be available.
		
		_filename = name;
		_encoding = null;
	}

	/**
	 * The constructor initializes the input file using the
	 * specified character encoding.
	 *
	 * @param name the name of the file to process
	 * @param encoding the encoding of the input file
	 * @exception UnsupportedCharsetException
	 * 	if the encoding is not supported by the JVM
	 */

	public TextFileProcessor(String name, String encoding)
			throws UnsupportedCharsetException
	{
		_filename = name;
		_encoding = encoding;
		if(encoding != null)
		{
			Charset.forName(encoding);
		}
	}

	/**
	 * This method is used to process the file.  For each line,
	 * the @{link LineProcessor.processLine} method is called.
	 *
	 * @param lp the LineProcessor instance
	 * @exception IOException
	 * 	if there was an error reading the file
	 * @exception Exception
	 * 	if there was an error thrown by the LineProcessor
	 */

	public void processFile(LineProcessor lp)
			throws IOException, Exception
	{
		BufferedReader in = null;
		InputStreamReader isr = null;
		
		try
		{
			File file = new File(_filename);
			FileInputStream fis = new FileInputStream(file);
			if(_encoding != null)
				isr = new InputStreamReader(fis, _encoding);
			else
				isr = new InputStreamReader(fis);

			in = new BufferedReader(isr);

			lp.reset();
			String line = in.readLine();
			while(line != null)
			{
				lp.processLine(line);
				line = in.readLine();
			}
		}
		catch(IOException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if(in != null)
					in.close();
			}
			catch(IOException e)
			{
				// don't care
			}
		}
	}

	/** the name of the file to process */
	private final String	_filename;

	/** the character encoding to use */
	private final String	_encoding;
}
