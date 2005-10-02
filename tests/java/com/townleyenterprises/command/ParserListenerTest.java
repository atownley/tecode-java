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
// File:	ParserListenerTest.java
// Created:	Sun Oct  2 01:43:24 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import java.util.List;
import junit.framework.TestCase;
import com.townleyenterprises.command.event.*;

/**
 * This set of tests makes sure that the new parser listener
 * interface gets called when it should.
 *
 * @version $Id: ParserListenerTest.java,v 1.1 2005/10/02 03:18:30 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class ParserListenerTest extends TestCase
		implements CommandListener
{
	private static class Runner extends CommandOption
	{
		public Runner(String lname, char sname)
		{
			super(lname, sname, false, null, null);
		}

		public void execute() throws Exception
		{
			run = true;
		}

		public boolean run = false;
	}

	public ParserListenerTest(String testname)
	{
		super(testname);
	}

	public void testAddDuplicateOption()
	{
		final CommandOption dup = new CommandOption("one",
			'Q', true, "ARG", "Some argument value");
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onAddDuplicateOption(OptionEvent event, CommandOption option)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), dup);
				assertEquals(option, opt1);
				return true;
			}
		});
		parser.addCommandListener(this);
		parser.addCommandListener(new DefaultCommandListener("dups",
			new CommandOption[] { dup }));
		assertEquals(dup, parser.getOption('Q'));
	}

	public void testAddDuplicateOptionSkip()
	{
		final CommandOption dup = new CommandOption("one",
			'Q', true, "ARG", "Some argument value");
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onAddDuplicateOption(OptionEvent event, CommandOption option)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), dup);
				assertEquals(option, opt1);
				return false;
			}
		});
		parser.addCommandListener(this);
		parser.addCommandListener(new DefaultCommandListener("dups",
			new CommandOption[] { dup }));
		assertNull(parser.getOption('Q'));
	}

	public void testOnMutexConstraintFailure()
	{
		final OptionConstraint oc = new MutexOptionConstraint(1,opt1,opt2);
		parser.setParserListener(new AbstractParserListener()
		{
			public void onConstraintFailure(ConstraintEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getConstraint(), oc);
			}
		});
		parser.addCommandListener(this);
		parser.addConstraint(oc);
		parser.parse(new String[] { "--one", "one", "--onlylong", "long" });
	}

	public void testOnRequiredConstraintFailure()
	{
		final OptionConstraint oc = new RequiredOptionConstraint(1,opt1);
		parser.setParserListener(new AbstractParserListener()
		{
			public void onConstraintFailure(ConstraintEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getConstraint(), oc);
			}
		});
		parser.addCommandListener(this);
		parser.addConstraint(oc);
		parser.parse(new String[] { "--three" });
		parser.parse(new String[] { "--one", "one" });
	}

	public void testOnRequiresAnyConstraintFailure()
	{
		final OptionConstraint oc = new RequiresAnyOptionConstraint(1,opt1, new CommandOption[] { opt2, opt3 });
		parser.setParserListener(new AbstractParserListener()
		{
			public void onConstraintFailure(ConstraintEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getConstraint(), oc);
			}
		});
		parser.addCommandListener(this);
		parser.addConstraint(oc);
		parser.parse(new String[] { "--onlylong", "long" });
		parser.parse(new String[] { "--one", "one", "--onlylong", "long" });
		parser.parse(new String[] { "--one", "one", "-t" });
	}

	public void testOnRequiresOneConstraintFailure()
	{
		final OptionConstraint oc = new RequiresOneOptionConstraint(1, new CommandOption[] { opt2, opt3 });
		parser.setParserListener(new AbstractParserListener()
		{
			public void onConstraintFailure(ConstraintEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getConstraint(), oc);
			}
		});
		parser.addCommandListener(this);
		parser.addConstraint(oc);
		parser.parse(new String[] { "--onlylong", "long" });
		parser.parse(new String[] { "--one", "one", "--three" });
		parser.parse(new String[] { "--three", "--onlylong", "long" });
		parser.parse(new String[] { "--one", "one" });
	}

	public void testAbortOnExecuteException()
	{
		final Exception ex = new Exception("BOOM!");
		final CommandOption bomb = new CommandOption("bomb",
				'X', false, null, "blows up") {
			public void execute() throws Exception
			{
				throw ex;
			}
		};

		final Runner run = new Runner("run", 'R');
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onExecuteException(OptionExceptionEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), bomb);
				assertEquals(event.getException(), ex);
				return false;
			}
		});
		parser.addCommandListener(
			new DefaultCommandListener(null,
				new CommandOption[] { bomb, run }));
		parser.parse(new String[] { "--bomb", "--run" });
		try
		{
			parser.executeCommands();
		}
		catch(Exception e)
		{
			assertEquals(ex, e);
		}
		assertEquals(false, run.run);
	}

	public void testNoAbortOnExecuteException() throws Exception
	{
		final Exception ex = new Exception("BOOM!");
		final CommandOption bomb = new CommandOption("bomb",
				'X', false, null, "blows up") {
			public void execute() throws Exception
			{
				throw ex;
			}
		};

		final Runner run = new Runner("run", 'R');
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onExecuteException(OptionExceptionEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), bomb);
				assertEquals(event.getException(), ex);
				return true;
			}
		});
		parser.addCommandListener(
			new DefaultCommandListener(null,
				new CommandOption[] { bomb, run }));
		parser.parse(new String[] { "--bomb", "--run" });
		parser.executeCommands();
		assertEquals(true, run.run);
	}

	public void testOnInvalidOptionCombination()
	{
		parser.setParserListener(new AbstractParserListener()
		{
			public void onInvalidOptionCombination(ParseEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getString(), "1t");
			}
		});
		parser.addCommandListener(this);
		parser.parse(new String[] { "-1t", "five" });
	}

	public void testAbortOnMatchException()
	{
		final RuntimeException ex = new RuntimeException("BOOM!");
		final CommandOption bomb = new CommandOption("bomb",
				'X', false, null, "blows up") {
			public void optionMatched(String arg)
			{
				throw ex;
			}
		};

		final Runner run = new Runner("run", 'R');
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onMatchException(OptionExceptionEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), bomb);
				assertEquals(event.getException(), ex);
				return false;
			}
		});
		parser.addCommandListener(
			new DefaultCommandListener(null,
				new CommandOption[] { bomb, run }));
		parser.parse(new String[] { "--bomb", "--run" });
		assertEquals(false, run.getMatched());
	}

	public void testNoAbortOnMatchException() throws Exception
	{
		final RuntimeException ex = new RuntimeException("BOOM!");
		final CommandOption bomb = new CommandOption("bomb",
				'X', false, null, "blows up") {
			public void optionMatched(String arg)
			{
				throw ex;
			}
		};

		final Runner run = new Runner("run", 'R');
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onMatchException(OptionExceptionEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), bomb);
				assertEquals(event.getException(), ex);
				return true;
			}
		});
		parser.addCommandListener(
			new DefaultCommandListener(null,
				new CommandOption[] { bomb, run }));
		parser.parse(new String[] { "--bomb", "--run" });
		assertEquals(true, run.getMatched());
	}

	public void testAbortOnMissingArgument() throws Exception
	{
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onMissingArgument(OptionEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), opt1);
				return false;
			}
		});
		parser.addCommandListener(this);
		parser.parse(new String[] { "--one", "--three" });
		assertEquals(false, opt3.getMatched());
	}

	public void testNoAbortOnMissingArgument() throws Exception
	{
		parser.setParserListener(new AbstractParserListener()
		{
			public boolean onMissingArgument(OptionEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getOption(), opt1);
				return true;
			}
		});
		parser.addCommandListener(this);
		parser.parse(new String[] { "--one", "--three" });
		assertEquals(true, opt3.getMatched());
	}

	public void testOnUnknownOption() throws Exception
	{
		parser.setParserListener(new AbstractParserListener()
		{
			public void onOnUnknownOption(ParseEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getString(), "arf");
			}
		});
		parser.addCommandListener(this);
		parser.parse(new String[] { "--arf", "--three" });
		assertEquals(false, opt3.getMatched());
	}

	public void testOnUnknownSwitch() throws Exception
	{
		parser.setParserListener(new AbstractParserListener()
		{
			public void onOnUnknownSwitch(ParseEvent event)
			{
				assertEquals(event.getParser(), parser);
				assertEquals(event.getString(), "tf1");
			}
		});
		parser.addCommandListener(this);
		parser.parse(new String[] { "-tf1", "one" });
		assertEquals(false, opt1.getMatched());
		assertEquals(true, opt3.getMatched());
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
	CommandOption opt4 = new CommandOption("one", 'X', false,
			null, "descriptive text 4");
	CommandOption opt5 = new CommandOption("ixx", 't', false,
			null, "descriptive text 5");
	CommandOption opt6 = new CommandOption("default", (char)0,
			true, "ARG", "this option has a default value", "yay");
	JoinedCommandOption joined = new JoinedCommandOption('D', false, "PROPERTY=VALUE", "set the system property", true);

	DelimitedCommandOption delim = new DelimitedCommandOption("zed", 'Z', "VALUE[,VALUE...]", "set the specified value");

	PosixCommandOption posix = new PosixCommandOption("display", true, "ARG", "specify the system display variable");

	RepeatableCommandOption repeat = new RepeatableCommandOption("rep", 'R', "VALUE", "set the specified value");

	CommandOption[] options1 = 
		new CommandOption[] { opt1, opt2, opt3, opt6, joined };
	CommandOption[] options2 = 
		new CommandOption[] { opt1, opt2, opt3, opt4, opt5 };
	CommandOption[] options3 =
		new CommandOption[] { delim, posix, repeat };
}
