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
// File:	ResourceManager.java
// Created:	Thu Jul 29 11:14:58 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 * This class provides a way of managing resource hierarchies
 * independent of the locale of a resource bundle.  The default
 * resolution strategy is an instance of {@link
 * com.townleyenterprises.common.UseLastOverrideStrategy} which should
 * "do the right thing" in almost all of the cases.
 *
 * @version $Id: ResourceManager.java,v 1.5 2004/12/28 21:47:07 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class ResourceManager extends OverrideManager
		implements ResourceProvider
{
	/**
	 * This method is used to return the possible variants of a
	 * given base resource name for the given locale.
	 *
	 * @param base the base name
	 * @param locale the locale
	 * @return an array of possible locations for the resource, in
	 * order of most specialized to least specialized.
	 */

	public static String[] getResourceNames(String base, Locale locale)
	{
		ArrayList list = new ArrayList();
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		list.add(base.concat("_").concat(locale.toString()));
		if(!"".equals(variant))
		{
			list.add(base.concat("_").concat(language.concat("_").concat(country)));
		}
		
		if(!"".equals(country))
		{
			list.add(base.concat("_").concat(language));
		}

		// always add the default
		list.add(base);

		return (String[])list.toArray(new String[list.size()]);
	}

	/**
	 * This method sets up the default resolution strategy.
	 */

	public ResourceManager()
	{
		setReadResolver(new UseLastOverrideStrategy());
	}

	/**
	 * This method will return the value for the key or the key if
	 * the value does not exist.
	 */

	public String getString(String key)
	{
		Object obj = get(key);
		if(obj == null)
			return key;

		return (String)obj;
	}

	public String getString(String key, Locale locale)
	{
		OverrideNode on = getNodeForReading(key);
		if(on == null)
			return null;

		ResourceProvider rp = (ResourceProvider)on.get();

		if(rp == null)
			return key;

		return rp.getString(key, locale);
	}

	public ImageIcon getIcon(String key)
	{
		return getIcon(key, Locale.getDefault());
	}

	public ImageIcon getIcon(String key, Locale locale)
	{
		return findIcon(key, locale);
	}

	/**
	 * This method keeps track of the objects managed so that the
	 * icons can be resolved in the right order.
	 */

	public void manage(Object object)
	{
		super.manage(object);

		// FIXME:  this is a hack because we need to track
		// things in reverse order and we don't have the
		// option of a reverse iterator... hmmm....
		_iconList.add(0, object);
	}

	/**
	 * This method is used to add a resource provider.  It simply
	 * deferrs to the {@link #manage} method, but it provides some
	 * type checking.
	 *
	 * @param provider the ResourceProvider
	 */

	public void addProvider(ResourceProvider provider)
	{
		manage(provider);
	}

	/**
	 * This method is used to format a given message located in
	 * the resource bundle.  The message is first retrieved and
	 * then processed by an instance of the MessageFormat class.
	 *
	 * @param key the message key
	 * @param locale the locale
	 * @param args the format arguments
	 */

	public String format(String key, Locale locale, Object[] args)
	{
		String fmt = getString(key, locale);
		MessageFormat format = new MessageFormat(fmt, locale);
		return format.format(args, 
				new StringBuffer(), null).toString();
	}

	/**
	 * This method is used to format a given message located in
	 * the resource bundle.  The message is first retrieved and
	 * then processed by an instance of the MessageFormat class.
	 *
	 * @param key the message key
	 * @param args the format arguments
	 */

	public String format(String key, Object[] args)
	{
		return format(key, Locale.getDefault(), args);
	}

	/**
	 * This method is used to allow strings to be used to
	 * obtain locales.  It probably isn't complete, but it
	 * will handle the basics when storing locale
	 * preferences as text strings
	 *
	 * @param s the locale string
	 * @return a valid locale or null if no locale can be
	 * 	determined
	 */

	public static Locale getLocale(String s)
	{
		Locale locale = null;

		if(s.indexOf("_") != -1)
		{
			String lang = s.substring(0, 2);
			String country = s.substring(3, 5);
	
			int idx = s.indexOf(".");
			if(idx != -1)
			{
				locale = new Locale(lang, country,
						s.substring(idx+1));
			}
			else
			{
				locale = new Locale(lang, country);
			}
		}
		else if("C".equals(s) || "POSIX".equals(s))
		{
			locale = Locale.US;
		}
		else
		{
			locale = new Locale(s, "");
		}

		return locale;
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

	protected Set getKeys(Object object)
	{
		if(object instanceof ResourceProvider)
		{
			return ((ResourceProvider)object).getKeys();
		}

		return Collections.EMPTY_SET;
	}

	/**
	 * This method is used to retrieve the value for the specific
	 * property key given the managed object.
	 *
	 * @param key the key to retrieve
	 * @param object the object to manipulate
	 * @return the value for the key
	 */

	protected Object getValue(Object key, Object object)
	{
		if(object instanceof ResourceProvider)
		{
			return ((ResourceProvider)object).getString(key.toString());
		}

		return null;
	}

	// this method currently has no meaning in this context
	
	protected void setValue(Object key, Object object, Object val)
	{
	}

	/**
	 * This method is used to walk the entire set of providers to
	 * attempt to find the icon resource.
	 *
	 * FIXME:  this isn't terribly efficient, but...
	 */

	private ImageIcon findIcon(String key, Locale locale)
	{
		ImageIcon icon = null;

		for(Iterator i = _iconList.iterator(); i.hasNext();)
		{
			ResourceProvider rp = (ResourceProvider)i.next();
			if(rp == null)
				return null;

			icon = rp.getIcon(key, locale);
			if(icon != null)
				break;
		}

		return icon;
	}

	/** maintain a node to track the correct resolution order */
	private List	_iconList = new ArrayList();
}
