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
// File:	PropertyProxyTest.java
// Created:	Thu Jan 22 22:56:02 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import junit.framework.TestCase;

/**
 * Basic unit tests for the PropertyProxy class
 *
 * @version $Id: PropertyProxyTest.java,v 1.1 2004/01/30 11:31:55 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class PropertyProxyTest extends TestCase
{
	private static class Proxy
	{
		public Integer getInteger()
		{
			return new Integer(7);
		}

		public boolean getBooleanValue()
		{
			return false;
		}
	}

	private static final Proxy instance = new Proxy();

	public PropertyProxyTest(String testname)
	{
		super(testname);
	}

	public void testGetObjectProperty()
	{
		PropertyProxy proxy = new PropertyProxy(Proxy.class);
		Object obj = proxy.getPropertyValue("integer", instance);
		assertEquals(new Integer(7), obj);
	}

	public void testGetPrimitiveProperty()
	{
		PropertyProxy proxy = new PropertyProxy(Proxy.class);
		Object obj = proxy.getPropertyValue("booleanValue", instance);
		assertEquals(Boolean.FALSE, obj);
	}

	public void testGetBogusProperty()
	{
		PropertyProxy proxy = new PropertyProxy(Proxy.class);
		try
		{
			proxy.getPropertyValue("fred", instance);
		}
		catch(RuntimeException e)
		{
			// expected
		}
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(PropertyProxyTest.class);
	}
}
