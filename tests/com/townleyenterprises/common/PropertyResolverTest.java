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
// File:	PropertyResolverTest.java
// Created:	Fri Jan 23 16:26:14 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.Properties;
import junit.framework.TestCase;

/**
 * Basic unit tests for the PropertyResolver class
 *
 * @version $Id: PropertyResolverTest.java,v 1.1 2004/01/28 19:42:14 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class PropertyResolverTest extends TestCase
{
	public PropertyResolverTest(String testname)
	{
		super(testname);
	}

	public void setUp()
	{
		_props.setProperty("one", "the first property");
		_props.setProperty("prefix.one", "the prefixed first property");
		_props.setProperty("xxx.one", "the xxx first property");
	}

	public void testBasicGet()
	{
		assertEquals("the first property", _resolver.get("one"));
	}

	public void testPrefixGet()
	{
		assertEquals("the prefixed first property",
			_resolver.get("prefix", "one"));
	}

	public void testBogusProperty()
	{
		assertNull(_resolver.get("bogus"));
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(PropertyResolverTest.class);
	}

	Properties		_props = new Properties();
	PropertyResolver	_resolver = new PropertyResolver(_props);
}
