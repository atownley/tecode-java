//////////////////////////////////////////////////////////////////////
//
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
// File:	OverrideNode.java
// Created:	Thu Jul 29 09:45:22 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

/**
 * This class maintains a pointer to the source object in an override
 * list.  The list maintains the order of the objects being overridden
 * which means that this class is really nothing more than a pointer
 * to an object.
 *
 * @version $Id: OverrideNode.java,v 1.2 2004/12/26 19:41:47 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public class OverrideNode
{
	/**
	 * The default constructor creates an empty node.
	 */

	public OverrideNode()
	{
	}

	/**
	 * The constructor creates a node to manage the specific
	 * object.
	 *
	 * @param object the object to manage
	 */

	public OverrideNode(Object object)
	{
		set(object);
	}

	/**
	 * This method is used to retrieve the object managed by this
	 * node.
	 *
	 * @return the object
	 */

	public Object get()
	{
		return _object;
	}

	/**
	 * This method is used to set the object reference tracked by
	 * this node.
	 *
	 * @param obj the object
	 */

	public void set(Object obj)
	{
		_object = obj;
	}

	/** the current object */
	private Object		_object = null;
}
