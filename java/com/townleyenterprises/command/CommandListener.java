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
// File:	CommandListener.java
// Created:	Sun May 11 20:19:05 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This interface must be implemented by classes that intend to
 * respond to command-line arguments processed by the CommandParser.
 *
 * @version $Id: CommandListener.java,v 1.3 2004/07/28 10:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public interface CommandListener
{
	/**
	 * This method is used to retrieve the description of the
	 * command listener's options when printing the help message.
	 * Usually it is the name of the application or subsystem.
	 */

	public String getDescription();

	/**
	 * This method is called by the CommandParser to determine all
	 * of the arguments that should be handled by the listener.
	 *
	 * @return an array of CommandOption arguments.  In the case
	 * 	where no arguments are specified (why would you
	 * 	bother?), a zero-length array should be returned.
	 */

	public CommandOption[] getOptions();

	/**
	 * This method is called whenever an arguement registered with
	 * the parser is detected in the argument stream.
	 *
	 * @param opt the CommandOption matched by the parser
	 * @param arg the argument (if any) specified for the option
	 */

	public void optionMatched(CommandOption opt, String arg);
}
