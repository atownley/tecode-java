//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003, Andrew S. Townley
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
// File:	SQLFilterAdapter.java
// Created:	Tue May 20 11:43:39 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.townleyenterprises.filter.*;

/**
 * This class is responsible for generating SQL given a filter and
 * optional sort specification.
 *
 * @version $Id: SQLFilterAdapter.java,v 1.2 2003/06/08 21:57:37 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class SQLFilterAdapter
{
	/**
	 * This version of the constructor takes the specified Filter
	 * instance and generates the SQL necessary to execute the
	 * query.
	 *
	 * @param filter the filter to be executed
	 */

	public SQLFilterAdapter(final Filter filter)
	{
		_filter = filter;
	}

	/**
	 * This version of the constructor includes the filter along
	 * with a map of the classes to the appropriate table names.
	 *
	 * @param filter the filter to be executed
	 * @param tablemap the map of the table names
	 */

	public SQLFilterAdapter(final Filter filter,
			final HashMap tablemap)
	{
		this(filter);
		_tablemap = tablemap;
	}

	/**
	 * This version of the constructor includes the filter and 
	 * a map of the classes to the appropriate table names, but
	 * also includes a property to column name mapping for
	 * specific classes.  The values for the colmap should be
	 * HashMap instances mapping the property names to the columns.
	 *
	 * @param filter the filter to be executed
	 * @param tablemap the map of the table names
	 * @param colmap the map of the column names for each class
	 */

	public SQLFilterAdapter(final Filter filter,
			final HashMap tablemap, final HashMap colmap)
	{
		this(filter, tablemap);
		_colmap = colmap;
	}

	/**
	 * This method is used to actually build the SQL query based
	 * on the arguments for the constructor if the parameter is
	 * null.  If the parameter is not null, the values of the
	 * parameter are used instead.
	 *
	 * @param what the "what" to select
	 */

	private void buildQuery(String what)
	{
		// build the query, must be evaluated so we have the
		// columns and tables
		String		where = evaluate(_filter);
		StringBuffer	buf = new StringBuffer("SELECT ");
	
		if(what == null)
		{
			for(Iterator i = _columns.keySet().iterator(); i.hasNext(); )
			{
				buf.append(i.next());
				if(i.hasNext())
					buf.append(",");
			}
		}
		else
		{
			buf.append(what);
		}

		buf.append(" FROM ");

		for(Iterator i = _tables.keySet().iterator(); i.hasNext(); )
		{
			buf.append(i.next());
			if(i.hasNext())
				buf.append(",");
		}
		buf.append(" WHERE ");

		buf.append(where);

		_sql = buf.toString();
	}

	/**
	 * This method provides access to the SQL for this query
	 *
	 * @param what the values to select
	 * @return the SQL
	 */

	public String toSQL(String what)
	{
		buildQuery(what);
		return _sql;
	}

	/**
	 * This method allows for specifying sort specifications to
	 * the generated SQL.  They are not given in the constructor,
	 * because the same query may be used over and over with only
	 * the order being changed.
	 * <p>
	 * Note:  using this version assumes that all of the
	 * properties in the sort specification are not mapped and
	 * that they are unique for all classes in the filter.
	 * </p>
	 *
	 * @param sortmap the SortSpecification to be used
	 * @return the SQL
	 */

	public String toSQL(SortSpecification[] sort)
	{
		return toSQL((Class)null, sort);
	}

	public String toSQL(Class klass, SortSpecification[] sort)
	{
		HashMap map = new HashMap();
		map.put(klass, sort);

		return toSQL(map);
	}

	public String toSQL(HashMap sortmap)
	{
		buildQuery(null);
		StringBuffer buf = new StringBuffer(_sql);

		if(sortmap != null && sortmap.size() > 0)
			buf.append(" ORDER BY");

		for(Iterator i = sortmap.keySet().iterator(); i.hasNext(); )
		{
			Class klass = (Class)i.next();
			SortSpecification[] sort = (SortSpecification[])sortmap.get(klass);

			if(sort != null && sort.length > 0)
			{
				// make sure all of the sort specifications are valid
				for(int j = 0; j < sort.length; ++j)
				{
					buf.append(" ");
					buf.append(getColumnName(klass, sort[j].getProperty()));
					buf.append(" ");
					buf.append(sort[j].getOrder());
					if(j < sort.length - 1)
						buf.append(",");
				}
			}

			if(i.hasNext())
				buf.append(",");
		}
		return buf.toString();
	}

	/**
	 * This method provides access to the query parameters.
	 *
	 * @return the query parameters as an array
	 */

	public Object[] getQueryParameters()
	{
		Object[] params = new Object[_parameters.size()];
		return _parameters.toArray(params);
	}

	/**
	 * This method is used to retrieve the actual table name,
	 * based on the value of the QueryFilter's proxy class.
	 *
	 * @param klass the class for the table
	 * @return the appropriate table name
	 */

	private String getTable(Class klass)
	{
		String tab = null;

		if(_tablemap != null)
			tab = (String)_tablemap.get(klass);

		if(tab == null)
		{
			tab = klass.toString();
			if(tab.startsWith("class "))
				tab = tab.substring(tab.indexOf(' ') + 1);
		}

		return tab;
	}

	/**
	 * This method is used to evaluate the column name for the
	 * given class and property based on the column name mappings.
	 *
	 * @param klass the class containing the properties
	 * @param property the property name
	 * @return the actual table column name
	 */

	private String getColumnName(Class klass, String property)
	{
		String col = null;

		if(_colmap == null || klass == null)
			return property;

		HashMap columns = (HashMap)_colmap.get(klass);
		if(columns != null)
		{
			col = (String)columns.get(property);
			if(col == null)
				col = property;
		}
		else
		{
			col = property;
		}

		return col;
	}

	/**
	 * This method translates the filter into an SQL92 fragment.
	 *
	 * @param filter the filter to evaluate
	 * @return the SQL string for the filter
	 */

	private String evaluate(Filter filter)
	{
		if(filter instanceof LogicalFilter)
		{
			StringBuffer s = new StringBuffer("(");
			String op = "";
			LogicalFilter lf = (LogicalFilter)filter;
			if(lf instanceof LogicalAndFilter)
				op = " AND ";
			else if(lf instanceof LogicalOrFilter)
				op = " OR ";

			for(Iterator i = lf.iterator(); i.hasNext(); )
			{
				s.append(evaluate((Filter)i.next()));
				if(i.hasNext())
					s.append(op);
			}

			s.append(")");
			return s.toString();
		}
		else if(filter instanceof SubstringFilter)
		{
			SubstringFilter sf = (SubstringFilter)filter;
			Class proxy = sf.getSubjectClass();
			String tabname = getTable(proxy);
			String prop = tabname + "." + 
				getColumnName(proxy, sf.getProperty());

			_tables.put(tabname, null);
			_parameters.add("%" + ((String)sf.getValue()) + "%");
			_columns.put(prop, null);
			return "(" + prop + " LIKE ?)";
		}
		else if(filter instanceof QueryFilter)
		{
			QueryFilter qf = (QueryFilter)filter;
			Class proxy = qf.getSubjectClass();
			String tabname = getTable(proxy);
			String prop = tabname + "." +
				getColumnName(proxy, qf.getProperty());

			_tables.put(tabname, null);
			_parameters.add(qf.getValue());
			_columns.put(prop, null);
			return "(" + prop + " " +
				qf.getOperator().toString() + " ?)";
		}
		else if(filter instanceof LogicalNotFilter)
		{
			LogicalNotFilter nf = (LogicalNotFilter)filter;
			return "NOT (" + evaluate(nf.getFilter()) + ")";
		}

		System.err.println("warning:  unable to evaluate filter (" + filter + ")");
		return null;
	}

	/** this is our base SQL */
	private String			_sql;

	/** this is our private reference to the filter */
	private final Filter		_filter;

	/** this is the list of query parameters */
	private final ArrayList		_parameters = new ArrayList();

	/** this map holds all of the query columns */
	private final HashMap		_columns = new HashMap();

	/** this map holds all of the query tables */
	private final HashMap		_tables = new HashMap();

	/** this map holds the class/table mappings to be used */
	private HashMap			_tablemap = new HashMap();

	/** this map holds the property/column mappings to be used */
	private HashMap			_colmap = new HashMap();
}
