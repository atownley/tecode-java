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
// File:	ObjectUtils.java
// Created:	Sep 21, 2002 9:15:47 PM
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.persistence;

/**
 * This class was taken from the 3.2-Beta release of Jaxor.  It was
 * incorporated into te-common on Thu Jan 29 18:11:51 GMT 2004 to
 * minimize the dependencies on Jaxor, but without completely
 * re-inventing the wheel.
 * <p>
 * The exposure of this class has been changed from public to
 * package-private.  It is not part of th public API for te-common.
 * </p>
 *
 * @author Michael Rettig
 */

class ObjectUtils {
    public static boolean equals(Object obj, Object obj2) {
        if (obj == null && obj2 == null)
            return true;
        if (obj == null)
            return false;
        if (obj2 == null)
            return false;
        return obj.equals(obj2);
    }
}
