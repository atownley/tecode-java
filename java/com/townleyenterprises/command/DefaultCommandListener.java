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
// File:	DefaultCommandListener.java
// Created:	Mon Jul 19 17:35:58 GMTDT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class allows boilerplate nested classes to be elimitated by
 * providing all of the things supplied by the CommandListener
 * interface as arguments to the constructor. 
 *
 * @version $Id: DefaultCommandListener.java,v 1.1 2004/07/30 16:20:03 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public class DefaultCommandListener extends AbstractCommandListener 
{
	/**
	 * The constructor is used to supply the arguments for the
	 * listener.
	 *
	 * @param desc the description
	 * @param opts the options
	 */

	public DefaultCommandListener(String desc, CommandOption[] opts)
	{
		_description = desc;
		_options = opts;
	}

	/**
	 * This method is used to retrieve the description of the
	 * command listener's options when printing the help message.
	 * Usually it is the name of the application or subsystem.
	 */

	public String getDescription()
	{
		return _description;
	}

	/**
	 * This method is called by the CommandParser to determine all
	 * of the arguments that should be handled by the listener.
	 *
	 * @return an array of CommandOption arguments.  In the case
	 * 	where no arguments are specified (why would you
	 * 	bother?), a zero-length array should be returned.
	 */

	public CommandOption[] getOptions()
	{
		return _options;
	}

	private final String 		_description;
	private final CommandOption[]	_options;
}
