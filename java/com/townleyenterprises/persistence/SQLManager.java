//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2002-2004, Andrew S. Townley
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
// File:	SQLManager.java
// Created:	Tue Dec 28 18:38:01 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.util.Collections;
import java.util.Set;

import com.townleyenterprises.common.OverrideManager;
import com.townleyenterprises.common.OverrideNode;
import com.townleyenterprises.common.UseLastOverrideStrategy;

/**
 * This class provides a way to manage SQL statements which may have
 * more efficient, or syntactically different versions depending on
 * the database vendor.  The actual statements are managed via the
 * override principles which are also provided by the {@link
 * com.townleyenterprises.common.ResourceManager} and the {@link
 * com.townleyenterprises.config.AppConfig} classes.
 * <p>
 * SQL statements are managed based on a defined naming structure and
 * new providers can be added simply by managing a new class in a
 * different package.  The statements should be contained in a file
 * called <code>statements.sql</code>.  Vendor replacements should be
 * defined in subsequent <code>statements_<em>VENDOR</em>.sql</code>
 * files where <code>VENDOR</code> is the value of the database type
 * obtained from the {@link PersistenceConfig} instance.  All values
 * will be merged so that the set of possible SQL statements includes
 * any vendor-specific ones which will override the standard ones with
 * the same name.
 * </p>
 * <p>
 * <em>
 * MIGRATION NOTE:  applications previously using the SQLProvider
 * class will need to have a minor change as this class does not
 * automatically remove the leading <code>'sql.'</code> prefix in the
 * same way.  Properties must be accessed by their complete key.  The
 * easiest way to migrate the code is to modify the properties file(s)
 * containing the SQL statements to ensure that they match the names
 * used in the code.
 * </em>
 * </p>
 *
 * @version $Id: SQLManager.java,v 1.1 2004/12/28 21:44:21 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public class SQLManager extends OverrideManager
		implements StatementProvider
{
	/**
	 * The constructor initializes the instance with the
	 * appropriate override strategy.
	 */

	public SQLManager()
	{
		setReadResolver(new UseLastOverrideStrategy());
	}

	/**
	 * This method returns the named statement 
	 *
	 * @param name the logical statement name
	 * @return the SQL statement for the specified environment
	 */

	public String getStatement(String name)
	{
		OverrideNode on = getNodeForReading(name);
		if(on == null)
			return null;

		StatementProvider sp = (StatementProvider)on.get();

		if(sp == null)
			return null;

		return sp.getStatement(name);
	}

	protected Set getKeys(Object object)
	{
		if(object instanceof StatementProvider)
		{
			return ((StatementProvider)object).getKeys();
		}

		return Collections.EMPTY_SET;
	}

	protected Object getValue(Object key, Object object)
	{
		if(object instanceof StatementProvider)
		{
			return ((StatementProvider)object).getStatement((String)key);
		}

		return null;
	}

	protected void setValue(Object key, Object object, Object value)
	{
	}
}
