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
// File:	ResultRow.java
// Created:	Tue May 20 23:25:18 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface should be implemented by classes that wish to be
 * dynamicly loaded from the {@link DynamicFinder} based on the
 * results of the execution of a query.
 * <p>
 * From the Jaxor perspective, this is a really simple version of an
 * EntityRow because the classes which implement this interface alone
 * are not interested in being persisted to the database.  It is also
 * effectively merging the functionality of the AbstractMapper class
 * because no dynamic mapping functionality is required either.
 * </p>
 *
 * @version $Id: ResultRow.java,v 1.2 2004/01/25 19:22:39 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public interface ResultRow
{
	/**
	 * This method needs to be implemented in order to
	 * populate an implementing class based on the values of the
	 * ResultSet.  It isn't perfect, but it alieviates almost all
	 * of the SQL drudgery from what are really (in our case,
	 * anyway) static Value Objects in the Sun J2EE sense of the
	 * term, or Data Transfer Objects in the Fowler sense of the
	 * term.
	 *
	 * @param results the ResultSet instance for this row
	 * @exception SQLException
	 * 	if accessing the data fails for whatever reason
	 */

	void load(ResultSet results) throws SQLException;

	/**
	 * This method allows ResultRow instances to act as prototypes
	 * so that non-public classes can be used to retrieve
	 * DynamicFinder results.
	 *
	 * @return a new ResultRow instance
	 */

	ResultRow newInstance();
}
