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
// File:	PathTest.java
// Created:	Fri Jan 23 16:26:14 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import junit.framework.TestCase;

/**
 * Basic unit tests for the Path class
 *
 * @version $Id: PathTest.java,v 1.4 2004/11/28 20:08:14 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class PathTest extends TestCase
{
	public PathTest(String testname)
	{
		super(testname);
	}

	public void testBasenameFileName()
	{
		assertEquals("file.txt",
			Path.basename("/some/path/to/file.txt", "/"));
	}

	public void testBasenameFileNameStripSuffix()
	{
		assertEquals("file",
			Path.basename("/some/path/to/file.txt",
				"/", ".txt"));
	}

	public void testDirname()
	{
		assertEquals("/some/path/to",
			Path.dirname("/some/path/to/file.txt"));
	}

	public void testDirnameNoPath()
	{
		assertEquals(".", Path.dirname("file.txt"));
	}

	public void testBasenameNullPath()
	{
		assertNull(Path.basename(null, "foo"));
	}

	public void testBasenameNoPath()
	{
		assertEquals("one", Path.basename("one", "/"));
	}

	public void testClassname()
	{
		assertEquals("PathTest", Path.classname(getClass().getName()));
	}

	public void testStripExtension()
	{
		assertEquals("a", Path.stripExtension("a.bat"));
	}

	public void testStripExtensionNone()
	{
		assertEquals("a", Path.stripExtension("a"));
	}

	public void testGetExtension()
	{
		assertEquals("bar", Path.getExtension("foo.bar"));
	}

	public void testGetExtensionNone()
	{
		assertEquals("", Path.getExtension("foo"));
	}
	
	public void testNullPath()
	{
		assertNull(Path.pathname(null, "foo"));
	}
	
	public void testNullPathDelimiter()
	{
		assertNull(Path.pathname("foo", null));
	}
	
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(PathTest.class);
	}
}
