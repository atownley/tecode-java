//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2002-2004, Michael Rettig
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
// File:	QueryParams.java
// Created:	c. 2003/04/28 20:50:58
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class was taken from the 3.2-Beta release of Jaxor.  It was
 * incorporated into te-common on Thu Jan 29 18:16:31 GMT 2004 to
 * minimize the dependencies on Jaxor, but without completely
 * re-inventing the wheel.
 * <p>
 * The exposure of this class has been changed from public to
 * package-private.  It is not part of th public API for te-common.
 * </p>
 *
 * @author Michael Rettig
 */

class QueryParams{
	
	private List _params  = new ArrayList();
	
	public void add(Class clz, Object value){
		add(TypeMap.INSTANCE.getByClass(clz), value);
	}
	
	public void add(int sqlType, Object value){
		_params.add(new Param(sqlType, value));
	}
	
	public int hashCode(){
		return _params.hashCode();
	}
	
	public boolean equals(Object obj){
		if(obj instanceof QueryParams){
			QueryParams query = (QueryParams)obj;
			return _params.equals(query._params);
		}
		return false;
	}

	private static class Param{	
		private final int _sqlType;
		private final Object _value;
		public Param(int sqlType, Object value){
			_sqlType = sqlType;
			_value = value;	
		}
		
		public void setValue(PreparedStatement stmt, int i) throws SQLException{
			if (_value == null)
					stmt.setNull(i, _sqlType);
			else
					stmt.setObject(i,_value,_sqlType);
		}
		
		public boolean equals(Object obj){
			if(obj instanceof Param){
				Param param = (Param)obj;
				return ((_sqlType == param._sqlType) && 
					ObjectUtils.equals(_value, param._value));
			}
			return false;
		}
		
		public int hashCode(){
			if(_value == null)
				return _sqlType;
			return _value.hashCode();
		}
	}

	public void setArgs(PreparedStatement stmt) throws SQLException {
		for (int i = 0; i < _params.size(); i++) {
			Param p = (Param) _params.get(i);
			p.setValue(stmt, i + 1);
		}
	}

	public void add(Object long1) {
		if(long1 == null)
			throw new NullPointerException("cannot add null query parameters.");
		add(long1.getClass(), long1);
	}
}
