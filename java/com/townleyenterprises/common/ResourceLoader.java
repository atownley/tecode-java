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
// File:	ResourceLoader.java
// Created:	Wed Nov 12 14:06:28 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;

/**
 * This class provides a way to access Java resource strings from a
 * location relative to a specific class.  This class assumes that all
 * resources are located in a sub-directory under the current package
 * called 'resources'.  Thus, in Unix, the resource strings for
 * <code>com.foo.MyClass</code> will be located in:
 * <code>com/foo/resources/strings.properties</code> and the images
 * will be located under <code>com/foo/resources/</code>.
 * <p>
 * This class supports a system property which will control how
 * missing resources are reported.  The property is:
 *
 * <ul>
 * <li>te-code.common.resourceloader.showmissingresources - set to
 * <code>true</code> or <code>yes</code> to display messages about
 * missing resources</li>
 * </ul>
 * </p>
 *
 * @version $Id: ResourceLoader.java,v 1.3 2004/08/11 16:19:01 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class ResourceLoader implements ResourceProvider
{
	/**
	 * The constructor takes an object to use as the basis for
	 * finding resources.
	 *
	 * @param obj the object to provide the base resource location
	 */

	public ResourceLoader(Object obj)
	{
		this(obj.getClass());
	}

	/**
	 * This constructor takes a class to use as the basis for
	 * finding resources.
	 *
	 * @param cls the class providing the base resource location
	 */

	public ResourceLoader(Class cls)
	{
		this(cls, "strings");
	}

	/**
	 * This constructor takes a class to use as the basis for
	 * finding resources for the specific locale.
	 *
	 * @param cls the class providing the base resource location
	 * @param locale the locale of the resources to load
	 */

	public ResourceLoader(Class cls, Locale locale)
	{
		this(cls, "strings", locale);
	}

	/**
	 * This version of the constructor is used to load resource
	 * settings from an arbitrarily named resource property file.
	 *
	 * @param cls the class providing the base resource location
	 * @param name the name of the resources
	 */

	public ResourceLoader(Class cls, String name)
	{
		this(cls, name, Locale.getDefault());
	}

	/**
	 * This version of the constructor is used to load resource
	 * settings from an arbitrarily named resource property file.
	 *
	 * @param cls the class providing the base resource location
	 * @param name the name of the resources
	 * @param locale the locale to use
	 */

	public ResourceLoader(Class cls, String name, Locale locale)
	{
		String p = null;

		// sanity parameter checks
		if(cls == null)
			p = "cls";
		else if(name == null)
			p = "name";
		else if(locale == null)
			p = "locale";

		if(p != null)
		{
			throw new NullPointerException(
				Strings.format("fNullParameter",
						new Object[] { p }));
		}

		_klass = cls;
		Package pkg = _klass.getPackage();
		if(pkg != null)
		{
			_name = pkg.getName().concat(".resources.").concat(name);
		}
		else
		{
			_name = "resources.".concat(name);
		}

		_locale = locale;
		_bundles.put(locale, 
			ResourceBundle.getBundle(_name, locale));
	}

	/**
	 * This method is used to find a given resource string.
	 *
	 * @param key the resource string key.  If the key can't be
	 * found or the resulting string is empty, the key is
	 * returned.
	 * @return the string for the key
	 */

	public String getString(String key)
	{
		return getBundleString(getResourceBundle(), key);
	}

	public String getString(String key, Locale locale)
	{
		String p = null;

		// sanity parameter checks
		if(locale == null)
			p = "locale";

		if(p != null)
		{
			throw new NullPointerException(
				Strings.format("fNullParameter",
						new Object[] { p }));
		}

		// see if we have a bundle for the locale
		ResourceBundle bundle = getResourceBundle(locale);
		if(bundle == null)
		{
			// try to load it
			bundle = ResourceBundle.getBundle(_name, locale);
			_bundles.put(locale, bundle);
		}

		return getBundleString(bundle, key);
	}

	/**
	 * This method is used to load an image for the default
	 * locale.
	 *
	 * @param key the image key
	 * @return the ImageIcon
	 */

	public ImageIcon getIcon(String key)
	{
		return getIcon(key, _locale);
	}

	/**
	 * This method is used to load an image for the specified
	 * locale and key.
	 *
	 * @param key the image key
	 * @param locale the locale
	 * @return the ImageIcon
	 */

	public ImageIcon getIcon(String key, Locale locale)
	{
		String p = null;

		// sanity parameter checks
		if(key == null)
			p = "key";
		else if(locale == null)
			p = "locale";

		if(p != null)
		{
			throw new NullPointerException(
				Strings.format("fNullParameter",
						new Object[] { p }));
		}

		// need to see if there's a locale-specific version of
		// the icon.

		String ext = Path.getExtension(key);
		String name = "resources/".concat(Path.stripExtension(key));
		String[] loc = ResourceManager.getResourceNames(name, locale);

//System.out.println("loc.length:  " + loc.length);

		URL url = null;
		// find one that works
		for(int i = 0; i < loc.length; ++i)
		{
//System.out.println("loc[" + i + "]:  " + loc[i].concat(".").concat(ext));
			url = _klass.getResource(loc[i].concat(".").concat(ext));
//System.out.println("trying url:  " + url);
			if(url != null)
				break;
		}

		return new ImageIcon(url);
	}

	public Collection getKeys()
	{
		ResourceBundle bndl = getResourceBundle();
		ArrayList list = new ArrayList();
		Enumeration e = bndl.getKeys();
		while(e.hasMoreElements())
		{
			list.add(e.nextElement());
		}

		return list;
	}

	/**
	 * This method returns the reference to the bundle for the
	 * default locale for this instance.
	 *
	 * @return the bundle
	 */

	public ResourceBundle getResourceBundle()
	{
		return getResourceBundle(_locale);
	}

	/**
	 * This method is used to get a direct reference to the resource
	 * bundle.
	 *
	 * @param locale the specific locale
	 * @return the resource bundle
	 */

	public ResourceBundle getResourceBundle(Locale locale)
	{
		String p = null;

		// sanity parameter checks
		if(locale == null)
			p = "locale";

		if(p != null)
		{
			throw new NullPointerException(
				Strings.format("fNullParameter",
						new Object[] { p }));
		}

		return (ResourceBundle)_bundles.get(locale);
	}

	/**
	 * Provide some clue as to what we're doing and who we are.
	 */

	public String toString()
	{
		StringBuffer buf = new StringBuffer("[ResourceLoader (");
		buf.append(hashCode());
		buf.append("): klass='");
		buf.append(_klass);
		buf.append("'; name='");
		buf.append(_name);
		buf.append("'; locale='");
		buf.append(_locale);
		buf.append("' ]");

		return buf.toString();
	}

	/**
	 * This is a helper method since we now need to work with more
	 * than one bundle.
	 */

	private String getBundleString(ResourceBundle rez, String key)
	{
		String s = null;
		String p = null;

		// sanity parameter checks
		if(rez == null)
			p = "rez";
		else if(key == null)
			p = "key";

		if(p != null)
		{
			throw new NullPointerException(
				Strings.format("fNullParameter",
						new Object[] { p }));
		}

		try
		{
			s = rez.getString(key);
		}
		catch(MissingResourceException e)
		{
			String x = System.getProperty("te-code.common.resourceloader.showmissingresources");
			if(p != null && (x.toLowerCase().charAt(0) == 'y'
					|| x.toLowerCase().charAt(0) == 't'))
			{
				System.err.println(Strings.format("fMissingResource", new Object[] { getClass().getName(), e.getKey(), _name }));
//				e.printStackTrace();
			}
			s = key;
		}

		return s;
	}

	/** a reference to the class */
	private Class			_klass;

	/** save a reference to our resource bundle name */
	private String			_name;

	/** save a reference to our specified default locale */
	private Locale			_locale;

	/** the resource bundles for the other locales */
	private HashMap			_bundles = new HashMap();
}
