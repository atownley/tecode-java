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
// File:	AbstractUIBuilder.java
// Created:	Thu Nov 20 02:39:26 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import java.util.Map;
import java.util.HashMap;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This class provides a convenient base class for concrete
 * implementations of the UIBuilder interface.
 *
 * @since 2.1
 * @version $Id: AbstractUIBuilder.java,v 1.1 2003/11/20 10:47:52 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public abstract class AbstractUIBuilder implements UIBuilder
{
	/**
	 * The constructor takes a map of Action instances which are
	 * to be associated with the menu items.  The keys of the map
	 * should match the keys used to resolve the menu items.
	 *
	 * @param actions the action map
	 */

	public AbstractUIBuilder(Map actions)
	{
		_actions = actions;
	}

	/**
	 * This method will build a completely initialized menu bar
	 * from the input source.
	 *
	 * @return a JMenuBar instance
	 */

	public abstract JMenuBar buildMenuBar();

	/**
	 * This method builds a given menu based on the appropriate
	 * key value.  For a given key, the following items should be
	 * defined:
	 * <ul>
	 * <li><em>key</em> -- a representation of the menu</li>
	 * <li><em>key</em>Label -- the actual label text</li>
	 * <li><em>key</em>Mnemonic -- the single key mnemonic for the
	 * menu</li>
	 * </ul>
	 *
	 * @param key the key for the menu
	 * @return a JMenu instance
	 */

	public abstract JMenu buildMenu(String key);

	/**
	 * @param key the key for the menu item
	 * @return a JMenuItem instance
	 */

	public final JMenuItem buildMenuItem(String key)
	{
		JMenuItem mi = buildMenuItemHelper(key);
		_menuItems.put(key, mi);

		return mi;
	}

	/**
	 * This method is used to retrieve the action for the
	 * specified key in the actions specified in the constructor.
	 *
	 * @param key the key of the action
	 * @return the Action or null if none exists
	 */

	public Action getAction(String key)
	{
		return (Action)_actions.get(key);
	}

	/**
	 * This method is used to retrieve a menu item instance
	 * created by the abstract builder.
	 *
	 * @param key the key of the menu item to retrieve
	 * @return a JMenuItem instance.  If no instance was created,
	 * 	this method returns null.
	 */

	public JMenuItem getMenuItem(String key)
	{
		return (JMenuItem)_menuItems.get(key);
	}

	/**
	 * This method must be implemented by derived classes.  It is
	 * functionally equivalent to the {@link #buildMenuItem}
	 * method, but it is used to ensure that the menu items get
	 * registered.
	 *
	 * @param key the menu item key
	 * @return a JMenuItem
	 */

	protected abstract JMenuItem buildMenuItemHelper(String key);

	/** keep the actions for the menu items */
	private Map	_actions;

	/** keep track of the menu items we create */
	private Map	_menuItems = new HashMap();
}
