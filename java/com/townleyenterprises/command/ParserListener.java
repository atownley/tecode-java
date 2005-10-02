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
// File:	ParserListener.java
// Created:	Wed Sep 21 21:49:15 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import com.townleyenterprises.command.event.ConstraintEvent;
import com.townleyenterprises.command.event.OptionEvent;
import com.townleyenterprises.command.event.OptionExceptionEvent;
import com.townleyenterprises.command.event.ParseEvent;

/**
 * This class implements the GoF Observer pattern for events
 * which originally assumed direct control of the console for
 * both reporting status as well as exiting the program.
 * <p>
 * This class should've existed a long time ago when I was
 * first trying to figure out how to test the CommandParser.
 * I almost wrote it then, but I was always in too big of a
 * hurry to do it because of other deadlines.  It's finally
 * getting written thanks to some prodding from Ruud van
 * Kersbergen from Holland House because he's doing some
 * things where exiting the JVM aren't very nice.
 * </p>
 *
 * @version $Id: ParserListener.java,v 1.2 2005/10/02 00:08:15 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public interface ParserListener
{
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

	boolean onAddDuplicateOption(OptionEvent event, 
			CommandOption option);

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

	void onConstraintFailure(ConstraintEvent event);

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

	boolean onExecuteException(OptionExceptionEvent event);

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

	void onInvalidOptionCombination(ParseEvent event);

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
	 * @return true to continue parsing; false to abort
	 */

	boolean onMatchException(OptionExceptionEvent event);

	/**
	 * This method is called when an option does not get
	 * the argument it expects.
	 *
	 * @param the event containint the option
	 * @return true to continue parsing; false to abort
	 */

	boolean onMissingArgument(OptionEvent event);

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

	void onUnknownOption(ParseEvent event);

	/**
	 * This method is called when the parser encounters a
	 * string which isn't an option argument, but should
	 * be an option.
	 *
	 * @param event the event containing the string being
	 * 	parsed
	 */

	void onUnknownSwitch(ParseEvent event);
}
