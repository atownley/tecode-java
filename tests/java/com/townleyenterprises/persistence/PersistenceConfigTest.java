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
// File:	PersistenceConfigTest.java
// Created:	Thu Jan 22 22:55:31 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.util.Properties;
import junit.framework.TestCase;

/**
 * Basic unit tests for the PersistenceConfig class
 *
 * @version $Id: PersistenceConfigTest.java,v 1.2 2004/07/28 10:34:00 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class PersistenceConfigTest extends TestCase
{
	public PersistenceConfigTest(String testname)
	{
		super(testname);
	}

	public void setUp()
	{
		_props.clear();
		_props.put("database.type", "postgresql");
		_props.put("database.user", "appname");
		_props.put("database.password", "secret");
		_props.put("database.name", "data");
		_props.put("postgresql.user", "postgres");
		_props.put("postgresql.host", "testbox.myco.com");
		_props.put("postgresql.port", "5432");
		_props.put("postgresql.jdbc.params", "host port database");
		_props.put("postgresql.jdbc.url", "jdbc:postgresql://{0}:{1}/{2}");
		_props.put("postgresql.jdbc.driver", "org.postgresql.Driver");
		_props.put("oracle.host", "production.myco.com");
		_props.put("oracle.port", "1521");
		_props.put("oracle.jdbc.params", "host port database");
		_props.put("oracle.jdbc.url", "jdbc:oracle:thin:@{0}:{1}:{2}");
		_props.put("oracle.jdbc.driver", "oracle.jdbc.OracleDriver");
	}

	public void testGetUser()
	{
		assertEquals("postgres", _config.getUser());
		
		_props.put("database.type", "oracle");
		assertEquals("appname", _config.getUser());
	}

	public void testGetPassword()
	{
		assertEquals("secret", _config.getPassword());
	}

	public void testGetHost()
	{
		assertEquals("testbox.myco.com", _config.getHost());
	}

	public void testGetDriver()
	{
		assertEquals("org.postgresql.Driver", _config.getDriverName());
	}

	public void testGetConnectionURL()
	{
		assertEquals("jdbc:postgresql://testbox.myco.com:5432/data",
			_config.getConnectionURL());
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(PersistenceConfigTest.class);
	}

	Properties		_props = new Properties();
	PersistenceConfig	_config = new PersistenceConfig(_props);
}
