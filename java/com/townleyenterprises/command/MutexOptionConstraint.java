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
// File:	MutexOptionConstraint.java
// Created:	Tue Jul 20 13:59:20 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class provides an implementation of a mutual exclusion
 * constraint for two options.
 *
 * @version $Id: MutexOptionConstraint.java,v 1.1 2004/07/30 16:20:22 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class MutexOptionConstraint extends OptionConstraint
{
	/**
	 * The constructor initializes the option being constrained
	 * and an exit status.
	 *
	 * @param status the status code
	 * @param opt the option being constrained
	 * @param opt2 the other option
	 */
	
	public MutexOptionConstraint(int status, CommandOption opt,
				CommandOption opt2)
	{
		super(status, opt, null);
		_exoption = opt2;
		_custmsg = false;
	}

	/**
	 * The constructor initializes the option being constrained,
	 * an exit status and message to be used during failure.
	 *
	 * @param status the status code
	 * @param opt the option being constrained
	 * @param opt2 the other option
	 * @param msg the message to report
	 */

	public MutexOptionConstraint(int status, CommandOption opt,
				CommandOption opt2, String message)
	{
		super(status, opt, message);
		_exoption = opt2;
		_custmsg = true;
	}

	public String getMessage()
	{
		if(_custmsg)
			return super.getMessage();

		return Strings.format("fMutexError", new Object[] {
				getOption().getName(),
				_exoption.getName() });
	}

	public boolean isOK()
	{
		return !(getOption().getMatched()
				&& _exoption.getMatched());
	}

	private final boolean		_custmsg;
	private final CommandOption	_exoption;
}
