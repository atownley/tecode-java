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
// File:	PosixCommandOption.java
// Created:	Wed Jan 28 22:35:59 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class provides support for POSIX-compliant command options.
 *
 * @version $Id: PosixCommandOption.java,v 1.3 2004/07/28 10:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.1
 */

public class PosixCommandOption extends CommandOption
{
	/**
	 * POSIX command options can't have both and long forms (we're
	 * not counting abbreviations at the moment).  This method
	 * creates the option using the indicated switch and values.
	 *
	 * @param name the name to be checked
	 * @param hasArg true if this option expects an argument;
	 * 	false if it is a switch
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 */

	public PosixCommandOption(String name, boolean hasArg,
				String argHelp, String argDesc)
	{
		super(null, (char)0, hasArg, argHelp,
				argDesc, true, null);

		if(name.length() == 1)
		{
			_switch = new Character(name.charAt(0));
			_name = null;
		}
		else
		{
			_switch = new Character((char)0);
			_name = name;
		}
	}

	public String getLongName()
	{
		return _name;
	}

	public Character getShortName()
	{
		return _switch;
	}

	private final String	_name;
	private final Character	_switch;
}
