//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2005, Andrew S. Townley
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
// File:	LocalizedCommandOption.java
// Created:	Sun Oct  2 00:16:18 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import com.townleyenterprises.common.InjectResourceManager;
import com.townleyenterprises.common.ResourceManager;

/**
 * This class provides support for a key-based approach to
 * returning CommandOption argument help and descriptions.
 *
 * @version $Id: LocalizedCommandOption.java,v 1.1 2005/10/02 00:02:29 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class LocalizedCommandOption extends CommandOption
		implements InjectResourceManager
{
	/**
	 * The class is fully initialized by the constructor and each
	 * argument is immutable once it has been set.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param hasArg true if this option expects an argument;
	 * 	false if it is a switch
	 * @param resKey the key on which the resource lookups
	 * 	will be based for this option
	 */

	public LocalizedCommandOption(String longName, char shortName,
				boolean hasArg, String resKey)
	{
		this(longName, shortName, hasArg, resKey, true, null);
	}

	/**
	 * This version of the constructor allows specifying if the
	 * argument is to be shown to the user and if the argument has
	 * a default value if it is not specified on the command line.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param hasArg true if this option expects an argument;
	 * 	false if it is a switch
	 * @param resKey the key on which the resource lookups
	 * 	will be based for this option
	 * @param show true if should be shown in autohelp
	 * @param def the default value of the argument if it is not
	 * 	specified.
	 */

	public LocalizedCommandOption(String longName, char shortName,
				boolean hasArg, String resKey,
				boolean show, String def)
	{
		super(longName, shortName, hasArg, null,
			null, show, def);
		_help = resKey + "Help";
		_desc = resKey + "Description";
	}

	/**
	 * This version of the constructor allows specifying if the
	 * argument has a default value.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param hasArg true if this option expects an argument;
	 * 	false if it is a switch
	 * @param resKey the key on which the resource lookups
	 * 	will be based for this option
	 * @param def the default value of the argument if it is not
	 *	specified.
	 */

	public LocalizedCommandOption(String longName, char shortName,
				boolean hasArg, String resKey,
				String def)
	{
		this(longName, shortName, hasArg, resKey, true, def);
	}

	public String getHelp()
	{
		if(_resources != null)
		{
			String s = _resources.getString(_help);
			return (s == _help) ? null : s;
		}

		return _help;
	}

	public String getDescription()
	{
		if(_resources != null)
		{
			String s = _resources.getString(_help);
			return (s == _desc) ? null : s;
		}

		return _desc;
	}

	/**
	 * This method implements the InjectResourceManager
	 * interface.
	 *
	 * @param resources the resource manager
	 */

	public void injectResourceManager(ResourceManager resources)
	{
		_resources = resources;
	}

	/**
	 * This method allows derived classes to access the
	 * resource manager instance.
	 *
	 * @return the ResourceManager instance
	 */

	protected ResourceManager getResources()
	{
		return _resources;
	}

	private final String	_desc;
	private final String	_help;
	private ResourceManager	_resources;
}
