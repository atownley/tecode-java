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
// File:	PersistenceConfig.java
// Created:	Wed Jan 28 17:31:03 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.Properties;

import com.townleyenterprises.common.AppConfig;
import com.townleyenterprises.common.PropertyResolver;
import com.townleyenterprises.config.ConfigSupplier;

/**
 * This class is used to provide a flexible mechanism to resolving the
 * required database connection parameter values and encapsulate the
 * differences between various database vendor connection URLs.
 *
 * <h3>
 * Properties
 * </h3>
 *
 * <p>
 * This class uses a set of properties to provide overridable values
 * for making the connection to a specific server.  The master
 * property is the type of database to be used, as this value is used
 * to provide overrides.  This mechanism allows completely different
 * database configurations to coexist enabling easy swapping of the
 * connectivity information.
 * </p>
 *
 * <table width="100%" border="1" cellspacing="0" cellpadding="5">
 * <tr bgcolor="#CCCCFF">
 * <th align="left">
 * Property
 * </th>
 * <th align="left">
 * Description
 * </th>
 * </tr>
 * 
 * <tr>
 * <td align="left" valign="top">
 * database.type
 * </td>
 * <td valign="left">
 * The symbolic name of the database type.  This name is entirely
 * dependent on the site, however the name or abbreviation of the
 * database vendor is generally used, e.g. <code>postgresql</code>.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * database.user
 * </td>
 * <td valign="left">
 * The login name for the database connection.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * database.password
 * </td>
 * <td valign="left">
 * The password for the database connection.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * database.host
 * </td>
 * <td valign="left">
 * The host name for the database connection.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * database.name
 * </td>
 * <td valign="left">
 * The name of the database for the connection. 
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left">
 * <em>${database.type}</em>.user
 * </td>
 * <td valign="left">
 * The login name for the database connection for the given database
 * type.  This value overrides the default parameter.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * <em>${database.type}</em>.password
 * </td>
 * <td valign="left">
 * The password for the database connection for the given database
 * type.  This value overrides the default parameter.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * <em>${database.type}</em>.host
 * </td>
 * <td valign="left">
 * The host name for the database connection for the given database
 * type.  This value overrides the default parameter.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * <em>${database.type}</em>.database.name
 * </td>
 * <td valign="left">
 * The name of the database for the connection for the given database
 * type.  This value overrides the default parameter.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * <em>${database.type}</em>.jdbc.params
 * </td>
 * <td valign="left">
 * The parameter names necessary to supply to the JDBC driver URL.
 * Parameter names can be anything, however the special parameter name
 * <stong>database</strong> refers to the value of the database name.
 * Each parameter should have value in the properties file for the
 * current database type.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * <em>${database.type}</em>.jdbc.url
 * </td>
 * <td valign="left">
 * This property is a string processed by the java.text.MessageFormat
 * class to generate the correct JDBC URL for the connection based on
 * the parameter values specified by the
 * <code><em>${database.type}</em>.jdbc.params</code> property.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * <em>${database.type}</em>.jdbc.driver
 * </td>
 * <td valign="left">
 * This property specifies the fully-qualified class name of the JDBC
 * driver for this database type.
 * </td>
 * </tr>
 *
 * <tr>
 * <td align="left" valign="top">
 * <em>${database.type}.${param}</em>
 * </td>
 * <td valign="left">
 * For each token in the value of the
 * <code><em>${database.type}</em>.jdbc.params</code> property, a parameter may be
 * defined based on the database type and the specific token.  The
 * value of this parameter is used to specify the appropriate
 * arguments to the
 * <code><em>${database.type}</em>.jdbc.url</code> property value.
 * </td>
 * </tr>
 *
 * </table>
 *
 * <h3>
 * Example
 * </h3>
 *
 * <p>
 * The above description becomes clearer with a concrete example.
 * Given the need to run an application against a test database server
 * (PostgreSQL) and a production database server (Oracle), the
 * following properties file could be supplied.  Based on changing
 * only the database type property value, the application can easily
 * be switched from one configuration to another.
 * </p>
 *
 * <pre>
 * database.type=postgresql
 * #database.type=oracle
 * database.user=appname
 * database.password=secret
 * database.name=data
 *
 * postgresql.host=testbox.myco.com
 * postgresql.port=5432
 * postgresql.jdbc.params=host port database
 * postgresql.jdbc.url=jdbc:postgresql://{0}:{1}/{2}
 * postgresql.jdbc.driver=org.postgresql.Driver
 *
 * oracle.host=production.myco.com
 * oracle.port=1521
 * oracle.database.name=production
 * oracle.jdbc.params=host port database
 * oracle.jdbc.url=jdbc:oracle:thin:@{0}:{1}:{2}
 * oracle.jdbc.driver=oracle.jdbc.OracleDriver
 * </pre>
 *
 * @version $Id: PersistenceConfig.java,v 1.2 2004/12/28 21:47:55 atownley Exp $
 * @author <a href="mailto:adz1092@nestscape.net">Andrew S. Townley</a>
 * @since 2.1
 */

