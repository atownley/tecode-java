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
// File:	QueryListener.java
// Created:	Wed May 21 14:35:14 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.sql.SQLException;

/**
 * This interface should be implemented by classes that wish to be
 * notified of QueryEvents by a QueryHandler.
 *
 * @version $Id: QueryListener.java,v 1.1 2003/06/07 18:42:35 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public interface QueryListener
{
	/**
	 * This method is called by the QueryHandler after executing
	 * a query.
	 *
	 * @param event the QueryEvent containing the data
	 */

	void queryCompleted(QueryEvent event);

	/**
	 * This method is called by the QueryHandler before executing
	 * a query.
	 *
	 * @param event the QueryEvent containing the data
	 */

	void queryExecuted(QueryEvent event);

	/**
	 * This method is called by the QueryHandler for each row
	 * processed in the result set.
	 *
	 * @param event the QueryEvent containing the data
	 * @exception SQLException
	 * 	if something happens processing the result row
	 */

	void nextRow(QueryEvent event) throws SQLException;
}