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
// File:	RequiredOptionConstraint.java
// Created:	Tue Jul 20 15:40:43 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class provides an implementation of a constraint which
 * requires the specific option to be matched.
 *
 * @version $Id: RequiredOptionConstraint.java,v 1.1 2004/07/30 16:20:22 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class RequiredOptionConstraint extends OptionConstraint
{
	/**
	 * The constructor initializes the option being constrained
	 * and an exit status.
	 *
	 * @param status the status code
	 * @param opt the option being constrained
	 */

	public RequiredOptionConstraint(int status, CommandOption opt)
	{
		super(status, opt, null);
		_custommsg = false;
	}

	/**
	 * The constructor initializes the option being constrained,
	 * an exit status and message to be used during failure.
	 *
	 * @param status the status code
	 * @param opt the option being constrained
	 * @param msg the message to report
	 */

	public RequiredOptionConstraint(int status,
			CommandOption opt, String message)
	{
		super(status, opt, message);
		_custommsg = true;
	}

	public String getMessage()
	{
		if(_custommsg)
			return super.getMessage();

		return Strings.format("fRequiredOptionError",
				new Object[] { getOption().getName() });
	}

	public boolean isOK()
	{
		return getOption().getMatched();
	}

	private final boolean	_custommsg;
}
