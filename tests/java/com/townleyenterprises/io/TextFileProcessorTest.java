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
// File:	TextFileProcessorTest.java
// Created:	Mon Dec 27 11:50:42 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.io;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @version $Id: TextFileProcessorTest.java,v 1.1 2004/12/27 12:28:22 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public final class TextFileProcessorTest extends TestCase
{
	private static class Processor extends AbstractLineProcessor
	{
		public void processLine(String line) throws Exception
		{
			// necessary to count the lines processed
			super.processLine(line);

			lines.add(line);
		}

		ArrayList lines = new ArrayList();
	}

	public void testSimpleParseUTF8WithDefaultEncoding() throws Exception
	{
		String testdata = System.getProperty("tests.data.dir");
		assertNotNull(testdata);

		File df = new File(testdata, "text-file-utf8.txt");
		TextFileProcessor fp = new TextFileProcessor(
				df.getAbsolutePath());

		Processor lp = new Processor();
		fp.processFile(lp);

		// check the data
		assertEquals(4, lp.lines.size());
		assertEquals("\u20AC4,000.00", lp.lines.get(0));
		assertEquals("Nestl\u00E9", lp.lines.get(1));
		assertEquals("ESPA\u00D1A", lp.lines.get(2));
		assertEquals("Pla\u00E7a", lp.lines.get(3));
	}

	public void testSimpleParseUTF8() throws Exception
	{
		String testdata = System.getProperty("tests.data.dir");
		assertNotNull(testdata);

		File df = new File(testdata, "text-file-utf8.txt");
		TextFileProcessor fp = new TextFileProcessor(
				df.getAbsolutePath(), "utf-8");

		Processor lp = new Processor();
		fp.processFile(lp);
		
		// check the data
		assertEquals(4, lp.lines.size());
		assertEquals("\u20AC4,000.00", lp.lines.get(0));
		assertEquals("Nestl\u00E9", lp.lines.get(1));
		assertEquals("ESPA\u00D1A", lp.lines.get(2));
		assertEquals("Pla\u00E7a", lp.lines.get(3));
	}

	public void testSimpleParse885915() throws Exception
	{
		String testdata = System.getProperty("tests.data.dir");
		assertNotNull(testdata);

		File df = new File(testdata, "text-file-8859-15.txt");
		TextFileProcessor fp = new TextFileProcessor(
				df.getAbsolutePath(), "iso8859-15");

		Processor lp = new Processor();
		fp.processFile(lp);
		
		// check the data
		assertEquals(4, lp.lines.size());
		assertEquals("\u20AC4,000.00", lp.lines.get(0));
		assertEquals("Nestl\u00E9", lp.lines.get(1));
		assertEquals("ESPA\u00D1A", lp.lines.get(2));
		assertEquals("Pla\u00E7a", lp.lines.get(3));
	}

	public void testSimpleParseCP1252() throws Exception
	{
		String testdata = System.getProperty("tests.data.dir");
		assertNotNull(testdata);

		File df = new File(testdata, "text-file-cp1252.txt");
		TextFileProcessor fp = new TextFileProcessor(
				df.getAbsolutePath(), "cp1252");

		Processor lp = new Processor();
		fp.processFile(lp);
		
		// check the data
		assertEquals(4, lp.lines.size());
		assertEquals("\u20AC4,000.00", lp.lines.get(0));
		assertEquals("Nestl\u00E9", lp.lines.get(1));
		assertEquals("ESPA\u00D1A", lp.lines.get(2));
		assertEquals("Pla\u00E7a", lp.lines.get(3));
	}

	public void testLineCount() throws Exception
	{
		String testdata = System.getProperty("tests.data.dir");
		assertNotNull(testdata);

		File df = new File(testdata, "text-file-cp1252.txt");
		TextFileProcessor fp = new TextFileProcessor(
				df.getAbsolutePath(), "cp1252");

		Processor lp = new Processor();
		fp.processFile(lp);
		
		// check the data
		assertEquals(4, lp.lines.size());
		assertEquals(4, lp.getLineCount());
	}

	public TextFileProcessorTest(String testname)
	{
		super(testname);
	}
}
