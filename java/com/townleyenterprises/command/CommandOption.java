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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class provides support for defining command-line arguments.
 *
 * @version $Id: CommandOption.java,v 1.8 2005/10/02 00:03:45 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public class CommandOption implements InjectParser
{
	/**
	 * This method is used to break up a <code>key=val</code>
	 * string into a 2 element array of <code>arr[0] = key</code>
	 * and <code>arr[1] = val</code>.
	 *
	 * @param str the string to process
	 * @return an array containing the values or an empty array
	 * (still with 2 elements) if the string does not have an
	 * equals.
	 * @since 3.0
	 */

	public static String[] parseOption(String str)
	{
		String[] rez = new String[2];

		int cut = str.indexOf("=");
		if(cut == -1)
			return rez;

		rez[0] = str.substring(0, cut - 1);
		rez[1] = str.substring(cut);

		return rez;
	}

	/**
	 * This method is used to parse a list options of the form
	 * <code>key=val</code> into a map which is easier to
	 * manipulate.
	 *
	 * @param list the list of options
	 * @return a map of the options
	 * @since 3.0
	 */

	public static Map parseOptions(List list)
	{
		HashMap map = new HashMap();
		for(Iterator i = list.iterator(); i.hasNext(); )
		{
			String[] ray = parseOption((String)i.next());
			map.put(ray[0], ray[1]);
		}

		return map;
	}

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
	 * @param def the default value of the argument if it is not
	 * 	specified.
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

	/**
	 * This version of the constructor allows specifying if the
	 * argument has a default value.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param hasArg true if this option expects an argument;
	 * 	false if it is a switch
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 * @param def the default value of the argument if it is not
	 *	specified.
	 * @since 2.1
	 */

	public CommandOption(String longName, char shortName,
				boolean hasArg, String argHelp,
				String argDesc, String def)
	{
		this(longName, shortName, hasArg, argHelp,
			argDesc, true, def);
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
	 * was given to the option.  If no argument was specified and
	 * the option has a default value, the default value will be
	 * returned instead.
	 *
	 * @return the argument or null if no argument was specified
	 */

	public String getArg()
	{
		if(_arg == null && _default != null)
			return _default;

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
	 *
	 * @since 2.1
	 */

	public void reset()
	{
		_matched = false;
		_arg = null;
	}

	/**
	 * This method is used to provide the argument parsed as the
	 * appropriate type.  By default, all arguments are treated as
	 * string values.
	 *
	 * @return the argument as a String object
	 * @since 2.1
	 */

	public Object getArgValue()
	{
		return getArg();
	}

	/**
	 * This method is used to allow the class to implement the GoF
	 * Command pattern fully.  Derived classes should override
	 * this method to perform any specific actions.  The default
	 * implementation does nothing.
	 *
	 * @exception Exception
	 * 	if something fails.
	 */

	public void execute() throws Exception
	{
	}

	/**
	 * This method is used to return some sort of normalized name
	 * for the option.
	 *
	 * @return a name
	 * @since 3.0
	 */

	public String getName()
	{
		String s = _longName;
		if(s == null)
			s = _shortName.toString();

		return s;
	}

	/**
	 * This method implements the InjectParser interface,
	 * allowing the option to have a reference to the
	 * current parser (and thus do things like get any
	 * leftover arguments after the parsing was complete).
	 *
	 * @param parser the command parser
	 */

	public void injectParser(CommandParser parser)
	{
		_parser = parser;
	}

	/**
	 * This method allows derived classes to access the
	 * parser instance.
	 *
	 * @return the parser instance
	 */

	protected CommandParser getParser()
	{
		return _parser;
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
	private CommandParser	_parser = null;
}
