//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2004-2005, Andrew S. Townley
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
// File:	OptionConstraint.java
// Created:	Tue Jul 20 13:54:03 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import com.townleyenterprises.common.InjectResourceManager;
import com.townleyenterprises.common.ResourceManager;

/**
 * This is the base class for all option constraints.
 *
 * @version $Id: OptionConstraint.java,v 1.2 2005/10/02 03:20:47 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public abstract class OptionConstraint implements InjectResourceManager
{
	/**
	 * The constructor initializes the option being constrained,
	 * an exit status and message to be used during failure.
	 *
	 * @param status the status code
	 * @param opt the option being constrained
	 * @param msg the message to report
	 */

	protected OptionConstraint(int status, CommandOption option,
				String msg)
	{
		_option = option;
		_message = msg;
		_status = status;
	}

	public int getExitStatus()
	{
		return _status;
	}

	public CommandOption getOption()
	{
		return _option;
	}

	/**
	 * This method should be implemented by derived classes to
	 * perform the constraint check.
	 *
	 * @return true if the constraint is valid; false otherwise
	 */

	public abstract boolean isOK();

	public String getMessage()
	{
		return _message;
	}

	public void injectResourceManager(ResourceManager resources)
	{
		_resources = resources;
	}

	protected String getResourceString(String key)
	{
		if(_resources == null)
			return Strings.get(key);

		return _resources.getString(key);
	}

	protected String formatResourceString(String key, Object[] args)
	{
		if(_resources == null)
			return Strings.format(key, args);

		return _resources.format(key, args);
	}

	private final CommandOption	_option;
	private final String		_message;
	private final int		_status;

	private ResourceManager		_resources = null;
}
