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
// Title:	PropertySorterTest.java
// Created: 	Sat Dec 11 20:23:21 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.util.ArrayList;
import java.util.Collections;

import junit.framework.TestCase;

import com.townleyenterprises.common.ResourceManager;

/**
 * This class exercises the PropertySorter class.
 *
 * @version $Id: PropertySorterTest.java,v 1.1 2004/12/12 00:57:05 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public class PropertySorterTest extends TestCase
{
	public PropertySorterTest(String testname)
	{
		super(testname);
	}

	public void setUp()
	{
		_list = new ArrayList();
		_list.add(new Data(0, "\u00e4pple"));
		_list.add(new Data(1, "banan"));
		_list.add(new Data(2, "p\u00e4ron"));
		_list.add(new Data(3, "orange"));
		_list.add(new Data(4, "Axelro"));
		_list.add(new Data(5, "A\u00e9roport"));
		_list.add(new Data(6, "a\u00e9roport"));

	}

	public void testCollateNonASCIILocaleEn()
	{
		Collections.sort(_list, new PropertySorter(Data.class,
				ResourceManager.getLocale("en"), _ss));

		assertEquals("a\u00e9roport", ((Data)_list.get(0)).getName());
		assertEquals("A\u00e9roport", ((Data)_list.get(1)).getName());
		assertEquals("\u00e4pple", ((Data)_list.get(2)).getName());
		assertEquals("Axelro", ((Data)_list.get(3)).getName());
		assertEquals("banan", ((Data)_list.get(4)).getName());
		assertEquals("orange", ((Data)_list.get(5)).getName());
		assertEquals("p\u00e4ron", ((Data)_list.get(6)).getName());
	}

	public void testCollateNonASCIILocaleDe()
	{
		Collections.sort(_list, new PropertySorter(Data.class,
				ResourceManager.getLocale("de"), _ss));

		assertEquals("a\u00e9roport", ((Data)_list.get(0)).getName());
		assertEquals("A\u00e9roport", ((Data)_list.get(1)).getName());
		assertEquals("\u00e4pple", ((Data)_list.get(2)).getName());
		assertEquals("Axelro", ((Data)_list.get(3)).getName());
		assertEquals("banan", ((Data)_list.get(4)).getName());
		assertEquals("orange", ((Data)_list.get(5)).getName());
		assertEquals("p\u00e4ron", ((Data)_list.get(6)).getName());
	}

	public void testCollateNonASCIILocaleSv()
	{
		Collections.sort(_list, new PropertySorter(Data.class,
				ResourceManager.getLocale("sv"), _ss));

		assertEquals("a\u00e9roport", ((Data)_list.get(0)).getName());
		assertEquals("A\u00e9roport", ((Data)_list.get(1)).getName());
		assertEquals("Axelro", ((Data)_list.get(2)).getName());
		assertEquals("banan", ((Data)_list.get(3)).getName());
		assertEquals("orange", ((Data)_list.get(4)).getName());
		assertEquals("p\u00e4ron", ((Data)_list.get(5)).getName());
		assertEquals("\u00e4pple", ((Data)_list.get(6)).getName());
	}

	ArrayList _list = null;
	SortSpecification[] _ss = { new SortSpecification("name") };
}
