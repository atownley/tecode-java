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
// File:	VersionTest.java
// Created:	Wed Feb  4 17:25:53 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import junit.framework.TestCase;

/**
 * Basic unit tests for the Version class.  Note, require() method
 * must be tested manually.
 *
 * @version $Id: VersionTest.java,v 1.2 2004/02/04 19:34:55 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class VersionTest extends TestCase
{
	public VersionTest(String testname)
	{
		super(testname);
	}

	public void testVersionCompareEqual()
	{
		assertEquals(0, Version.compare(2, 1, "0", "69", 
					2, 1, "0", "69"));
	}

	public void testVersionCompareEqualPreRelease()
	{
		assertEquals(0, Version.compare(2, 1, "0", "69", 
					2, 1, "0-pre5", "69"));
	}

	public void testVersionCompareLess()
	{
		assertTrue(Version.compare(2, 1, "0", "68", 
					2, 1, "0-pre5", "69") < 0);
		assertTrue(Version.compare(2, 0, "100", "70", 
					2, 1, "0-pre5", "69") < 0);
		assertTrue(Version.compare(0, 0, "100", "70", 
					2, 1, "0-pre5", "69") < 0);
		assertTrue(Version.compare(9, 0, "0", "2070", 
					0, 0, "0", "DEVELOPER") < 0);
	}

	public void testVersionCompareGreater()
	{
		assertTrue(Version.compare(2, 1, "0", "70", 
					2, 1, "0-pre5", "69") > 0);
		assertTrue(Version.compare(2, 1, "1", "69", 
					2, 1, "0-pre5", "69") > 0);
		assertTrue(Version.compare(3, 0, "100", "24", 
					2, 1, "0-pre5", "69") > 0);
		assertTrue(Version.compare(0, 0, "0", "DEVELOPER", 
					2, 1, "0-pre5", "69") > 0);
	}

	public void testVersionParse()
	{
		String[] exp = new String[] { "2", "1", "0", "70" };
		String[] act = Version.parse("2.1.0 (Build 70)");
		for(int i = 0; i < act.length; ++i)
			assertEquals(exp[i], act[i]);

		act = Version.parse("2.1.0 (Build 70; 2004-02-04 17:40:26 GMT)");
		for(int i = 0; i < act.length; ++i)
			assertEquals(exp[i], act[i]);
	}

	public void testVersionParsePreRelease()
	{
		String[] exp = new String[] { "2", "1", "0-pre4", "70" };
		String[] act = Version.parse("2.1.0-pre4 (Build 70)");
		for(int i = 0; i < act.length; ++i)
			assertEquals(exp[i], act[i]);

		act = Version.parse("2.1.0-pre4 (Build 70; 2004-02-04 17:40:26 GMT)");
		for(int i = 0; i < act.length; ++i)
			assertEquals(exp[i], act[i]);
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(VersionTest.class);
	}
}
