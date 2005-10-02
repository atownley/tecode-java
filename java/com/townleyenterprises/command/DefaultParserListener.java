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
// File:	DefaultParserListener.java
// Created:	Wed Sep 21 22:25:07 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import com.townleyenterprises.command.event.ConstraintEvent;
import com.townleyenterprises.command.event.OptionEvent;
import com.townleyenterprises.command.event.OptionExceptionEvent;
import com.townleyenterprises.command.event.ParseEvent;
import com.townleyenterprises.common.ResourceProvider;

/**
 * This class implements the ParserListener interface to
 * provide the parser behavior expected by existing programs.
 *
 * @version $Id: DefaultParserListener.java,v 1.3 2005/10/02 00:08:24 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class DefaultParserListener implements ParserListener
{
	/**
	 * This is the default constructor which uses the
	 * standard property values for this package when
	 * creating messages.
	 */

	public DefaultParserListener()
	{
		this(null);
	}

	/**
	 * This version of the constructor takes a
	 * ResourceProvider which will be merged with the
	 * default resource strings for the package.
	 *
	 * @param res the ResourceProvider
	 */

	public DefaultParserListener(ResourceProvider res)
	{
		if(res != null)
		{
			Strings.addResourceProvider(res);
		}
	}

	/**
	 * This method is called whenever the parser detects
	 * that a duplicate option has been detected,
	 * effectively about to shadow an existing option.
	 * The listener has the ability to handle this in any
	 * way it chooses, however the return value controls
	 * if the option is ignored or added to the parser's
	 * list of valid options.
	 *
	 * @param event the OptionEvent containing the option
	 * 	to be added
	 * @param option the option about to be shadowed
	 * @return true if the option should be added; false
	 * 	to skip it
	 */

	public boolean onAddDuplicateOption(OptionEvent event,
			CommandOption option)
	{
		String sopt = event.getOption().getLongName();
		String desc = event.getOption().getDescription();
		System.err.println(Strings.format("fParserWarnOverride",
			new Object[] { sopt, desc, 
				option.getDescription() }));
		return true;
	}

	/**
	 * This method is called when a constraint failure has
	 * occurred.  Some sort of error handling should be
	 * provided to indicate that the constraint has
	 * failed.
	 * <p>
	 * The command parser will stop parsing the current
	 * arguments after the first constraint failure.
	 * </p>
	 *
	 * @param event the event
	 */

	public void onConstraintFailure(ConstraintEvent event)
	{
		OptionConstraint oc = event.getConstraint();

		System.err.println(Strings.format("fParserConstraintFailure",
			new Object[] { oc.getMessage() }));

		event.getParser().usage();
		System.exit(oc.getExitStatus());
	}

	/**
	 * This method is called when the CommandOption's
	 * execute() method raises an exception.  If the
	 * listener wishes, it may allow execution of the
	 * remaining options, but this isn't normally the
	 * behavior a user would expect.
	 *
	 * @param event the event containing the option
	 * 	that generated the exception
	 * @return true to continue execution; false to abort
	 */

	public boolean onExecuteException(OptionExceptionEvent event)
	{
		if(_abortExecOnError)
			return false;

		System.err.println(event.getException());
		return true;
	}

	/**
	 * This method is called when an invalid option
	 * combination is detected when expanding the combined
	 * switches, e.g. <code>-xvf</code>.  If the options
	 * are not in the correct order, e.g.
	 * <code>-xfv</code> this method will be called.
	 *
	 * @param event the event containing the unparsable
	 * 	string
	 */

	public void onInvalidOptionCombination(ParseEvent event)
	{
		String sw = event.getString();
		System.err.println(Strings.format("fParserErrInvalidCombo", new Object[] { sw }));
		event.getParser().usage();
	}

	/**
	 * This method is called when a RuntimeException is
	 * thrown by the optionMatched event of the
	 * CommandOption.  This event allows parsing errors to
	 * be detected if there is a problem parsing the value
	 * when it is captured rather than waiting until the
	 * option executes.
	 *
	 * @param event the event containing the option and
	 * 	the exception
	 * @return true to continue execution; false to abort
	 */

	public boolean onMatchException(OptionExceptionEvent event)
	{
		System.err.println(event.getException().getMessage());
		return false;
	}

	/**
	 * This method is called when an option does not get
	 * the argument it expects.
	 *
	 * @param the event containint the option
	 * @return true to continue parsing; false to abort
	 */

	public boolean onMissingArgument(OptionEvent event)
	{
		CommandParser parser = event.getParser();
		CommandOption option = event.getOption();
		String hlp = option.getHelp();
		if(hlp == null || hlp.length() == 0)
		{
			hlp = Strings.get("sParserDefaultArg");
		}

		String name = option.getLongName();
		if(name == null || name.length() == 0)
		{
			StringBuffer buf = new StringBuffer();
			buf.append(parser.getShortSwitch());
			buf.append(option.getShortName());
			name = buf.toString();
		}
		else
		{
			StringBuffer buf = new StringBuffer(
					parser.getLongSwitch());
			buf.append(name);
			name = buf.toString();
		}

		if(_exitmissing)
		{
			System.err.println(Strings.format("fParserErrMissingParam",
					new Object[] {
						name, hlp,
						Strings.get("sExiting")
					}));
			event.getParser().usage();
			System.exit(_exitstatus);
		}
		else
		{
			System.err.println(Strings.format("fParserErrMissingParam",
					new Object[] {
						name, hlp,
						Strings.get("sIgnored")
					}));
		}

		return true;
	}

	/**
	 * This method is called when something which looks
	 * like an option is detected, but it isn't one that's
	 * registered with the parser.  In this case, "looks
	 * like" means that it starts with whatever the
	 * current switch characters are.
	 *
	 * @param event the event containing the string being
	 * 	parsed
	 */

	public void onUnknownOption(ParseEvent event)
	{
		System.err.println(
			Strings.format("fParserErrUnknownOption",
				new Object[] { event.getString() } ));
		event.getParser().usage();
	}

	/**
	 * This method is called when the parser encounters a
	 * string which isn't an option argument, but should
	 * be an option.
	 *
	 * @param event the event containing the string being
	 * 	parsed
	 */

	public void onUnknownSwitch(ParseEvent event)
	{
		System.err.println(Strings.format("fParserErrUnknownSwitch", new Object[] { event.getString() }));
		event.getParser().usage();
	}

	/**
	 * This method is to ensure we have backward
	 * compatibility with the existing CommandParser
	 * behavior.
	 *
	 * @param val toggles the behavior
	 * @param status the exit status for System.exit()
	 */

	public void setExitOnMissingArg(boolean val, int status)
	{
		_exitmissing = val;
		_exitstatus = status;
	}

	/**
	 * This method is used to configure the command parser to stop
	 * executing commands when an unhandled exeception is thrown
	 * by an option.
	 *
	 * @param val toggles the behavior
	 */

	public void setAbortExecuteOnError(boolean val)
	{
		_abortExecOnError = val;
	}

	/** determine if we're supposed to abort on exec error */
	private boolean		_abortExecOnError = true;

	/** if we're supposed to exit on missing arg */
	private boolean		_exitmissing = false;

	/** the exit status for missing args */
	private int		_exitstatus = 0;
}
