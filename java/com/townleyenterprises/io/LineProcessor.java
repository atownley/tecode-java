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
// File:	LineProcessor.java
// Created:	Mon Jan 26 17:54:36 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.io;

/**
 * This interface is used to provide line-based processing of text
 * files.
 *
 * @version $Id: LineProcessor.java,v 1.1 2004/01/26 18:49:36 atownley Exp $
 * @author <a href="mailto:adz1092@nestscape.net">Andrew S. Townley</a>
 * @since 2.1
 */

public interface LineProcessor
{
	/**
	 * This method gets called for each line processed containing
	 * the text of the line.
	 *
	 * @param line the line of the input file
	 * @exception Exception
	 *	if an error occurs processing the line
	 */

	void processLine(String line) throws Exception;

	/**
	 * This method gets called to reset any internal state
	 * maintained by the instance prior to starting any input.
	 */

	void reset();
}
