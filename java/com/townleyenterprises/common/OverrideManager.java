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
// File:	OverrideManager.java
// Created:	Thu Jul 29 10:01:49 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class provides an implementation for tracking overridable
 * nodes based on a pluggable override strategy. 
 *
 * @version $Id: OverrideManager.java,v 1.1 2004/07/29 18:29:20 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public abstract class OverrideManager
{
	/**
	 * This method returns all of the keys managed by this
	 * instance.
	 *
	 * @return the keys as a collection
	 */

	public Collection getKeys()
	{
		return _map.keySet();
	}

	/**
	 * This method retrieves the value for the specified key.
	 *
	 * @param key the key to retrieve
	 * @return the value for the key or null if the key doesn't
	 * exist
	 */

	public Object get(Object key)
	{
		OverrideNode node = getNode(key);
		if(node == null)
			return null;

		return getValue(key, node.get());
	}

	/**
	 * This method is used to add the specified object to the
	 * managed objects of this instance.  Concrete instances
	 * of this class must be able to provide an implementation of
	 * the {#getKeys} method which can enumerate the keys of the
	 * specified object.
	 *
	 * @param object the object to manage
	 */

	public void manage(Object object)
	{
		Collection c = getKeys(object);

		for(Iterator i = c.iterator(); i.hasNext(); )
		{
			Object key = i.next();
//System.out.println("adding key:  " + key);
			OverrideNode node = getNode(key);
			if(node == null)
			{
				node = new OverrideNode();
				_map.put(key, node);
			}
			node.set(resolve(key, node.get(), object));
		}
	}

	/**
	 * This method is used to configure the override strategy used
	 * when adding new objects.
	 *
	 * @param strategy the strategy
	 */

	public void setOverrideStrategy(OverrideStrategy strategy)
	{
		_strategy = strategy;
	}

	/**
	 * Provide an implementation of the strategy interface based
	 * on the configured strategy.
	 *
	 * @param key the key
	 * @param s1 the first supplier
	 * @param s2 the second supplier
	 * @return the object providing the property for the given key
	 */

	protected final Object resolve(Object key, Object s1, Object s2)
	{
		if(_strategy == null)
			return null;

		return _strategy.resolve(key, s1, s2);
	}

	/**
	 * This method is used to retrieve the property keys from the
	 * specified object.  The current assumption is that all
	 * objects managed by this class will at least conform to a
	 * common interface.
	 *
	 * @param object the object to manage
	 * @return a collection of key values
	 */

	protected abstract Collection getKeys(Object object);

	/**
	 * This method is used to retrieve the value for the specific
	 * property key given the managed object.
	 *
	 * @param key the key to retrieve
	 * @param object the object to manipulate
	 * @return the value for the key
	 */

	protected abstract Object getValue(Object key, Object object);

	/**
	 * This method is used to retrieve the specific OverrideNode
	 * associated with the given key.
	 *
	 * @param key the key
	 * @return the node
	 */

	protected OverrideNode getNode(Object key)
	{
		return (OverrideNode)_map.get(key);
	}
	
	/** track the current strategy */
	private OverrideStrategy	_strategy = null;

	/** the map of values */
	private HashMap			_map = new HashMap();
}
