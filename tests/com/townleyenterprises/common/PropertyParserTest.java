//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2002-2004, Andrew S. Townley
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
// File:	PropertyParserTest.java
// Created:	Thu Jan 22 22:55:31 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import junit.framework.TestCase;

/**
 * Basic unit tests for the PropertyParser class
 *
 * @version $Id: PropertyParserTest.java,v 1.1 2004/01/25 19:39:18 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class PropertyParserTest extends TestCase
{
	public PropertyParserTest(String testname)
	{
		super(testname);
	}

	public void testParseBooleanNull()
	{
		PropertyParser pparser = new PropertyParser();
		assertEquals(false, pparser.booleanValue("bogus"));
	}

	public void testParseBooleanEmpty()
	{
		System.setProperty("bogus", "");
		PropertyParser pparser = new PropertyParser();
		assertEquals(false, pparser.booleanValue("bogus"));
	}

	public void testParseBooleanYes()
	{
		PropertyParser pparser = new PropertyParser();
		
		System.setProperty("bogus", "Y");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "Yes");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "Yellow Rose of Texas");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "y");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "yes");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "yesterday, all my troubles seemed so far away....");
		assertEquals(true, pparser.booleanValue("bogus"));
	}

	public void testParseBooleanTrue()
	{
		PropertyParser pparser = new PropertyParser();
		
		System.setProperty("bogus", "T");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "True");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "Texas");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "t");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "true");
		assertEquals(true, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "truth is relative to the listener");
		assertEquals(true, pparser.booleanValue("bogus"));
	}

	public void testParseBooleanOther()
	{
		PropertyParser pparser = new PropertyParser();
		
		System.setProperty("bogus", "X");
		assertEquals(false, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "XXAA");
		assertEquals(false, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "what?");
		assertEquals(false, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "12345");
		assertEquals(false, pparser.booleanValue("bogus"));
		System.setProperty("bogus", "zzz");
		assertEquals(false, pparser.booleanValue("bogus"));
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(PropertyParserTest.class);
	}
}
