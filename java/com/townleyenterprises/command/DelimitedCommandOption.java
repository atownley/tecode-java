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
// File:	DelimitedCommandOption.java
// Created:	Sun Jun  8 14:11:08 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import java.util.StringTokenizer;

/**
 * This class provides support for multi-valued options which are
 * specified using delmited values.  Examples of options of this form
 * are:
 * <pre>
 * 	-X one,two,three
 * 	--value=1,2,3,4
 * 	--eggs green
 * </pre>
 *
 * @version $Id: DelimitedCommandOption.java,v 1.3 2004/07/28 10:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public class DelimitedCommandOption extends RepeatableCommandOption
{
	/**
	 * The constructor takes almost all of the parent class's
	 * arguments, but the assumption is that if it was a regular
	 * command option, it wouldn't be necessary to recognize it
	 * more than once.  Therefore, this class assumes that there
	 * will be an argument each time it is matched by the command
	 * parser.  The default delmiter is the comma (','). 
	 *
	 * @param longName the long name to be checked
	 * @param shortName the short, single character name
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 */

	public DelimitedCommandOption(String longName, char shortName,
				String argHelp, String argDesc)
	{
		this(longName, shortName, argHelp, argDesc, 
				true, null, ",");
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
	 * @param delim the delimiter to be used to seperate the
	 * 	option values.
	 */

	public DelimitedCommandOption(String longName, char shortName,
				String argHelp, String argDesc,
				boolean show, String def, String delim)
	{
		super(longName, shortName, argHelp, argDesc, show, def);
		_delim = delim;
	}

	/**
	 * This method will parse the argument as it's added and
	 * ensure that all of the values get added to the parent
	 * class's list.
	 *
	 * @param arg the argument to be added
	 */

	protected void addArg(String arg)
	{
		StringTokenizer st = new StringTokenizer(arg, _delim);
		while(st.hasMoreTokens())
		{
			super.addArg(st.nextToken());
		}
	}

	private final String	_delim;
}
