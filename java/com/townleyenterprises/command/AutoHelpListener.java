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
// File:	AutoHelpListener.java
// Created:	Sat Oct  1 20:46:37 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This interface provides additional configuration for
 * listeners who are providing autohelp functionality.
 *
 * @version $Id: AutoHelpListener.java,v 1.1 2005/10/01 20:30:45 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public interface AutoHelpListener extends CommandListener, InjectParser
{
	/**
	 * This method is used to set optional text which can be
	 * printed before the command option descriptions.
	 *
	 * @param header the text to be printed before the option
	 * 	descriptions
	 */

	void setHeader(String header);

	/**
	 * This method is used to set optional text which can be
	 * printed after the command option descriptions.
	 *
	 * @param footer the text to be printed after the option
	 * 	descriptions
	 */

	void setFooter(String footer);

	/**
	 * This method is used to print usage information.
	 */

	void usage();

	/**
	 * This method is used to print the detailed help.
	 */

	void help();

	/**
	 * This method controls if the program should exit
	 * when the help options are automatically triggered.
	 *
	 * @param val true to exit when the options are
	 * matched; false to not exit.
	 */

	void setExitOnHelp(boolean val);

	/**
	 * This method returns the current state of the exit
	 * on help setting.
	 *
	 * @return the current setting
	 */

	public boolean getExitOnHelp();
}
