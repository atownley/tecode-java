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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class provides an implementation for tracking overridable
 * nodes based on a pluggable override strategy. 
 *
 * @version $Id: OverrideManager.java,v 1.2 2004/12/26 19:43:00 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 */

public abstract class OverrideManager
{
	/**
	 * This method returns all of the keys managed by this
	 * instance.
	 *
	 * @return the keys as a set
	 */

	public Set getKeys()
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
		OverrideNode node = getNodeForReading(key);
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
		Set c = getKeys(object);

		for(Iterator i = c.iterator(); i.hasNext(); )
		{
			Object key = i.next();
			List olist = (List)_map.get(key);
			if(olist == null)
			{
				olist = new ArrayList();
				_map.put(key, olist);
			}
			olist.add(new OverrideNode(object));
		}
	}

	/**
	 * This method sets the value for the specified key.
	 *
	 * @param key the key to retrieve
	 * @param value the value for the key 
	 */

	public void put(Object key, Object value)
	{
		OverrideNode node = getNodeForWriting(key);
		setValue(key, node.get(), value);

		// now, we need to check to see that the set value
		// will be read next time.
		if(!node.equals(getNodeForReading(key)))
		{
			List list = (List)_map.get(key);
			list.add(node);
		}
	}

	/**
	 * This method is used to configure the override strategy used
	 * when reading values.
	 *
	 * @param strategy the strategy
	 */

	public void setReadResolver(OverrideStrategy strategy)
	{
		_readResolver = strategy;
	}

	/**
	 * This method is used to configure the override strategy used
	 * when writing values.
	 *
	 * @param strategy the strategy
	 */

	public void setWriteResolver(OverrideStrategy strategy)
	{
		_writeResolver = strategy;
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

	protected abstract Set getKeys(Object object);

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
	 * This method is used to set the value for the specific
	 * property key given the managed object.
	 *
	 * @param key the key to set
	 * @param object the object to manipulate
	 * @param value the value for the key
	 */

	protected abstract void setValue(Object key, 
					Object object, Object value);

	/**
	 * This method is used to retrieve the specific OverrideNode
	 * associated with the given key for reading the value.
	 *
	 * @param key the key
	 * @return the node
	 */

	protected OverrideNode getNodeForReading(Object key)
	{
		return _readResolver.resolve(key, (List)_map.get(key));	
	}

	/**
	 * This method is used to retrieve the specific OverrideNode
	 * associated with the given key for writing the value.
	 *
	 * @param key the key
	 * @return the node
	 */

	protected OverrideNode getNodeForWriting(Object key)
	{
		List list = (List)_map.get(key);
		OverrideNode node =  _writeResolver.resolve(key, list);
		if(list == null && node != null)
		{
			// key didn't exist before
			List list2 = new ArrayList();
			_map.put(key, list2);
			list2.add(node);
		}

		return node;
	}

	/** track the current read strategy */
	private OverrideStrategy	_readResolver = null;
	
	/** track the current write strategy */
	private OverrideStrategy	_writeResolver = null;

	/** the map of values */
	private HashMap			_map = new HashMap();
}
