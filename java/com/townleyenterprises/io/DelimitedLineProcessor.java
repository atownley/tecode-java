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
// File:	DelimitedLineProcessor.java
// Created:	Mon Jan 26 17:59:36 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.io;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the capability to break a line of delimited
 * text into a list of objects representing each value.
 *
 * @version $Id: DelimitedLineProcessor.java,v 1.1 2004/01/26 18:49:36 atownley Exp $
 * @author <a href="mailto:adz1092@nestscape.net">Andrew S. Townley</a>
 * @since 2.1
 */

public abstract class DelimitedLineProcessor extends AbstractLineProcessor
{
	/**
	 * The constructor initializes the processor with the
	 * delimiter to be used.
	 *
	 * @param delim the delimiter
	 */

	public DelimitedLineProcessor(String delim)
	{
		_delim = delim;
	}

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
		if(line == null || line.length() == 0)
			return;

		ArrayList list = new ArrayList();
		int idx = 0;
		int lidx = -1;

		// break up the list
		while(idx != -1)
		{
			String tok;

			idx = line.indexOf(_delim, lidx+1);

			// does the line start with the delimiter
			if(idx == 0)
			{
				tok = "";
			}
			else
			{
				if(idx != -1)
				{
					tok = line.substring(lidx+1, idx);
				}
				else
				{
					tok = line.substring(lidx+1);
				}
			}

			list.add(tok);
			lidx = idx;
		}

		// call the abstract method
		processItems(list);
	}

	/**
	 * This method gets called after the input line has been
	 * broken up into the individual tokens.
	 *
	 * @param list the list to process
	 * @exception Exception
	 * 	if something goes wrong processing the items
	 */

	public abstract void processItems(List list) throws Exception;

	/**
	 * This method does nothing.
	 */

	public void reset()
	{
	}

	/** the delimiter */
	private String _delim;
}
