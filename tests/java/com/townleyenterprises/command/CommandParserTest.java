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
 * @version $Id: CommandParserTest.java,v 1.3 2004/07/30 16:19:33 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class CommandParserTest extends TestCase
		implements CommandListener
{
	public CommandParserTest(String testname)
	{
		super(testname);
	}

	public void testAllOptionsRegistered()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--one", "value",
					"--onlylong", "value3",
					"--three" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertTrue(opt2.getMatched());
		assertTrue(opt3.getMatched());
	}

	public void testOptionOneWithSpaces()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--one", "value" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
		assertEquals("value", opt1.getArgValue());
	}

	public void testOptionOneWithEquals()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--one=value" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
		assertEquals("value", opt1.getArgValue());
	}

	public void testOptionOneShort()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "-1", "value" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
		assertEquals("value", opt1.getArgValue());
	}

	public void testOptionNoneMatched()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "one", "two", "three" };
		parser.parse(args);

		assertFalse(opt1.getMatched());
		assertFalse(opt2.getMatched());
		assertFalse(opt3.getMatched());
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
		assertEquals("value", opt2.getArgValue());
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
		String[] args = new String[] { "--one", "two", "-t" };
		parser.parse(args);
		assertFalse(opt1.getMatched());
		assertFalse(opt3.getMatched());
		assertTrue(opt4.getMatched());
		assertTrue(opt5.getMatched());
	}

	public void testUnregisterListener()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--one", "value",
					"--onlylong", "value3",
					"--three" };
		parser.parse(args);

		assertTrue(opt1.getMatched());
		assertTrue(opt2.getMatched());
		assertTrue(opt3.getMatched());
		
		// now unregister and try to parse
		parser.removeCommandListener(this);

		// manually reset our option's state
		opt1.reset();
		opt2.reset();
		opt3.reset();

		// re-parse the argument list
		parser.parse(args);

		assertFalse(opt1.getMatched());
		assertFalse(opt2.getMatched());
		assertFalse(opt3.getMatched());
	}

	public void testAlternateSwitches()
	{
		CommandParser altp = new CommandParser("altp", null,
						'/', "^^");
		altp.addCommandListener(this);
		String[] args = new String[] { "^^one", "value",
					"^^onlylong", "value3",
					"/t" };
		altp.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
		assertEquals("value", opt1.getArgValue());
		assertTrue(opt2.getMatched());
		assertEquals("value3", opt2.getArg());
		assertEquals("value3", opt2.getArgValue());
		assertTrue(opt3.getMatched());
	}

	public void testSameSwitches()
	{
		try
		{
			CommandParser altp = new CommandParser("altp",
						null, '/', "/");
		}
		catch(RuntimeException e)
		{
			assertEquals("long switch must be at least 2 characters.", e.getMessage());
		}
	}

	public void testLongLongSwitch()
	{
		CommandParser altp = new CommandParser("altp", null,
						'*', "*****");
		altp.addCommandListener(this);
		String[] args = new String[] { "*****one", "value",
					"*****onlylong", "value3",
					"*t" };
		altp.parse(args);

		assertTrue(opt1.getMatched());
		assertEquals("value", opt1.getArg());
		assertEquals("value", opt1.getArgValue());
		assertTrue(opt2.getMatched());
		assertEquals("value3", opt2.getArg());
		assertEquals("value3", opt2.getArgValue());
		assertTrue(opt3.getMatched());
	}

	public void testOptionDefault()
	{
		parser.addCommandListener(this);
		String[] args = new String[0];
		parser.parse(args);

		assertFalse(opt6.getMatched());
		assertEquals("yay", opt6.getArg());
		assertEquals("yay", opt6.getArgValue());
	}

	public void testEndOfArgsPosix()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "--", "-1", "value", "-t", "--onlylong", "value" };
		parser.parse(args);

		assertFalse(opt1.getMatched());
		assertFalse(opt2.getMatched());
		assertFalse(opt3.getMatched());
		assertEquals(5, parser.getUnhandledArguments().length);
	}

	public void testEndOfArgsNone()
	{
		CommandParser altp = new CommandParser("altp", null,
						'-', "--", null);
		altp.addCommandListener(this);
		
		String[] args = new String[] { "--", "-1", "value", "-t", "--onlylong", "value" };
		altp.parse(args);
	
		// this tests causes the argument parsing to stop at the '--'
		assertFalse(opt1.getMatched());
		assertFalse(opt2.getMatched());
		assertFalse(opt3.getMatched());
	}

	public void testEndOfArgsCustom()
	{
		CommandParser altp = new CommandParser("altp", null,
						'-', "--", "***");
		altp.addCommandListener(this);
		
		String[] args = new String[] { "***", "--", "-1", "value", "-t", "--onlylong", "value" };
		altp.parse(args);
	
		assertFalse(opt1.getMatched());
		assertFalse(opt2.getMatched());
		assertFalse(opt3.getMatched());
		assertEquals(6, altp.getUnhandledArguments().length);
	}

	public void testSingleJoinedOption()
	{
		parser.addCommandListener(this);
		String[] args = new String[] { "-Dfoo=bar" };
		parser.parse(args);
		
		assertTrue(joined.getMatched());
		assertEquals("foo=bar", joined.getArg());
	}

//	public void testHelp()
//	{
//		// NOTE:  this is a manual test
//		parser.addCommandListener(this);
//		String[] args = new String[] { "--help" };
//		parser.parse(args);
//	}
		
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
	CommandOption opt4 = new CommandOption("one", 'X', false,
			null, "descriptive text 4");
	CommandOption opt5 = new CommandOption("ixx", 't', false,
			null, "descriptive text 5");
	CommandOption opt6 = new CommandOption("default", (char)0,
			true, "ARG", "this option has a default value", "yay");
	CommandOption joined = new JoinedCommandOption('D', false, "PROPERTY=VALUE", "set the system property", true);

	CommandOption[] options1 = 
		new CommandOption[] { opt1, opt2, opt3, opt6, joined };
	CommandOption[] options2 = 
		new CommandOption[] { opt1, opt2, opt3, opt4, opt5 };
}
