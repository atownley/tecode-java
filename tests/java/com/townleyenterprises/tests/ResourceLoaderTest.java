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
// File:	ResourceLoaderTest.java
// Created:	Wed Aug 11 12:17:03 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.tests;

import java.util.Locale;
import javax.swing.ImageIcon;
import junit.framework.TestCase;

import com.townleyenterprises.common.*;

/**
 * Basic unit tests for the ResourceLoader class.  This class is here
 * rather than in the package because, since the package makes use of
 * this mechanism, we can't test the defaults very well without
 * putting test strings into the resources for the common package.
 *
 * @version $Id: ResourceLoaderTest.java,v 1.2 2004/08/11 16:24:55 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class ResourceLoaderTest extends TestCase
{
	public ResourceLoaderTest(String testname)
	{
		super(testname);
	}

	public void testGetString()
	{
		ResourceLoader loader = new ResourceLoader(this);
		assertEquals("Yes", loader.getString("yes"));
	}

	public void testGetStringFrench()
	{
		ResourceLoader loader = new ResourceLoader(
				this.getClass(),
				"strings", Locale.FRENCH);
		assertEquals("Oui", loader.getString("yes"));
	}

	public void testGetStringFrench2()
	{
		ResourceLoader loader = new ResourceLoader(this);
		assertEquals("Oui", loader.getString("yes",
				Locale.FRENCH));
	}

	public void testGetStringNamedBundle()
	{
		ResourceLoader loader = new 
			ResourceLoader(this.getClass(), "bubba");
		assertEquals("Yep", loader.getString("yes"));
	}

	public void testGetIcon()
	{
		ResourceLoader loader = new ResourceLoader(this);
		ImageIcon icon = loader.getIcon("panel.png");
		assertEquals(24, icon.getIconHeight());
		assertEquals(24, icon.getIconWidth());
	}

	public void testGetIconSpanish()
	{
		ResourceLoader loader = new ResourceLoader(
			this.getClass(), "strings",
			new Locale("es", "ES"));
		ImageIcon icon = loader.getIcon("panel.png");
		assertEquals(48, icon.getIconHeight());
		assertEquals(48, icon.getIconWidth());
	}

	public void testGetIconSpanish2()
	{
		ResourceLoader loader = new ResourceLoader(this);
		ImageIcon icon = loader.getIcon("panel.png",
			new Locale("es", "ES"));
		assertEquals(48, icon.getIconHeight());
		assertEquals(48, icon.getIconWidth());
	}

	public void testMissingKey()
	{
		ResourceLoader loader = new ResourceLoader(this);
		assertEquals("barf", loader.getString("barf"));
	}
		
	public void testErrorNullClass()
	{
		try
		{
			new ResourceLoader(null, "foo", Locale.getDefault());
			fail("no exception thrown");
		}
		catch(NullPointerException e)
		{
			int rc = e.getMessage().indexOf("cls");
			assertTrue("'cls' should be part of the string", -1 != rc);
		}
	}

	public void testErrorNullName()
	{
		try
		{
			new ResourceLoader(this.getClass(), null, 
					Locale.getDefault());
			fail("no exception thrown");
		}
		catch(NullPointerException e)
		{
			int rc = e.getMessage().indexOf("name");
			assertTrue("'name' should be part of the string", -1 != rc);
		}
	}

	public void testErrorNullLocale()
	{
		try
		{
			new ResourceLoader(this.getClass(), "foo",  null);
			fail("no exception thrown");
		}
		catch(NullPointerException e)
		{
			int rc = e.getMessage().indexOf("locale");
			assertTrue("'locale' should be part of the string", -1 != rc);
		}
	}

	public void testGetStringNullKey()
	{
		try
		{
			ResourceLoader loader = new ResourceLoader(this);
			loader.getString(null);
			fail("no exception thrown");
		}
		catch(NullPointerException e)
		{
			int rc = e.getMessage().indexOf("key");
			assertTrue("'key' should be part of the string", -1 != rc);
		}
	}

	public void testGetStringNullLocale()
	{
		try
		{
			ResourceLoader loader = new ResourceLoader(this);
			loader.getString("yes", null);
			fail("no exception thrown");
		}
		catch(NullPointerException e)
		{
			int rc = e.getMessage().indexOf("locale");
			assertTrue("'locale' should be part of the string", -1 != rc);
		}
	}

	public void testGetIconNullKey()
	{
		try
		{
			ResourceLoader loader = new ResourceLoader(this);
			loader.getString("yes", null);
			fail("no exception thrown");
		}
		catch(NullPointerException e)
		{
			int rc = e.getMessage().indexOf("locale");
			assertTrue("'locale' should be part of the string", -1 != rc);
		}
	}

	public void testGetIconNullLocale()
	{
		try
		{
			ResourceLoader loader = new ResourceLoader(this);
			loader.getString("yes", null);
			fail("no exception thrown");
		}
		catch(NullPointerException e)
		{
			int rc = e.getMessage().indexOf("locale");
			assertTrue("'locale' should be part of the string", -1 != rc);
		}
	}
}
