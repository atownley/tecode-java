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
// File:	JoinedCommandOption.java
// Created:	Mon Jan 26 11:26:47 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class provides support for "joined" command options.  Some
 * common examples of these are:
 * <ul>
 * <li>From gcc:
 * <ul>
 * <li><code><strong>-m[VALUE]</strong></code> options for
 * machine-dependent behavior</li>
 * <li><code><strong>-W[WARNING]</strong></code> options for warning
 * levels</li>
 * </ul>
 * </li>
 * <li>From java:
 * <ul>
 * <li><code><strong>-D[PROPERTY=VALUE]</strong></code> options to
 * set system properties</li>
 * <li><code><strong>-X[OPTION]</strong></code> non-standard
 * extension options</li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * Typically speaking, the value of the joined option must immediately
 * follow the option's short name or switch.  However, this class
 * supports behavior to allow the value to also be specified as the
 * next argument in the argument list.
 * </p>
 *
 * @version $Id: JoinedCommandOption.java,v 1.2 2004/07/28 10:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.1
 */

public class JoinedCommandOption extends RepeatableCommandOption
{
	/**
	 * The constructor specifies only the arguments which make
	 * sense for this type of option.  These options do not
	 * support long names.
	 *
	 * @param shortName this is the switch for the option
	 * @param canSplit true if this option can be split from the
	 * 	value; false means that the value must be joined to
	 * 	the switch
	 * @param argHelp the help string for the argument
	 * @param argDesc the long description of what the argument
	 * 	does
	 * @param show true if should be shown in autohelp
	 */

	public JoinedCommandOption(char shortName, boolean canSplit,
					String argHelp, String argDesc,
					boolean show)
	{
		super(null, shortName, argHelp, argDesc, show, null);
		_cansplit = canSplit;
	}

	/**
	 * This method is used to tell the parser if the option value
	 * can follow the switch or it must be part of the switch.
	 *
	 * @return true if the argument must be joined; false
	 * 	otherwise
	 */

	public boolean getArgCanSplit()
	{
		return _cansplit;
	}

	/** track if the argument value can be split from the switch */
	private final boolean	_cansplit;
}
