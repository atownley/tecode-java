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
// File:	SQLProvider.java
// Created:	Tue Oct 22 18:02:43 IST 2002
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.io.IOException;
import com.townleyenterprises.common.AppConfig;
import com.townleyenterprises.common.ConfigLoader;
import com.townleyenterprises.common.ConfigSupplier;

/**
 * This class provides a way to make the database queries that are
 * used by these classes more modular and separate from the code.
 * This class will provide access to the statements in the form of a
 * HashMap-style interface, so that the SQL statements are accessed by
 * a name.
 * <p>
 * It should also be said that there are no public, non-static methods
 * for this class.  The only public method is get() and will return
 * the specific statement or null, if it doesn't exist.
 * </p>
 * <p>
 * Two separate files are used by this class.  The first file is named
 * 'statements.sql' and contains all of the vendor-independent
 * SQL statments.  Like the standard resource bundle, various
 * statements_VENDOR.sql files are also consulted to create a
 * unified statement map for the particular vendor.  This vendor is
 * retrieved from the application's AppConfig class.
 * </p>
 * <p>
 * Prior to using this class, it should be configured using the
 * initialize() method.
 * </p>
 * @deprecated The functionality of this class has been replaced with
 * the {@link SQLManager} class which provides a more flexible
 * approach to the problem, but still manages to have vendor-specific
 * SQL coexist nicely with ANSI SQL.
 *
 * @version $Id: SQLProvider.java,v 1.4 2004/12/28 21:48:30 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class SQLProvider 
{
	// don't want instances
	private SQLProvider()
	{
	}

	/**
	 * This method returns the named statement based on the
	 * current environment.
	 *
	 * @param name the logical statement name
	 * @return the SQL statement for the specified environment
	 */

	public static String get(String name)
	{
		return getConfig().getProperty("sql", name);
	}

	/**
	 * This helper method is used to ensure that we have a valid
	 * properties file from which to pull the configuration
	 * information.
	 */

	private static ConfigLoader getConfig()
	{
		if(props == null)
		{
			String vendor = AppConfig.get("database.type");
			StringBuffer buf = new StringBuffer(sql);
			buf.append("_");
			buf.append(vendor);
			buf.append(".sql");
			
			ConfigSupplier cs = AppConfig.getPersistenceConfigSupplier();

			try
			{
				props = new ConfigLoader(cs.getClass(),
						sql + ".sql", buf.toString());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		return props;
	}

	/** this is our global list of statements */
	private static ConfigLoader	props = null;

	private static String		sql = "statements";
}
