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
// File:	PropertiesConfigSupplierTest.java
// Created:	Sun Dec 26 20:44:02 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @version $Id: PropertiesConfigSupplierTest.java,v 1.2 2004/12/28 10:37:29 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public final class PropertiesConfigSupplierTest extends TestCase
{
	public PropertiesConfigSupplierTest(String testname)
	{
		super(testname);
	}

	protected void setUp() throws Exception
	{
		config = new PropertiesConfigSupplier("testapp",
				PropertiesConfigSupplierTest.class);
	}

	public void testSimpleProperty()
	{
		assertEquals("val1", config.get("key1"));
	}

	public void testRegistration()
	{
		AppConfig ac = new AppConfig("testapp");
		ac.registerConfigSupplier(config);

		assertEquals("val1", ac.get("key1"));
	}

	public void testFilePathRead() throws Exception
	{
		File tf = new File(System.getProperty("tests.data.dir"),
				"test.properties");
		PropertiesConfigSupplier pcs = new PropertiesConfigSupplier("testapp", tf.getAbsolutePath());

		assertEquals("value", pcs.get("simple.property"));
		assertEquals("value", pcs.get("simple.property.lvs").trim());
		assertEquals("value", pcs.get("simple.property.tvs").trim());
		assertEquals("value", pcs.get("simple.property.lks").trim());
		assertEquals("value", pcs.get("simple.property.tks").trim());
	}

	PropertiesConfigSupplier	config;
}
