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
// File:	QueryHandler.java
// Created:	Wed May 21 13:34:34 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class encapsulates the core functionality of making a database
 * query into a single place.  Queries are executed based on the
 * settings of certain properties for the class, and listeners may be
 * registered to receive notification of each row to be processed.
 *
 * <h4>
 * Migration from Earlier Releases
 * </h4>
 * 
 * <p>
 * Prior to version 2.1, this class relied on a pre-release version of
 * Jaxor being available to perform tasks such as setting the query
 * parameters and supplying connections.  With version 2.1, this link
 * is no longer present, however the previous behavior is available by
 * placing one of the appropriate extensions in the classpath prior to
 * the main te-common.jar.
 * </p>
 *
 * @version $Id: QueryHandler.java,v 1.5 2004/07/28 10:33:59 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class QueryHandler
{
	/**
	 * The constructure allocates a new set of resources for
	 * executing a specific query.
	 */

	public QueryHandler()
	{
	}

	/**
	 * This constructor allows setting a connection factory for
	 * this instance.
	 *
	 * @param factory the connection factory to use
	 * @since 2.1
	 */

	public QueryHandler(ConnectionFactory factory)
	{
		_factory = factory;
	}

	/**
	 * This method causes the query handler to abort processing
	 * the active query.
	 */

	public void abort()
	{
		_abort = true;
	}

	/**
	 * This method adds a QueryListener to the list of objects
	 * which will retrieve notifications of various QueryHandler
	 * events.
	 *
	 * @param listener the QueryListener
	 */

	public void addQueryListener(QueryListener listener)
	{
		_listeners.add(listener);
	}

	/**
	 * This method actually executes the query based on the
	 * configured property values and the query and query
	 * parameters.  For each row in the result set, each
	 * registered listener is notified so that they may do
	 * whatever is necessary (create an object for the row, or
	 * generate a summary report, or whatever)
	 * <p>
	 * <em>
	 * NOTE:  this code is borrowed almost exactly from Jaxor's
	 * BaseFinder.executeQuery method.
	 * </em>
	 * </p>
	 *
	 * @param query the SQL query to be executed
	 * @param params the parameters to be supplied to the query
	 * @return a List containing the results.  In the event
	 * 	that there are not <code>count</code> number of rows,
	 * 	only the specific rows will be retrieved.  If there
	 * 	are less rows than the start index, an empty list will
	 * 	be returned.
	 * @exception SQLException
	 * 	if an SQLException was thrown during query execution
	 */

	public void execute(final String query, final Object[] params)
				throws SQLException
	{
		Connection		con = null;
		PreparedStatement	stmt = null;
		QueryParams		args = new QueryParams();
	
		if(params != null && params.length > 0)
		{
			for(int i = 0; i < params.length; ++i)
			{
				args.add(params[i]);
			}
		}

		try
		{
			if(_factory != null)
				con = _factory.getConnection();
			else if(_defaultFactory != null)
				con = _defaultFactory.getConnection();

			stmt = con.prepareStatement(query);
			args.setArgs(stmt);
			ResultSet rs = stmt.executeQuery();
			if(_start != 0)
				rs.relative(_start);

			int rows = 0;
			_queryExecuted(new QueryEvent(query));
			while(rs.next())
			{
				QueryEvent e;
				e = new QueryEvent(query, rs, ++rows);
				_processRow(e);
				if((_count != -1 && rows == _count) ||
						_abort)
				{
					break;
				}
			}

			rs.close();
			_queryCompleted(new QueryEvent(query));
		}
		finally
		{
			try
			{
				if(stmt != null)
					stmt.close();
				if(con != null)
					con.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to unregister an object from the list
	 * of listeners.  If the listener is not registered, no error
	 * is raised.
	 *
	 * @param listener the QueryLister
	 */

	public void removeQueryListener(QueryListener listener)
	{
		_listeners.remove(listener);
	}

	/**
	 * This method initializes the connection factory for this
	 * instance.  This factory will be used instead of attempting
	 * to obtain a connection from the JaxorSession.
	 *
	 * @param factory the ConnectionFactory instance
	 */

	public void setConnectionFactory(ConnectionFactory factory)
	{
		_factory = factory;
	}

	/**
	 * This method sets the start row for processing the results.
	 *
	 * @param the absolute index (1-based) of the row on which to
	 * 	start processing query results.
	 */

	public void setStart(int start)
	{
		_start = start;
	}

	/**
	 * This method is called by the QueryHandler after notifiying
	 * the listeners of the next row.
	 *
	 * @param event the QueryEvent to propagate
	 * @exception SQLException
	 * 	if there is an issue with processing the result set
	 */

	protected void nextRow(QueryEvent event)
				throws SQLException
	{
	}

	/**
	 * This method actually handles the processing of the row.  It
	 * provides hook methods to be used by derived classes so
	 * that they don't forget to call the listener notification.
	 *
	 * @param event the QueryEvent to propagate
	 * @exception SQLException
	 * 	if there is an issue with processing the result set
	 */

	private void _processRow(final QueryEvent event)
				throws SQLException
	{
		notifyNextRow(event);
		nextRow(event);
	}

	/**
	 * This method actually notifies the listeners that a query
	 * has been completed.
	 *
	 * @param event the QueryEvent to propagate
	 */

	private void _queryCompleted(final QueryEvent event)
				throws SQLException
	{
		notifyCompleted(event);
	}

	/**
	 * This method actually notifies the listeners that a query
	 * has been executed.
	 *
	 * @param event the QueryEvent to propagate
	 */

	private void _queryExecuted(final QueryEvent event)
				throws SQLException
	{
		_abort = false;
		notifyExecuted(event);
	}

	/**
	 * This method takes care of notifying the listeners about the
	 * query completion
	 *
	 * @param event the event to propagate
	 * @exception SQLException
	 * 	if one of the listeners throws an exception
	 */

	private void notifyCompleted(QueryEvent event)
				throws SQLException
	{
		for(Iterator i = _listeners.iterator(); i.hasNext(); )
		{
			QueryListener listener = (QueryListener)i.next();
			listener.queryCompleted(event);
		}
	}

	/**
	 * This method takes care of notifying the listeners about the
	 * query execution
	 *
	 * @param event the event to propagate
	 * @exception SQLException
	 * 	if one of the listeners throws an exception
	 */

	private void notifyExecuted(QueryEvent event)
				throws SQLException
	{
		for(Iterator i = _listeners.iterator(); i.hasNext(); )
		{
			QueryListener listener = (QueryListener)i.next();
			listener.queryExecuted(event);
		}
	}

	/**
	 * This method takes care of notifying the listeners about the
	 * row changes.
	 *
	 * @param event the event to propagate
	 * @exception SQLException
	 * 	if one of the listeners throws an exception
	 */

	private void notifyNextRow(QueryEvent event)
				throws SQLException
	{
		for(Iterator i = _listeners.iterator(); i.hasNext(); )
		{
			QueryListener listener = (QueryListener)i.next();
			listener.nextRow(event);
		}
	}

	/**
	 * This method is used to set the default conneciton factory
	 * for all instances of this class
	 *
	 * @param factory the factory
	 */

	public static void setDefaultConnectionFactory(ConnectionFactory factory)
	{
		_defaultFactory = factory;
	}

	/** our registered listeners */
	private List				_listeners = new ArrayList();

	/** represents the number of rows which should be retrieved */
	private int				_count = -1;

	/** represents the configured starting point for results */
	private int				_start = 0;

	/** indicates if the processing should be aborted */
	private boolean				_abort = false;

	/** our connection factory */
	private ConnectionFactory		_factory = null;

	/** the default factory */
	private static ConnectionFactory	_defaultFactory = null;
}
