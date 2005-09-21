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
// File:	OptionExceptionEvent.java
// Created:	Wed Sep 21 21:41:05 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command.event;

import com.townleyenterprises.command.CommandOption;
import com.townleyenterprises.command.CommandParser;

/**
 * This class allows the option to be sent to the listener in
 * the event of a parser event pertaining to a specific option
 * that generated an exception.  In the normal case, this
 * exception will be a RuntimeException, but maybe not always.
 *
 * @version $Id: OptionExceptionEvent.java,v 1.1 2005/09/21 23:01:12 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class OptionExceptionEvent extends OptionEvent
{
	/**
	 * The constructor takes a reference to the
	 * CommandOption involved in the event and the
	 * exception.
	 *
	 * @param parser the parser
	 * @param option the option
	 * @param throwable the exception
	 */

	public OptionExceptionEvent(CommandParser parser, 
			CommandOption option, Throwable throwable)
	{
		super(parser, option);
		_throwable = throwable;
	}

	public Throwable getException()
	{
		return _throwable;
	}

	/** track the exception */
	private final Throwable	_throwable;
}
