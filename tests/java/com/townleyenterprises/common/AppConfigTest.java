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
// Created:	Thu Jan 22 19:03:09 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

/**
 * Basic unit tests for the AppConfig class.  It uses the following
 * properties files which must be located in the classpath:
 * <h3>
 * Base Properties
 * </h3>
 * <p>
 * <pre>
 * property1=basevalue1
 * property2=basevalue2
 * compound.property=compound1
 * another.compound.property=compound2
 * </pre>
 * </p>
 * 
 * <h3>
 * Override Properties
 * </h3>
 * <p>
 * <pre>
 * property1=override1
 * property3=override3
 * </pre>
 * </p>
 *
 * @version $Id: AppConfigTest.java,v 1.3 2004/08/11 13:22:31 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class AppConfigTest extends TestCase
		implements ConfigSupplier
{
	public AppConfigTest(String testname)
	{
		super(testname);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		AppConfig.registerAppSupplier(this);
	}

	public String getAppName()
	{
		return "appconfigtest";
	}

	public void testOverrideProperty()
	{
		assertEquals("override1", AppConfig.get("property1"));
	}

	public void testGetProperty()
	{
		assertEquals("basevalue2", AppConfig.get("property2"));
		assertEquals("override3", AppConfig.get("property3"));
	}

	public void testSystemOverrideDefault()
	{
		System.setProperty("property1", "spam");
		assertEquals("spam", AppConfig.get("property1"));
	}

	public void testSystemOverrideYes()
	{
		System.setProperty("te-code.common.appconfig.overridesystemproperties", "yes");
		System.setProperty("property1", "spam");
		assertEquals("override1", AppConfig.get("property1"));
	}

	public void testSystemOverrideTrue()
	{
		System.setProperty("te-code.common.appconfig.overridesystemproperties", "true");
		System.setProperty("property1", "spam");
		assertEquals("override1", AppConfig.get("property1"));
	}

	public void testGetCompoundProperty()
	{
		assertEquals("compound1", AppConfig.get("compound", "property"));
		assertEquals("compound2", AppConfig.get("another.compound", "property"));
		assertEquals("compound2", AppConfig.get("another", "compound.property"));
	}

	public void testSystemOverrideTrash()
	{
		System.setProperty("te-code.common.appconfig.overridesystemproperties", "bogus");
		System.setProperty("property1", "spam");
		assertEquals("spam", AppConfig.get("property1"));
	}

	public void testPropertiesNotNull()
	{
		assertNotNull(AppConfig.getProperties());
	}

	public void testGetConfigLoaders()
	{
		List list = AppConfig.getConfigLoaders();
		assertNotNull(list);
		try
		{
			list.add("shouldn't work");
		}
		catch(UnsupportedOperationException e)
		{
			// expected
		}

		assertEquals(1, list.size());
	}

	public void testRegisterNullAppSupplier() throws IOException
	{
		AppConfig.registerAppSupplier(null);
	}

	public void testRegisterBogusAppSupplier()
	{
		try
		{
			AppConfig.registerAppSupplier(new ConfigSupplier() {
				public String getAppName()
				{
					return "coca-cola";
				}
			});
		}
		catch(IOException e)
		{
			// expected
		}
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(AppConfigTest.class);
	}
}
