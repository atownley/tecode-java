//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003-2004, Andrew S. Townley
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
// File:	DynamicFinder.java
// Created: 	Wed May 21 19:49:53 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.extensions.jaxor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.jaxor.api.EntityInterface;
import net.sourceforge.jaxor.SystemSQLException;
import net.sourceforge.jaxor.util.SystemException;
import net.sourceforge.jaxor.JaxorSession;
import net.sourceforge.jaxor.EntityNotFoundException;

import com.townleyenterprises.persistence.QueryAdapter;
import com.townleyenterprises.persistence.QueryEvent;
import com.townleyenterprises.persistence.QueryHandler;

/**
 * This class is a more dynamic implemnetation of Jaxor's BaseFinder
 * which can be used to create Jaxor entities from the results of
 * queries not necessarily limited to the individual table.  Like the
 * QueryHandler, it does not implement Jaxor's Laxy Loading mechanism.
 *
 * @version $Id: DynamicFinder.java,v 1.1 2004/01/30 11:48:43 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class DynamicFinder extends QueryAdapter
{
	/**
	 * The constructor initializes the DynamicFinder with the
	 * implementation class that will be used for the returned
	 * entities.
	 *
	 * @param klass the implementation class
	 */

	public DynamicFinder(Class klass)
	{
		_klass = klass;
		_handler.addQueryListener(this);
	}

	/**
	 * This method of the QueryLister interface will be used to
	 * handle the dynamic instantiation of the specific Entity
	 * instances.
	 *
	 * @param event the event with the result set
	 * @exception SQLException
	 * 	if anything goes wrong reading the results
	 */

	public void nextRow(QueryEvent event) throws SQLException
	{
		ResultSet rs = event.getResultSet();

		_list.add(load(rs));
	}

	/**
	 * This method is used to execute the query and return a list
	 * of the new entity instances which are created by the
	 * results.
	 *
	 * @param sql the query string to execute
	 * @param args the arguments to the query
	 * @return a List of entity instances
	 */
	
	public List find(final String sql, final Object[] args)
	{
		try
		{
			_list.clear();
			_handler.execute(sql, args);
			return _list;
		}
		catch(SQLException e)
		{
			throw new SystemSQLException(e);
		}
	}

	/**
	 * This method is used to execute the query and return a
	 * single result.
	 *
	 * @param sql the query to execute
	 * @param args the arguments to the query
	 * @return an instance of the EntityInterface
	 */

	public EntityInterface findUnique(final String sql,
					final Object[] args)
	{
		List list = find(sql, args);

		if(list.size() > 1)
			throw new SystemException("error:  more than one row returned for findUnique");
		else if(list.size() == 0)
			throw new EntityNotFoundException();

		return (EntityInterface)list.get(0);
	}

	/**
	 * This method is exposed so that query results which have the
	 * appropriate columns to populate the entity can be used to
	 * generate the target entity without requiring a second call
	 * to the database.
	 *
	 * @param rs the ResultSet
	 * @return the new Entity instance
	 */

	public EntityInterface load(final ResultSet rs)
	{
		if(_klass == null)
		{
			throw new SystemException("error:  no Entity class specified with query.");
		}

		EntityInterface entity = createInstance(_klass);
		try
		{
			entity.getFields().load(rs);
		}
		catch(SQLException e)
		{
			throw new SystemSQLException(e);
		}

		// we are going to use Jaxor's cache though
		EntityInterface cache = JaxorSession.getInstanceCache().updateCache(entity);
		if(cache == entity || cache == null)
		{
			entity.afterLoad();
		}

		return cache;
	}
	
	/**
	 * This method is 100% taken from the Jaxor BaseFinder
	 * implementation to create the instances of the Entity
	 * classes via reflection.
	 *
	 * @param klass the implementation class to create
	 * @return the new instance of the class
	 */

	private EntityInterface createInstance(Class klass)
	{
		try
		{
			return (EntityInterface)klass.newInstance();
		}
		catch(InstantiationException e)
		{
			throw new SystemException(e);
		}
		catch(IllegalAccessException e)
		{
			throw new SystemException(e);
		}
	}

	/** the class for the objects we are supposed to create */
	private final Class		_klass;

	/** the list of our results */
	private final ArrayList		_list = new ArrayList();

	/** the query handler to execute the queries */
	private QueryHandler		_handler = new QueryHandler();
}
