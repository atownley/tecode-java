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
// File:	ResourceUIBuilder.java
// Created:	Thu Nov 20 02:55:04 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.Action;
import javax.swing.event.ChangeListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This class provides a concrete implementation of the UIBuilder
 * interface which reads the UI descriptions from a resource bundle.
 * This class is heavily based on the mechanism used in the Java
 * JFC/Swing Notepad demo.  This class uses the same approach (and
 * even methods), but abstracts this functionality from a particular
 * application.
 *
 * @since 2.1
 * @version $Id: ResourceUIBuilder.java,v 1.2 2003/11/20 16:41:07 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class ResourceUIBuilder extends AbstractUIBuilder
{
	/**
	 * this is the property name used to look up the main menu
	 * bar description
	 */

	public static final String	MAINMENU = "menubar";

	/** this is the suffix used when looking up various labels */
	public static final String	LABEL_SUFFIX = "Label";

	/** this is the suffix used when looking up mnemonics */
	public static final String	MNEMONIC_SUFFIX = "Mnemonic";

	/** this is the suffix used when looking up status text */
	public static final String	STATUS_SUFFIX = "StatusText";

	/** this is the client property used to hold the status text */
	public static final String	STATUSTEXT_PROPERTY = STATUS_SUFFIX;
	/**
	 * The constructor takes the initialized ResourceLoader
	 * used to read the UI definition.  Since the description of the
	 * menu structure isn't localized, it allows language-specific
	 * strings to be resolved by the normal facilities provided by
	 * the Java ResourceBundle class.
	 *
	 * @param loader the resource loader
	 * @param actions the action map
	 */

	public ResourceUIBuilder(ResourceProvider loader, Map actions)
	{
		this(loader, actions, null);
	}

	/**
	 * This version of the constructor takes a ChangeListener
	 * which will be notified whenver the menu item or menu
	 * changes state.  This is most often used to implement status
	 * bar help text.
	 *
	 * @param loader the resource loader
	 * @param actions the action map
	 * @param menuStatusListener the change listener
	 */

	public ResourceUIBuilder(ResourceProvider loader, Map actions,
				ChangeListener menuStatusListener)
	{
		super(actions);
		_loader = loader;
		_statusListener = menuStatusListener;
	}

	/**
	 * Menubars are described in terms of a whitespace delimited
	 * string representing the top-level menus.  This string is
	 * tokenized and each token is used to look up and build the
	 * given menu.
	 * <p>
	 * The key used for the menu bar is <code>menubar</code>.
	 * </p>
	 *
	 * @return a JMenuBar instance
	 */

	public JMenuBar buildMenuBar()
	{
		JMenuBar menubar = new JMenuBar();
		String[] keys = tokenize(_loader.getString(MAINMENU));
		for(int i = 0; i < keys.length; ++i)
		{
			JMenu menu = buildMenu(keys[i]);
			if(menu != null)
				menubar.add(menu);
		}

		return menubar;
	}

	/**
	 * This method builds a menu based on the specified key.  To
	 * add separators to the menu, simply insert <code>-</code>
	 * characters into the menu definition.
	 */

	public JMenu buildMenu(String key)
	{
		String skey;
		String s;

		// read the menu
		JMenu menu = new JMenu(_loader.getString(key + LABEL_SUFFIX));
		skey = key + MNEMONIC_SUFFIX;
		s = _loader.getString(skey);
		if(!skey.equals(s))
			menu.setMnemonic(s.charAt(0));

		// now, process the menu items
		String[] keys = tokenize(_loader.getString(key));
		for(int i = 0; i < keys.length; ++i)
		{
			if("-".equals(keys[i]))
				menu.addSeparator();
			else
			{
				JMenuItem item = buildMenuItem(keys[i]);
				menu.add(item);
			}
		}

		return menu;
	}

	/**
	 * This method builds an individual menu item and associates
	 * an action provided in the constructor.  If no action is
	 * present, the menu item is disabled.
	 * <p>
	 * The original version of this method from the Notepad
	 * example deals with menu item images.  Currently, images
	 * aren't supported by this class.  Support will be added to
	 * the ResourceLoader shortly, so this class will be able to
	 * use it "free" once that happens.
	 * </p>
	 *
	 * @param key the menu item key
	 * @return a JMenuItem
	 */

	protected JMenuItem buildMenuItemHelper(String key)
	{
		String skey;
		String s;

		JMenuItem item = new JMenuItem(_loader.getString(key +
				LABEL_SUFFIX));

		// set up the status text handling
		skey = key + STATUS_SUFFIX;
		s = _loader.getString(skey);
		if(!skey.equals(s) && _statusListener != null)
		{
			item.putClientProperty(STATUSTEXT_PROPERTY, s);
			item.addChangeListener(_statusListener);
		}

		// set the mnemonic
		skey = key + MNEMONIC_SUFFIX;
		s = _loader.getString(skey);
		if(!skey.equals(s))
			item.setMnemonic(s.charAt(0));

		// set the menu action if we can
		Action action = getAction(key);
		if(action != null)
			item.addActionListener(action);
		else
			item.setEnabled(false);

		return item;
	}

	/**
	 * This is a utility method which is used to return an array
	 * of strings parsed from the source.  It is assumed that the
	 * strings are separated by whitespace.
	 *
	 * @param source the string to parse
	 * @return the array of parsed strings
	 */

	protected String[] tokenize(String source)
	{
		List list = new ArrayList();
		StringTokenizer st = new StringTokenizer(source);

		while(st.hasMoreTokens())
			list.add(st.nextToken());

		return (String[])list.toArray(new String[list.size()]);
	}
	
	/** keep a reference to the resource loader */
	private ResourceProvider	_loader;

	/** keep a reference to the status listener */
	private ChangeListener		_statusListener;
}
