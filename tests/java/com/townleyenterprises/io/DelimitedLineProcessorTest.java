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
// File:	DelimitedLineProcessorTest.java
// Created:	Mon Dec 27 11:20:50 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @version $Id: DelimitedLineProcessorTest.java,v 1.1 2004/12/27 12:28:22 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public final class DelimitedLineProcessorTest extends TestCase
{
	private static class Processor extends DelimitedLineProcessor
	{
		public Processor(String del)
		{
			super(del);
		}

		public void processItems(List list)
		{
			lines.add(list);
		}
	}

	protected void setUp() throws Exception
	{
		String testdata = System.getProperty("tests.data.dir");
		assertNotNull(testdata);

		File df = new File(testdata, "delimited-file.txt");
		TextFileProcessor fp = new TextFileProcessor(
				df.getAbsolutePath());

		fp.processFile(new Processor("|"));
	}

	public void testEmptyItemOne()
	{
		List line = (List)lines.get(0);

		assertEquals(7, line.size());
		assertEquals("", line.get(0));
		assertEquals("this", line.get(1));
		assertEquals("line", line.get(2));
		assertEquals("has", line.get(3));
		assertEquals("no", line.get(4));
		assertEquals("item", line.get(5));
		assertEquals("one", line.get(6));
	}

	public void testNormalParse()
	{
		List line = (List)lines.get(1);

		assertEquals(5, line.size());
		assertEquals("This", line.get(0));
		assertEquals("is", line.get(1));
		assertEquals("the", line.get(2));
		assertEquals("first", line.get(3));
		assertEquals("line", line.get(4));
	}

	public void testEmptyLastToken()
	{
		List line = (List)lines.get(3);

		assertEquals(7, line.size());
		assertEquals("this", line.get(0));
		assertEquals("line", line.get(1));
		assertEquals("has", line.get(2));
		assertEquals("no", line.get(3));
		assertEquals("last", line.get(4));
		assertEquals("token", line.get(5));
		assertEquals("", line.get(6));
	}

	public void testMissingToken()
	{
		List line = (List)lines.get(4);

		assertEquals(7, line.size());
		assertEquals("this", line.get(0));
		assertEquals("", line.get(1));
		assertEquals("line", line.get(2));
		assertEquals("is", line.get(3));
		assertEquals("missing", line.get(4));
		assertEquals("a", line.get(5));
		assertEquals("token", line.get(6));
	}

	public DelimitedLineProcessorTest(String testname)
	{
		super(testname);
	}

	static ArrayList lines = new ArrayList();
}
