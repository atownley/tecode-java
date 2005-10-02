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
// File:	InjectParser.java
// Created:	Sat Oct  1 19:56:48 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

/**
 * This interface allows classes who implement it to support
 * dependency injection of the CommandParser.  The main
 * motivation for this interface is that even with the OO
 * approach of the whole library for parsing command line
 * arguments, it breaks down if the option needs access to
 * additional arguments.
 * <p>
 * The solution (without completely breaking stuff that
 * already exists) is to allow concrete options to implement
 * this interface.  If they do, they will automatically have a
 * parser dependency injected into them when they are
 * registered with the parser.  If they don't, then nothing
 * changes and they option implementation is oblivious of the
 * parser (or uses some other way to get a reference).
 * </p>
 * <p>
 * This particular approach (and naming conventions) are
 * adapted from <a
 * href="http://www.martinfowler.com/articles/injection.html#InterfaceInjection">an
 * article by Martin Fowler on dependency injection</a>.
 * </p>
 * <p>
 * Part of the reason to do it this way instead of just use
 * setter injection is just to make it pretty clear that this
 * is an optional thing, because the CommandOption class
 * shouldn't expose the parser as a public accessor.
 * </p>
 * <p>
 * This requirement was partially inspired by a request from
 * Ruud van Kersbergen.
 * </p>
 *
 * @version $Id: InjectParser.java,v 1.2 2005/10/02 00:09:31 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public interface InjectParser
{
	/**
	 * This method is called when the option is registered
	 * with the command parser.  A reference is passed to
	 * the current parser, thus allowing two-way linking
	 * between options and a parser.
	 */

	void injectParser(CommandParser parser);
}
