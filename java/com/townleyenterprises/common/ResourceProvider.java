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
// File:	ResourceProvider.java
// Created:	Thu Nov 20 12:35:59 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.Locale;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 * This interface defines methods for retrieving resources from
 * various locations.  This interface is intended to be expanded with
 * a variety of other resource types.
 *
 * @version $Id: ResourceProvider.java,v 1.2 2004/12/26 19:44:46 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public interface ResourceProvider
{
	/**
	 * This method returns the collection of keys for the resource
	 * provider.
	 */

	Set getKeys();

	/**
	 * This method is used to find a given resource string based
	 * on the default locale.
	 *
	 * @param key the resource string key
	 * @return the string for the key
	 */

	String getString(String key);

	/**
	 * This method is used to retrieve the specified image icon
	 * for the default locale.
	 *
	 * @param key the key (usually the name) of the icon
	 * @return the appropriate ImageIcon
	 */

	ImageIcon getIcon(String key);
	
	/**
	 * This method is used to find a given resource string based
	 * on the specified locale.
	 *
	 * @param key the resource string key
	 * @param locale the locale
	 * @return the string for the key
	 */

	String getString(String key, Locale locale);

	/**
	 * This method is used to retrieve the specified image icon
	 * for the specified locale.
	 *
	 * @param key the key (usually the name) of the icon
	 * @param locale the locale
	 * @return the appropriate ImageIcon
	 */

	ImageIcon getIcon(String key, Locale locale);
}
