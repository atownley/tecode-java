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
// File:	QueryEvent.java
// Created:	Wed May 21 15:13:22 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.sql.ResultSet;

/**
 * This class provides a container for query notification data.
 *
 * @version $Id: QueryEvent.java,v 1.1 2003/06/07 18:42:35 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class QueryEvent
{
	/**
	 * This constructor is inialized with only the query.
	 *
	 * @param query the SQL query string executed
	 */

	public QueryEvent(String query)
	{
		this(query, null, 0);
	}

	/**
	 * This constructor fully initializes the class from a query
	 * result.
	 *
	 * @param query the SQL query string
	 * @param rs the ResultSet
	 * @param row the row
	 */

	public QueryEvent(String query, ResultSet rs, int row)
	{
		_query = query;
		_rs = rs;
		_row = row;
	}

	public String getQuery()
	{
		return _query;
	}

	public ResultSet getResultSet()
	{
		return _rs;
	}

	public int getRow()
	{
		return _row;
	}

	private final String	_query;
	private final ResultSet	_rs;
	private final int	_row;
}
