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
// File:	AbstractLineProcessor.java
// Created:	Mon Jan 26 18:10:44 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.io;

/**
 * This class is used to provide default implementations for the
 * methods in the LineProcessor interface.  It also keeps track of the
 * count of lines processed.
 *
 * @version $Id: AbstractLineProcessor.java,v 1.1 2004/01/26 18:49:36 atownley Exp $
 * @author <a href="mailto:adz1092@nestscape.net">Andrew S. Townley</a>
 * @since 2.1
 */

public abstract class AbstractLineProcessor implements LineProcessor
{
	/**
	 * This method gets called for each line processed containing
	 * the text of the line.
	 *
	 * @param line the line of the input file
	 * @exception Exception
	 *	if an error occurs processing the line
	 */

	public void processLine(String line) throws Exception
	{
		++_lines;
	}

	/**
	 * This method gets called to reset any internal state
	 * maintained by the instance prior to starting any input.
	 */

	public void reset()
	{
		_lines = 0;
	}

	/**
	 * This method returns the number of lines processed since the
	 * last reset.
	 *
	 * @return the count
	 */

	public int getLineCount()
	{
		return _lines;
	}

	/** the number of lines processed */
	private int _lines = 0;
}