public final class PersistenceConfig
{
	/**
	 * This version of the constructor should not normally be used
	 * unless the properties are not maintained by a
	 * ConfigSupplier.
	 *
	 * @param properties the java.util.Properties instance
	 */

	public PersistenceConfig(Properties properties)
	{
		_resolver = new PropertyResolver(properties);
	}

	/**
	 * This version of the constructor takes a ConfigSupplier
	 * interface to use to retrieve the properties.  This should
	 * be the preferred constructor for all new code.
	 *
	 * @param config the ConfigSupplier instance
	 * @since 3.0
	 */

	public PersistenceConfig(ConfigSupplier config)
	{
		_resolver = new PropertyResolver(config);
	}

	/**
	 * This version of the constructor works with the {@link
	 * com.townleyenterprises.common.AppConfig} class to provide
	 * the property information.
	 *
	 * @deprecated As of the 3.0 release, the preferred
	 * constructor should be the version taking a {@link
	 * com.townleyenterprises.config.ConfigSupplier} instance
	 * because this allows multiple application configurations to
	 * coexist within the same JVM.
	 */

	public PersistenceConfig()
	{
		_resolver = new PropertyResolver(AppConfig.getProperties());
	}

	/**
	 * This method returns the username for the current
	 * connection.  If a user is set for the current database
	 * type, that user is returned, otherwise the default user is
	 * returned.
	 *
	 * @return the user or null if not found
	 */

	public String getUser()
	{
		return getProp("user");
	}

	/**
	 * This method returns the password for the current
	 * connection.  If a password is set for the current database
	 * type, that password is returned, otherwise the default
	 * password is returned.
	 *
	 * @return the password or null if not found
	 */

	public String getPassword()
	{
		return getProp("password");
	}

	/**
	 * This method returns the host for the current
	 * connection.  If a host is set for the current database
	 * type, that host is returned, otherwise the default
	 * host is returned.
	 *
	 * @return the host or null if not found
	 */

	public String getHost()
	{
		return getProp("host");
	}

	/**
	 * This method returns the driver for the current
	 * connection.  If a driver is set for the current database
	 * type, that driver is returned, otherwise the default
	 * driver is returned.
	 *
	 * @return the driver or null if not found
	 */

	public String getDriverName()
	{
		return getProp("jdbc.driver");
	}

	/**
	 * This method returns the URL for the current
	 * connection, correctly populated with all of the parameter
	 * values.
	 *
	 * @return the URL as a string or null
	 */

	public String getConnectionURL()
	{
		String url = getProp("jdbc.url");
		if(url == null || url.length() == 0)
			return null;

		String tokens = getProp("jdbc.params");
		StringTokenizer st = new StringTokenizer(tokens);
		Object[] args = new Object[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens())
		{
			String val = null;
			String tok = st.nextToken();
			if("database".equals(tok))
			{
				val = _resolver.get(getDatabaseType(),
						"database.name");
				if(val == null || val.length() == 0)
				{
					val = _resolver.get("database.name");
				}
			}
			else
			{
				val = getProp(tok);
			}

			if(val == null || val.length() == 0)
			{
				System.err.println("PersistenceConfig error:  unable to find value for property '" + tok + "'.");
				return null;
			}
			
			args[i++] = val;
		}

		return MessageFormat.format(url, args);
	}

	/**
	 * This method returns the current value of the database type
	 *
	 * @return the database type
	 */

	private String getDatabaseType()
	{
		String dbtype = _resolver.get("database.type");
		if(dbtype == null || dbtype.length() == 0)
			return "";

		return dbtype;
	}

	/**
	 * This method encapsulates getting the property overrides.
	 *
	 * @param property the property key
	 * @return the property value
	 */

	private String getProp(String property)
	{
		String s = _resolver.get(getDatabaseType(), property);
		if(s == null || s.length() == 0)
			s = _resolver.get("database", property);

		return s;
	}

	/** the PropertyResolver to use */
	private PropertyResolver	_resolver = null;
}
