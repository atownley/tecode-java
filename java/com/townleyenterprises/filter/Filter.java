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
// Title:	Filter.java
// Created: 	Wed May  7 17:57:59 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

/**
 * This interface provides the base operation common to all filters
 * within the system.
 *
 * @version $Id: Filter.java,v 1.4 2004/12/04 17:28:16 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public interface Filter
{
	/**
	 * This method actually performs the operation that will
	 * determine if the parameter object should be included in the
	 * "result" or not.
	 *
	 * @param o the object to be tested
	 * @return true if the object should be included in the
	 * 	result; false if the object should not
	 * @deprecated As of the 3.0 release, the {@link
	 * #execute} method should be used to more accurately
	 * reflect the relationship to the GoF Command
	 * pattern.  This method will be removed in a future
	 * version of the library.
	 */

	public boolean doFilter(Object o);

	/**
	 * This method actually performs the operation that will
	 * determine if the parameter object should be included in the
	 * "result" or not.
	 *
	 * @param o the object to be tested
	 * @return true if the object should be included in the
	 * 	result; false if the object should not
	 */

	public boolean execute(Object o);
}
