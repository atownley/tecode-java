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
// File:	AppConfigTest.java
// Created:	Sun Dec 26 15:22:29 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit tests for the new AppConfig class which are based on
 * the NUnit tests.
 *
 * @version $Id: AppConfigTest.java,v 1.2 2004/12/27 23:23:10 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public final class AppConfigTest extends TestCase
{
	public AppConfigTest(String testname)
	{
		super(testname);
	}

	protected void setUp() throws Exception
	{
		config = new AppConfig("actest");
		map1 = new HashMap();
		map1.put("key1", "val1");
		map1.put("key2", "val2");
		map1.put("key3", "val3");

		ConfigSupplier cs1 = 
			new MapConfigSupplier("actest", map1);
		config.registerConfigSupplier(cs1);

		map2 = new HashMap();
		map2.put("key2", "override");

		ConfigSupplier cs2 = 
			new MapConfigSupplier("actest", map2);
		config.registerConfigSupplier(cs2);

		map3 = new HashMap();
		map3.put("eggs", "green");
		map3.put("ham", "true");
		map3.put("key3", "true");
		map3.put("os.name", "NEXTSTEP");

		ConfigSupplier cs3 =
			new MapConfigSupplier("actest", map3);
		config.registerConfigSupplier(cs3);
	}

	public void testSimpleProperty()
	{
		assertEquals("val1", config.get("key1"));
	}

	public void testOverrideProperty()
	{
		assertEquals("val1", config.get("key1"));
		assertEquals("override", config.get("key2"));
		assertEquals("true", config.get("key3"));
	}

	public void testOverrideProperty2ndLevel()
	{
		assertEquals("true", config.get("key3"));
	}

	public void testIgnoreSettings()
	{
		HashMap map = new HashMap();
		map.put("ht", "bogus");

		config.registerConfigSupplier(
			new MapConfigSupplier("bogus", map));

		assertNull(config.get("ht"));
	}

	public void testEmptySettings()
	{
		HashMap map = new HashMap();
		map.put("key1", null);
		map.put("key2", null);
		map.put("key3", null);

		config.registerConfigSupplier(
			new MapConfigSupplier("actest", map));

		assertNull(config.get("key1"));
		assertNull(config.get("key2"));
		assertNull(config.get("key3"));
	}

	public void testBasicPropertySave()
	{
		config.put("key1", "new value");
		assertEquals("new value", map1.get("key1"));
	}

	public void testOverridePropertySave()
	{
		config.put("key2", "new value");
		assertEquals("new value", map2.get("key2"));
	}

	public void testDefaultSaveNewKey()
	{
		config.put("my new key", "some value");
		assertEquals("some value", map3.get("my new key"));
		assertEquals("some value", config.get("my new key"));
	}

	public void testWriteCaptureResolver()
	{
		AppConfig ac = new AppConfig("actest");
		HashMap map = new HashMap();
		ac.setWriteResolutionStrategy(new WriteCaptureStrategy(
				new MapConfigSupplier("actest", map)));

		ac.registerConfigSupplier(config);
		ac.put("key1", "write capture1");
		ac.put("key2", "write capture2");

		assertEquals("write capture1", ac.get("key1"));
		assertEquals("write capture2", ac.get("key2"));
		assertEquals("write capture1", map.get("key1"));
		assertEquals("write capture2", map.get("key2"));
		assertEquals("val1", config.get("key1"));
		assertEquals("override", config.get("key2"));
		assertEquals("val1", map1.get("key1"));
		assertEquals("override", map2.get("key2"));
	}

	public void testSystemMaster()
	{
		if("NEXTSTEP".equals(config.get("os.name")))
		{
			fail("system override failed");
		}
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(AppConfigTest.class);
	}

	private Map		map1;
	private Map		map2;
	private Map		map3;
	private AppConfig	config;
}
