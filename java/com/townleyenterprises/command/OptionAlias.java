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
// File:	OptionAlias.java
// Created:	Sat Oct  1 21:50:46 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class implements a decorator for an existing
 * CommandOption allowing the option to be accessed by more
 * than one long name or switch.
 *
 * @version $Id: OptionAlias.java,v 1.1 2005/10/02 00:03:13 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class OptionAlias extends CommandOption
{
	/**
	 * The long name and short name must be specified when
	 * setting up the alias.
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param option the option to decorate.
	 */

	public OptionAlias(String longName, char shortName,
				CommandOption option)
	{
		super(longName, shortName,
			option.getExpectsArgument(),
			option.getHelp(),
			option.getDescription(),
			option.getShowArgInHelp(),
			option.getArgumentDefault());
		_option = option;
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
		return _option.getArg();
	}

	/**
	 * Indicates if this option has been matched by the command
	 * parser or not.
	 *
	 * @return true if matched; false otherwise
	 */

	public boolean getMatched()
	{
		return _option.getMatched();
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
		_option.optionMatched(arg);
	}

	/**
	 * This method is used to support multiple parses by a
	 * CommandParser instance using different sets of arguments.
	 * Derived classes should override this method to reset any
	 * state stored using the @{link optionMatched} method.
	 */

	public void reset()
	{
		_option.reset();
	}

	/**
	 * This method is used to provide the argument parsed as the
	 * appropriate type.  By default, all arguments are treated as
	 * string values.
	 *
	 * @return the argument as a String object
	 */

	public Object getArgValue()
	{
		return _option.getArgValue();
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
		_option.execute();
	}

	private CommandOption	_option = null;
}
