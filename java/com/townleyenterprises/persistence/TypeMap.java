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
// File:	TypeMap.java
// Created:	Sept 21, 2002 2:28:00 PM
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * This class was taken from the 3.2-Beta release of Jaxor.  It was
 * incorporated into te-common on Thu Jan 29 18:24:10 GMT 2004 to
 * minimize the dependencies on Jaxor, but without completely
 * re-inventing the wheel.
 * <p>
 * The exposure of this class has been changed from public to
 * package-private.  It is not part of th public API for te-common.
 * </p>
 *
 * @author Michael Rettig
 */

class TypeMap {
    private final Map _map = new HashMap();
    public static TypeMap INSTANCE = new TypeMap();

    private TypeMap() {
        put(Boolean.class, Types.BIT);
        put("Boolean", Types.BIT);
        put(Byte.class, Types.TINYINT);
        put("Byte", Types.TINYINT);
        put(Character.class, Types.VARCHAR);
        put("Character", Types.VARCHAR);
        put(Short.class, Types.SMALLINT);
        put("Short", Types.SMALLINT);
        put(Integer.class, Types.INTEGER);
        put("Integer", Types.INTEGER);
        put(Long.class, Types.BIGINT);
        put("Long", Types.BIGINT);
        put(Float.class, Types.REAL);
        put("Float", Types.REAL);
        put(Double.class, Types.DOUBLE);
        put("Double", Types.DOUBLE);
        put(String.class, Types.VARCHAR);
        put("String", Types.VARCHAR);
        put(java.sql.Date.class, Types.DATE);
        put(java.sql.Time.class, Types.TIME);
        put(java.sql.Timestamp.class, Types.TIMESTAMP);
        put(java.math.BigDecimal.class, Types.NUMERIC);
    }

    private void put(Class clzz, int result) {
        put(clzz.getName(), result);
    }

    private void put(String name, int result) {
        _map.put(name, new Integer(result));
    }

    public int get(Object obj) {
        return getByClass(obj.getClass());
    }
    
    public int getByClass(Class cl){
    	return getAsInt(cl.getName()).intValue();
    }

    public String get(String type) {
        return getAsInt(type).toString();
    }

    private Integer getAsInt(String type) {
        Integer i = (Integer) _map.get(type);
        if (i == null)
            throw new RuntimeException("Unknown type: " + type);
        return i;
    }
}
