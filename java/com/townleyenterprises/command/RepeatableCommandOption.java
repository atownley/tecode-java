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
// File:	RepeatableCommandOption.java
// Created:	Sun Jun  8 13:37:39 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import java.util.List;
import java.util.ArrayList;

/**
 * This class provides the basic support for repeatable command line
 * arguments.  This class can be used to allow a given argument to be
 * supplied more than once on the command line.
 *
 * @version $Id: RepeatableCommandOption.java,v 1.1 2003/06/08 13:35:51 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public class RepeatableCommandOption extends CommandOption
{
	/**
	 * The constructor takes almost all of the parent class's
	 * arguments, but the assumption is that if it was a regular
	 * command option, it wouldn't be necessary to recognize it
	 * more than once.  Therefore, this class assumes that there
	 * will be an argument each time it is matched by the command
	 * parser.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 */

	public RepeatableCommandOption(String longName, char shortName,
				String argHelp, String argDesc)
	{
		super(longName, shortName, true, argHelp,
				argDesc, true, null);
	}

	/**
	 * This version of the constructor allows specifying if the
	 * argument is to be shown to the user and if the argument has
	 * a default value if it is not specified on the command line.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 * @param show true if should be shown in autohelp
	 * @param def the default value (informational only) of
	 * 	the argument if it is not specified.
	 */

	public RepeatableCommandOption(String longName, char shortName,
				String argHelp, String argDesc,
				boolean show, String def)
	{
		super(longName, shortName, true, argHelp,
				argDesc, show, def);
	}

	/**
	 * This method will return the last argument matched by this
	 * option rather than the collection of all the arguments.
	 *
	 * @return the argument or null if no argument was specified
	 */

	public String getArg()
	{
		return super.getArg();
	}

	/**
	 * This method calls the parent class's version to ensure that
	 * the option behaves consistently with the rest of the
	 * system, but it collects each of the arguments so that they
	 * may be retrieved via the {@link getArgs} method.
	 *
	 * @param arg the argument (if expected)
	 */

	public void optionMatched(String arg)
	{
		super.optionMatched(arg);
		addArg(arg);
	}

	/**
	 * This method returns the arguments which have been matched
	 * by this instance.
	 *
	 * @return a copy of the matched arguments
	 */

	public List getArgs()
	{
		return _list.subList(0, _list.size());
	}

	/**
	 * This method is provided so that any derived classes can
	 * append arguments to the list.
	 *
	 * @param arg the argument to be added
	 */

	protected void addArg(String arg)
	{
		_list.add(arg);
	}

	private ArrayList	_list = new ArrayList();
}
