//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2004-2005, Andrew S. Townley
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
// File:	StatementLoader.java
// Created:	Wed Nov 12 14:06:28 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.io.IOException;
import java.util.Set;

import com.townleyenterprises.common.ConfigLoader;

/**
 * @version $Id: StatementLoader.java,v 1.1 2004/12/28 21:44:22 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public class StatementLoader implements StatementProvider
{
	/**
	 * This constructor takes a class to use as the basis for
	 * finding SQL statements.
	 *
	 * @param cls the class providing the base location
	 * @param vendor the vendor type of the current configuration
	 * @exception IOException
	 * 	if the statements can't be loaded
	 */

	public StatementLoader(Class cls, String vendor)
			throws IOException
	{
		String sql = "statements";

		_klass = cls;
		_vendor = vendor;

		StringBuffer buf = new StringBuffer(sql);
		buf.append("_");
		buf.append(vendor);
		buf.append(".sql");
		
		_props = new ConfigLoader(_klass, sql + ".sql", 
					buf.toString());
	}

	/**
	 * This method is used to find a given statement string based
	 * on the default locale.
	 *
	 * @param key the resource string key
	 * @return the string for the key
	 */

	public String getStatement(String key)
	{
		return _props.getProperty(key);
	}

	public Set getKeys()
	{
		return _props.getKeys();
	}

	/**
	 * Provide some clue as to what we're doing and who we are.
	 */

	public String toString()
	{
		StringBuffer buf = new StringBuffer("[StatementLoader (");
		buf.append(hashCode());
		buf.append("): klass='");
		buf.append(_klass);
		buf.append("'; vendor='");
		buf.append(_vendor);
		buf.append("' ]");

		return buf.toString();
	}

	/** a reference to the class */
	private Class			_klass;

	/** a reference to the vendor */
	private String			_vendor;

	/** the properties */
	private ConfigLoader		_props;
}
