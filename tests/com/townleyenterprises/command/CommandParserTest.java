//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2002-2004, Andrew S. Townley
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
// File:	CommandParserTest.java
// Created:	Thu Jan 22 23:10:58 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import junit.framework.TestCase;

/**
 * Unit tests for the CommandParser class.  These tests mainly deal
 * with successfully parsing the options.  Manual verification of the
 * formatting of the usage and help output will still be necessary.
 *
 * @version $Id: CommandParserTest.java,v 1.1 2004/01/25 19:39:17 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class CommandParserTest extends TestCase
		implements CommandListener
{
	public CommandParserTest(String testname)
	{
		super(testname);
	}

	public void testOptionOneWithSpaces()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--one", "value" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
	}

	public void testOptionOneWithEquals()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--one=value" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
	}

	public void testOptionOneShort()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "-1", "value" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
	}

	public void testOptionNoneMatched()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "one", "two", "three" };
		parser.parse(args);

		assertFalse(opt1.getMatched());
		assertFalse(opt2.getMatched());
		assertFalse(opt3.getMatched());
		assertFalse(opt4.getMatched());
	}

	public void testOptionOneMissingArg()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--one" };
		parser.parse(args);
		assertFalse(opt1.getMatched());
	}

	public void testOnlyLongOption()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--onlylong", "value" };
		parser.parse(args);
		
		assertTrue(opt2.getMatched());
		assertEquals("value", opt2.getArg());
	}

	public void testOnlyLongOptionMissingArg()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--onlylong" };
		parser.parse(args);
		
		assertFalse(opt2.getMatched());
	}

	public void testConflictingOptions()
	{
		parser.addCommandListener(new AbstractCommandListener() {
			public String getDescription()
			{
				return "test1";
			}

			public CommandOption[] getOptions()
			{
				return options2;
			}
		});
		String[] args = new String[] { "--one", "two" };
		parser.parse(args);
		assertFalse(opt1.getMatched());
		assertTrue(opt5.getMatched());
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(CommandParserTest.class);
	}

	public String getDescription()
	{
		return "CommandParserTest";
	}

	public CommandOption[] getOptions()
	{
		return options1;
	}

	public void optionMatched(CommandOption opt, String arg)
	{
	}

	CommandParser parser = new CommandParser("progname");

	CommandOption opt1 = new CommandOption("one", '1', true,
			"ARG", "Some argument value");
	CommandOption opt2 = new CommandOption("onlylong", (char)0,
			true, "ARG", "option 2 text");
	CommandOption opt3 = new CommandOption("three", 't', false,
			null, "some descriptive text");
	CommandOption opt4 = new CommandOption("four", 'f', false,
			null, "descriptive text 4");
	CommandOption opt5 = new CommandOption("one", 'X', false,
			null, "descriptive text 5");
	
	CommandOption[] options1 = 
		new CommandOption[] { opt1, opt2, opt3, opt4 };
	CommandOption[] options2 = 
		new CommandOption[] { opt1, opt2, opt3, opt4, opt5 };
}
