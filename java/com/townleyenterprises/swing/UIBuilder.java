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
// File:	UIBuilder.java
// Created:	Thu Nov 20 02:21:14 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This interface is part of an implementation of the Builder pattern
 * which provides ways to dynamically build portions of a Swing
 * application's User Interface.  This mechanism was originally inspired
 * by the 1999 verion of the Swing Notepad demo (version 1.15) by
 * Timothy Prinzing, but has come a long way from the original
 * adaptation.  This implementation makes the technique into a
 * full-blow implementation of the Builder pattern which is extensible
 * to different types of physical UI layout representations.
 * <p>
 * This interface provides the basic part builder methods, but it
 * doesn't define an equivalent getResult() method.  The Director is
 * expected to do what it needs with each of the individual pieces.
 * </p>
 *
 * @since 2.1
 * @version $Id: UIBuilder.java,v 1.3 2004/01/25 19:26:45 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public interface UIBuilder
{
	/**
	 * This method will build a completely initialized menu bar
	 * from the input source.
	 *
	 * @return a JMenuBar instance
	 */

	JMenuBar buildMenuBar();

	/**
	 * This method will build a completely initialized menu bar from
	 * the input source with the specified key.  This method is mainly
	 * used when more than one menubar needs to be built from the saem
	 * resource loader.
	 *
	 * @param key the resource key for the menubar
	 * @return a JMenuBar instance
	 */

	JMenuBar buildMenuBar(String key);

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

	JMenu buildMenu(String key);

	/**
	 * This method builds a menu item based on the given key.  For
	 * a given key, the following items should be defined:
	 * <ul>
	 * <li><em>key</em>Label -- the text for the menu item label</li>
	 * <li><em>key</em>StatusText -- the status text displayed
	 * when the menu item is selected</li>
	 * <li><em>key</em>Mnemonic -- the single key mnemonic for the
	 * menu item</li>
	 *
	 * @param key the key for the menu item
	 * @return a JMenuItem instance
	 */

	JMenuItem buildMenuItem(String key);
}
