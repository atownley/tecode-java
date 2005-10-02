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
// File:	RequiresOneOptionConstraint.java
// Created:	Sun Oct  2 01:25:55 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This class provides an implementation of a dependency constraint
 * between options where one of the listed options must have
 * been matched.
 *
 * @version $Id: RequiresOneOptionConstraint.java,v 1.1 2005/10/02 03:20:36 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class RequiresOneOptionConstraint extends OptionConstraint
{
	/**
	 * The constructor initializes the option being constrained
	 * and an exit status.
	 *
	 * @param status the status code
	 * @param deps the dependent options
	 */

	public RequiresOneOptionConstraint(int status,
				CommandOption[] deps)
	{
		super(status, null, null);
		_deps = deps;
		_custommsg = false;
	}

	/**
	 * The constructor initializes the option being constrained,
	 * an exit status and message to be used during failure.
	 *
	 * @param status the status code
	 * @param deps the dependent options
	 * @param msg the message to report
	 */

	public RequiresOneOptionConstraint(int status,
				CommandOption[] deps, String message)
	{
		super(status, null, message);
		_deps = deps;
		_custommsg = true;
	}

	public String getMessage()
	{
		if(_custommsg)
			return super.getMessage();

		StringBuffer buf = new StringBuffer(
			getResourceString("sRequiresOneError"));

		for(int i = 0; i < _deps.length; ++i)
		{
			String s = _deps[i].getName();
			
			buf.append("'");
			buf.append(s);
			buf.append("'");

			if(i < _deps.length - 1)
			{
				if(i == _deps.length - 1)
				{
					buf.append(getResourceString("sRequiresAnyOr"));
				}
				else
				{
					buf.append(", ");
				}
			}
		}

		return buf.toString();
	}

	public boolean isOK()
	{
		for(int i = 0; i < _deps.length; ++i)
		{
			if(_deps[i].getMatched())
				return true;
		}

		return false;
	}

	private final CommandOption[]	_deps;
	private final boolean		_custommsg;
}
