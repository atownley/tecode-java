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
// File:	CommandOption.java
// Created:	Sun May 11 20:06:56 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class provides support for defining command-line arguments.
 *
 * @version $Id: CommandOption.java,v 1.3 2004/01/26 09:18:42 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public class CommandOption
{
	/**
	 * The class is fully initialized by the constructor and each
	 * argument is immutable once it has been set.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param hasArg true if this option expects an argument;
	 * 	false if it is a switch
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 */

	public CommandOption(String longName, char shortName,
				boolean hasArg, String argHelp,
				String argDesc)
	{
		this(longName, shortName, hasArg, argHelp,
				argDesc, true, null);
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
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 * @param show true if should be shown in autohelp
	 * @param def the default value (informational only) of
	 * 	the argument if it is not specified.
	 */

	public CommandOption(String longName, char shortName,
				boolean hasArg, String argHelp,
				String argDesc, boolean show, 
				String def)
	{
		_longName = longName;
		_shortName = new Character(shortName);
		_help = argHelp;
		_desc = argDesc;
		_show = show;
		_hasarg = hasArg;
		_default = def;
	}

	public String getLongName()
	{
		return _longName;
	}

	public Character getShortName()
	{
		return _shortName;
	}

	public boolean getExpectsArgument()
	{
		return _hasarg;
	}

	public String getHelp()
	{
		return _help;
	}

	public String getDescription()
	{
		return _desc;
	}

	public boolean getShowArgInHelp()
	{
		return _show;
	}

	public String getArgumentDefault()
	{
		return _default;
	}

	public int hashCode()
	{
		StringBuffer buf = new StringBuffer(_longName);
		buf.append(_shortName);
		buf.append(_help);
		buf.append(_desc);

		return buf.toString().hashCode();
	}

	/**
	 * This method is used to retrieve the argument (if any) which
	 * was given to the option.
	 *
	 * @return the argument or null if no argument was specified
	 */

	public String getArg()
	{
		return _arg;
	}

	/**
	 * Indicates if this option has been matched by the command
	 * parser or not.
	 *
	 * @return true if matched; false otherwise
	 */

	public boolean getMatched()
	{
		return _matched;
	}

	/**
	 * This method is called by the command parser to indicate
	 * that the option has been matched.
	 * <p>
	 * This method may be overridden by derived classes to provide
	 * object-oriented command-line argument handling.  The
	 * default implementation simply sets the value returned by
	 * {@link #getMatched} to <code>true</code> and stores the
	 * argument.
	 *
	 * @param arg the argument (if expected)
	 */

	public void optionMatched(String arg)
	{
		_matched = true;
		_arg = arg;
	}

	/**
	 * This method is used to support multiple parses by a
	 * CommandParser instance using different sets of arguments.
	 * Derived classes should override this method to reset any
	 * state stored using the @{link optionMatched} method.
	 */

	public void reset()
	{
		_matched = false;
		_arg = null;
	}

	private final boolean	_hasarg;
	private final boolean	_show;
	private final Character	_shortName;
	private final String	_default;
	private final String	_desc;
	private final String	_help;
	private final String	_longName;

	private String		_arg = null;
	private boolean		_matched = false;
}
