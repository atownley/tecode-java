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
//     * Neither the names Andrew Townley or Townley Enterprises,
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
// File:	VersionMismatchException.java
// Created:	Mon Jan  3 17:52:42 GMT 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

/**
 * This class encapsulates information about a version mismatch.  It
 * is derived from the original Version.VersionMismatchException
 * class, but is substantially more useable.
 *
 * @version $Id: VersionMismatchException.java,v 1.1 2005/01/03 18:55:46 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class VersionMismatchException extends RuntimeException
{
	/**
	 * The constructor specifies both the expected and the actual
	 * version as strings.
	 *
	 * @param exp the expected version
	 * @param act the actual version
	 */

	public VersionMismatchException(String exp, String act)
	{
		super(Strings.format("fVersionMismatch2",
			new Object[] { exp, act }));

		_actualVersion = act;
		_expectedVersion = exp;
	}

	/**
	 * Accessor for the actual version.
	 */

	public String getActualVersion()
	{
		return _actualVersion;
	}

	/**
	 * Accessor for the expected version.
	 */

	public String getExpectedVersion()
	{
		return _expectedVersion;
	}

	/** the actual version */
	private final String	_actualVersion;

	/** the expected version */
	private final String	_expectedVersion;
}
