//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2001-2003, Andrew S. Townley
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
// Name:	TraceMessage.java
// Author:	Andrew S. Townley <adz1092 at netscape dot net>
// Created:	Fri Dec 12 10:39:08 GMT 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

/**
 * This class encapsulates a message which is to be written to the
 * trace log.  It is mainly useful when you want to collect a set of
 * data and then write it all at once rather than as individual lines.
 * The main difference between this and just using a StringBuffer is
 * that it will append Traceable messages if it can rather than
 * toString() which can sometimes be quite expensive.
 *
 * @since 3.0
 */

public class TraceMessage 
{
	public TraceMessage()
	{
		this(null);
	}

	public TraceMessage(String message)
	{
		if(message != null)
			_buf = new StringBuffer(message);
		else
			_buf = new StringBuffer();
	}

	/**
	 * This method allows adding traceable contents via traceString()
	 * instead of toString().
	 *
	 * @param traceable Traceable instance
	 */

	public StringBuffer append(Traceable traceable)
	{
		return _buf.append(traceable.traceString());
	}

	public String toString()
	{
		return _buf.toString();
	}

	// implementation of decorator methods so we can be used as
	// StringBuffer

	public StringBuffer append(boolean arg) { return _buf.append(arg); }
	public StringBuffer append(char arg) { return _buf.append(arg); }
	public StringBuffer append(char[] arg) { return _buf.append(arg); }
	public StringBuffer append(char[] arg, int offset, int len) { return _buf.append(arg, offset, len); }
	public StringBuffer append(double arg) { return _buf.append(arg); }
	public StringBuffer append(float arg) { return _buf.append(arg); }
	public StringBuffer append(int arg) { return _buf.append(arg); }
	public StringBuffer append(long arg) { return _buf.append(arg); }
	public StringBuffer append(Object arg) { return _buf.append(arg); }

	private final StringBuffer _buf;
}
