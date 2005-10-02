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
// File:	CommandParser.java
// Created:	Sun May 11 20:18:27 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Iterator;

import com.townleyenterprises.command.event.ConstraintEvent;
import com.townleyenterprises.command.event.OptionEvent;
import com.townleyenterprises.command.event.OptionExceptionEvent;
import com.townleyenterprises.command.event.ParseEvent;

/**
 * This class provides support for parsing command-line arguments.
 *
 * @version $Id: CommandParser.java,v 1.24 2005/10/02 03:19:24 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public final class CommandParser
{
	/**
	 * This is a special exception thrown to abort parser
	 * processing.
	 */

	private static class AbortAction extends RuntimeException {}

	/**
	 * This class is used to make life easier by mapping 3 things
	 * at once.  If we match an argument, we automatically have
	 * all we need to call the appropriate listener.
	 */

	private static class OptionHolder
	{
		public OptionHolder(CommandOption o, CommandListener l)
		{
			option = o;
			listener = l;
		}
	
		final CommandOption	option;
		final CommandListener	listener;
	}

	/**
	 * The default constructor initializes the parser with the
	 * standard '-' and '--' switches for the short and long
	 * options.  To use a different switch, the alternate
	 * constructor may be used instead.
	 *
	 * @param appName the name of the application
	 */

	public CommandParser(String appName)
	{
		this(appName, null);
	}

	/**
	 * This version of the constructor alows a description for the
	 * unhandled arguments to be supplied to the parser.
	 * Primarily this is intended for use by the autohelp feature.
	 *
	 * @param appName the name of the application
	 * @param argHelp the help for the additional arguments which
	 * 	may be supplied to the application
	 */

	public CommandParser(String appName, String argHelp)
	{
		this(appName, argHelp, '-', "--", "--");
	}

	/**
	 * This version of the constructor allows the client to
	 * specify the switch characters to be used for the short and
	 * long options.
	 *
	 * @param appName the name of the application
	 * @param argHelp the help for the additional arguments which
	 * 	may be supplied to the application
	 * @param sSwitch the single character option switch
	 * @param lSwitch the long option switch
	 * @exception RuntimeException
	 * 	if a single character is used for the long switch
	 */

	public CommandParser(String appName, String argHelp,
				char sSwitch, String lSwitch)
	{
		this(appName, argHelp, sSwitch, lSwitch, "--");
	}

	/**
	 * This version of the constructor allows the client to
	 * specify the switch characters to be used for the short and
	 * long options.  It also allows the specification of the
	 * string to mark the end of the argument list.  By default,
	 * this string is <code>--</code> which conforms to the POSIX
	 * standard.
	 *
	 * @param appName the name of the application
	 * @param argHelp the help for the additional arguments which
	 * 	may be supplied to the application
	 * @param sSwitch the single character option switch
	 * @param lSwitch the long option switch
	 * @param endOfArgsMarker the string marking the end of the
	 * 	argument list (may be null).  Anything after this
	 * 	string is treated as a leftover argument.
	 * @exception RuntimeException
	 * 	if a single character is used for the long switch
	 * @since 2.1
	 */

	public CommandParser(String appName, String argHelp,
				char sSwitch, String lSwitch,
				String endOfArgsMarker)
	{
		_appname = appName;
		_arghelp = argHelp;
		_sswitch = sSwitch;
		_lswitch = lSwitch;
		_eoargs = endOfArgsMarker;

		if(_lswitch.length() == 1)
		{
			throw new RuntimeException(
				Strings.get("sParserLswitchError"));
		}

		setAutoHelpListener(new DefaultAutoHelpListener());
	}

	/**
	 * This method tells the parser to automatically handle
	 * command lines with the help character.  Optionally, the
	 * help or usage can be printed when no arguments are
	 * specified.  By default autohelp is enabled and zero
	 * arguments are allowed.
	 *
	 * @param autohelp true to use autohelp; false to disable
	 * @param allowZeroArgs true to allow commands to have no
	 * 	arguments; false to require at least one argument
	 */

	public void enableAutohelp(boolean autohelp,
				boolean allowZeroArgs)
	{
		_autohelp = autohelp;
		_zeroarg = allowZeroArgs;
	}

	/**
	 * This method is used to register a new command listener with
	 * the parser.
	 *
	 * @param listener the CommandListener instance
	 */

	public void addCommandListener(CommandListener listener)
	{
		// prevent adding the same listener more than once
		if(_listeners.contains(listener))
			return;

		CommandOption[] opts = listener.getOptions();

		for(int i = 0; i < opts.length; ++i)
		{
			addOption(opts[i], listener);
		}

		_listeners.add(listener);
	}

	/**
	 * This method is used to unregister a command listener with
	 * the parser.
	 *
	 * @param listener the CommandListener instance
	 */

	public void removeCommandListener(CommandListener listener)
	{
		CommandOption[] opts = listener.getOptions();

		for(int i = 0; i < opts.length; ++i)
		{
			removeOption(opts[i]);
		}

		_listeners.remove(listener);
	}

	/**
	 * This is the main parsing function that should be called to
	 * trigger the parsing of the command-line arguments
	 * registered with the parser.
	 *
	 * @param args the command-line arguments to parse
	 */

	public void parse(String[] args)
	{
		if(args.length == 0 && !_zeroarg)
		{
			usage();
			return;
		}
		
		if(_autohelp)
		{
			addCommandListener(_helpl);
		}
		else
		{
			removeCommandListener(_helpl);
		}

		// reset all the options (fix for multiple parse bug)
		resetOptions();

		OptionHolder val = null;

		boolean copyargs = false;
		_leftovers = new ArrayList();
		
		for(int i = 0; i < args.length; ++i)
		{
			String	s = args[i];

			// executive decision:  if the argument is
			// empty, it's silently ignored

			if(s == null || s.length() == 0)
				continue;

			if(s.equals(_eoargs))
			{
				copyargs = true;
				continue;
			}

			if(copyargs)
			{
				_leftovers.add(s);
				continue;
			}

			// take care of the normal processing
			try
			{
				i = processArg(i, args);
			}
			catch(AbortAction e)
			{
				//e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * This method allows the client of the argument parser to
	 * retrieve any unhandled arguments in the argument list.  The
	 * main use of this method is to get options such as file
	 * names from the command line.
	 *
	 * @return an array of String objects or a zero-length array
	 * 	if none were present
	 */

	public String[] getUnhandledArguments()
	{
		if(_leftovers == null)
			return new String[0];

		String[] args = new String[_leftovers.size()];
		return (String[])_leftovers.toArray(args);
	}

	/**
	 * This method is used to retrieve a particular option
	 * by name.
	 *
	 * @param name the long name of the option
	 * @return the option or null if none exist
	 * @since 3.0
	 */

	public CommandOption getOption(String name)
	{
		OptionHolder oh = (OptionHolder)_longOpts.get(name);
		if(oh != null)
			return oh.option;

		return null;
	}

	/**
	 * This method is used to retrieve a particular option
	 * by its switch.
	 *
	 * @param sw the short name of the option (or
	 * switch)
	 * @return the option or null if none exist
	 * @since 3.0
	 */

	public CommandOption getOption(char sw)
	{
		OptionHolder oh = (OptionHolder)_shortOpts.get(new Character(sw));
		if(oh != null)
			return oh.option;

		return null;
	}

	/**
	 * This method prints the automatically generated help
	 * messages for the registered options.
	 */

	public void help()
	{
		_helpl.help();
	}

	/**
	 * This method is used to print the usage summary information.
	 */

	public void usage()
	{
		_helpl.usage();
	}

	/**
	 * This method is used to configure the command parser to exit
	 * with the specified return code when it encounters arguments
	 * with missing required parameters.
	 *
	 * @param val toggles the behavior
	 * @param status the exit status to pass to System.exit()
	 * @deprecated As of the 3.0 version, this method is
	 * deprecated.  The recommended way to migrate code is
	 * to manipulate a {@link
	 * com.townleyenterprises.command.ParserListener} instance directly.
	 */

	public void setExitOnMissingArg(boolean val, int status)
	{
		if(!(_parserl instanceof DefaultParserListener))
		{
			throw new RuntimeException(
				Strings.get("sDeprecatedMethod"));
		}

		((DefaultParserListener)_parserl).setExitOnMissingArg(val, status);
	}

	/**
	 * This method is used to configure the command parser to stop
	 * executing commands when an unhandled exeception is thrown
	 * by an option.
	 *
	 * @param val toggles the behavior
	 * @deprecated This behavior of this method has been
	 * removed from the CommandParser.  If you wish to
	 * implement it, create a custom implementation of the
	 * {@link
	 * com.townleyenterprises.command.ParserListener}
	 * interface instead.
	 */

	public void setAbortExecuteOnError(boolean val)
	{
		if(!(_parserl instanceof DefaultParserListener))
		{
			throw new RuntimeException(
				Strings.get("sDeprecatedMethod"));
		}

		((DefaultParserListener)_parserl).setAbortExecuteOnError(val);
	}

	/**
	 * This method is used to set optional text which can be
	 * printed before and after the command option descriptions.
	 *
	 * @param header the text to be printed before the option
	 * 	descriptions
	 * @param footer the text to be printed after the option
	 * 	descriptions
	 */

	public void setExtraHelpText(String header, String footer)
	{
		if(_helpl != null)
		{
			_helpl.setHeader(header);
			_helpl.setFooter(footer);
		}
	}

	/**
	 * This method is used to check all of the command constraints
	 * and execute all of the options.
	 *
	 * @exception Exception if anything bad happens
	 * @since 3.0
	 */

	public void executeCommands() throws Exception
	{
		checkConstraints();

		for(Iterator i = _commands.iterator(); i.hasNext(); )
		{
			CommandOption opt = null;
			try
			{
				opt = (CommandOption)i.next();
				if(opt.getMatched())
				{
					opt.execute();
				}
			}
			catch(Exception e)
			{
				OptionExceptionEvent evt = new
					OptionExceptionEvent(this, 
						opt, e);
				if(!_parserl.onExecuteException(evt))
				{
					throw e;
				}
			}
		}
	}

	/**
	 * This method is used to add an option constraint to the
	 * parser.
	 *
	 * @param constraint the constraint to check
	 * @since 3.0
	 */

	public void addConstraint(OptionConstraint constraint)
	{
		_constraints.add(constraint);
	}

	/**
	 * This method is used to remove a constraint from the parser.
	 *
	 * @param constraint the constraint to remove
	 * @since 3.0
	 */

	public void removeConstraint(OptionConstraint constraint)
	{
		_constraints.remove(constraint);
	}

	/**
	 * This method is used to retrieve the short switch
	 * currently in use.
	 *
	 * @since 3.0
	 */

	public char getShortSwitch()
	{
		return _sswitch;
	}

	/**
	 * This method is used to retrieve the long switch
	 * currently in use.
	 *
	 * @since 3.0
	 */

	public String getLongSwitch()
	{
		return _lswitch;
	}

	/**
	 * This method is used to retrieve the indicator for
	 * the end of the argument list.
	 *
	 * @since 3.0
	 */

	public String getEndOfArgsIndicator()
	{
		return _eoargs;
	}

	/**
	 * This method is used to retrieve the application
	 * name.
	 *
	 * @since 3.0
	 */

	public String getApplicationName()
	{
		return _appname;
	}

	/**
	 * This method is used to retrieve the argument list
	 * help text for the application.
	 *
	 * @since 3.0
	 */

	public String getArgumentHelp()
	{
		return _arghelp;
	}

	/**
	 * This method returns the current parser listener.
	 * @since 3.0
	 */

	public ParserListener getParserListener()
	{
		return _parserl;
	}

	/**
	 * This method is used to set the current parser
	 * listener.
	 *
	 * @param listener the new listener
	 * @since 3.0
	 */

	public void setParserListener(ParserListener listener)
	{
		_parserl = listener;
	}

	/**
	 * This method is used to get the current help
	 * listener.
	 *
	 * @since 3.0
	 */

	public AutoHelpListener getAutoHelpListener()
	{
		return _helpl;
	}

	/**
	 * This method is used to set the current autohelp
	 * listener.
	 *
	 * @param listener the new listener
	 * @since 3.0
	 */

	public void setAutoHelpListener(AutoHelpListener listener)
	{
		_helpl = listener;
		_helpl.injectParser(this);
	}

	/**
	 * This method returns all of the currently registered
	 * command listeners.
	 *
	 * @since 3.0
	 */

	public CommandListener[] getCommandListeners()
	{
		return (CommandListener[])_listeners.toArray(new CommandListener[_listeners.size()]);
	}

	/**
	 * This method is used to check the constraints.
	 */

	private void checkConstraints()
	{
		if(_checkedConstraints)
			return;

		for(Iterator i = _constraints.iterator(); i.hasNext(); )
		{
			OptionConstraint oc = (OptionConstraint)i.next();
			if(!oc.isOK())
			{
				ConstraintEvent event = new
					ConstraintEvent(this, oc);
				_parserl.onConstraintFailure(event);
				return;
			}
		}

		_checkedConstraints = true;
	}

	/**
	 * This method is an easy way to add a new command option to
	 * the appropriate places.
	 *
	 * @param opt the CommandOption to add
	 * @param l the CommandListener to notify
	 */

	private void addOption(CommandOption opt, CommandListener l)
	{
		OptionHolder holder = new OptionHolder(opt, l);
	
		String lname = opt.getLongName();
		Character c = opt.getShortName();

		// sanity check for existing options
		OptionHolder lobj = (OptionHolder)_longOpts.get(lname);
		OptionHolder sobj = (OptionHolder)_shortOpts.get(c);
		if(lobj != null || sobj != null)
		{
			CommandOption dup = null;
			if(lobj != null) dup = lobj.option;
			if(sobj != null) dup = sobj.option;
			OptionEvent event = new OptionEvent(this, opt);
			if(!_parserl.onAddDuplicateOption(event, dup))
				return;

			String desc = null;
			String sopt = null;
			if(lobj != null)
			{
				sopt = lname;
				desc = lobj.listener.getDescription();
			}
			else if(sobj != null)
			{
				sopt = c.toString();
				desc = sobj.listener.getDescription();
			}
		}

		// set up the maps
		_longOpts.put(lname, holder);

		if(c.charValue() != 0)
		{
			_shortOpts.put(c, holder);
		}

		if(!_commands.contains(opt))
		{
			_commands.add(opt);
		}

		// inject the parser reference
		opt.injectParser(this);
	}

	/**
	 * This method controls what happens when a missing argument
	 * for an option is encountered.
	 *
	 * @param val the OptionHolder
	 */

	private boolean handleMissingArg(OptionHolder val)
	{
		String hlp = val.option.getHelp();
		if(hlp == null || hlp.length() == 0)
		{
			hlp = Strings.get("sParserDefaultArg");
		}

		String name = val.option.getLongName();
		if(name == null || name.length() == 0)
		{
			StringBuffer buf = new StringBuffer();
			buf.append(_sswitch);
			buf.append(val.option.getShortName());
			name = buf.toString();
		}
		else
		{
			StringBuffer buf = new StringBuffer(_lswitch);
			buf.append(name);
			name = buf.toString();
		}

		OptionEvent event = new OptionEvent(this, val.option);
		return _parserl.onMissingArgument(event);
	}

	/**
	 * This method is used to unregister a command option from the
	 * appropriate places.
	 *
	 * @param opt the CommandOption to delete
	 */

	private void removeOption(CommandOption opt)
	{
		_longOpts.remove(opt.getLongName());
		_shortOpts.remove(opt.getShortName());
		_commands.remove(opt);
	}

	/**
	 * This method is used to reset the option state prior to parsing.
	 * It is necessary to ensure that each time the parse is
	 * performed, the correct results are returned.
	 */

	private void resetOptions()
	{
		Iterator i = _commands.iterator();
		while(i.hasNext())
		{
			CommandOption opt = (CommandOption)i.next();
			opt.reset();
		}

		_checkedConstraints = false;
	}

	private int processArg(int argc, String[] args)
	{
		OptionHolder val = null;
		String	s = args[argc];

		if(s == null || s.length() == 0)
			return --argc;

		char c0 = s.charAt(0);
		int slen = s.length();
		int idx = s.indexOf("=");
		
		if((_sswitch == c0) && (slen > 1)
				&& !(s.startsWith(_lswitch)))
		{
			// we have one of the following:
			//
			// 1. a switch
			// 2. a posix option
			// 3. a set of combined options
			
			if(slen == 2)
			{
				val = (OptionHolder)_shortOpts.get(new Character(s.charAt(1)));
			}
			else if(slen > 2)
			{
				val = (OptionHolder)_longOpts.get(s);

				if(val == null)
				{
					// must be combined switches
					return expandSwitches(s.substring(1),
							argc, args);
				}
			}
		}
		else if(s.startsWith(_lswitch))
		{
			// must be a long option
			String key;
			if(idx != -1)
			{
				key = s.substring(_lswitch.length(), idx);
			}
			else
			{
				key = s.substring(_lswitch.length());
			}

			val = (OptionHolder)_longOpts.get(key);
		}
		else
		{
			_leftovers.add(s);
			return argc;
		}

		// if we get here should have a value
		if(val == null)
		{
			ParseEvent event = new ParseEvent(this, s);
			_parserl.onUnknownOption(event);
			throw new AbortAction();
		}

		String arg = null;

		// handle the option
		if(val.option.getExpectsArgument())
		{
			// check to make sure that there's no
			// '=' sign.
			if(idx != -1)
			{
				arg = s.substring(idx + 1);
				if(arg.length() == 0)
				{
					if(!handleMissingArg(val))
						throw new AbortAction();

					return argc;
				}
			}
			else
			{
				if(++argc < args.length)
				{
					arg = args[argc];
				}
				else
				{
					if(!handleMissingArg(val))
						throw new AbortAction();
					
					return --argc;
				}

				// FIXME:  needs to be handled
				// better...
				if(arg.startsWith(_lswitch)
						|| arg.charAt(0) == _sswitch)
				{
					if(!handleMissingArg(val))
						throw new AbortAction();

					return --argc;
				}
			}
		}

		if(!matchOption(val, arg))
			throw new AbortAction();

		return argc;
	}

	private int expandSwitches(String sw, int argc, String[] args)
	{
		OptionHolder oh = null;
		Character ch = null;
		String arg = null;

		for(int i = 0; i < sw.length(); ++i)
		{
			ch = new Character(sw.charAt(i));
			oh = (OptionHolder)_shortOpts.get(ch);
			if(oh == null)
			{
				ParseEvent event = new ParseEvent(this, ch.toString());
				_parserl.onUnknownSwitch(event);
				throw new AbortAction();
			}

			if(oh.option instanceof JoinedCommandOption)
			{
				if(i == 0)
				{
					arg = sw.substring(1);
					if(!matchOption(oh, arg))
						throw new AbortAction();
					break;
				}
				else
				{
					ParseEvent event = new ParseEvent(this, ch.toString());
					_parserl.onUnknownSwitch(event);
					throw new AbortAction();
				}
			}
			else
			{
				if(oh.option.getExpectsArgument()
						&& (i == sw.length() - 1))
				{
					if(++argc < args.length)
					{
						arg = args[argc];
					}
					else
					{
						if(!handleMissingArg(oh))
							throw new AbortAction();
						return --argc;
					}

					if(arg.startsWith(_lswitch)
							|| arg.charAt(0) == _sswitch)
					{
						if(!handleMissingArg(oh))
							throw new AbortAction();

						return --argc;
					}
				}
				else if(oh.option.getExpectsArgument())
				{
					ParseEvent event = new ParseEvent(this, sw);
					_parserl.onInvalidOptionCombination(event);
					throw new AbortAction();
				}
			}

			// match the option
			if(!matchOption(oh, arg))
				throw new AbortAction();
		}		

		return argc;
	}

	private boolean matchOption(OptionHolder val, String arg)
	{
		// give the option a chance to do what it
		// wants
		try
		{
			val.option.optionMatched(arg);
			
			// notify the listeners
			val.listener.optionMatched(val.option, arg);
		}
		catch(Throwable t)
		{
			OptionExceptionEvent oee = new OptionExceptionEvent(this, val.option, t);
			return _parserl.onMatchException(oee);
		}
		return true;
	}

	/** the name of our application */
	private String			_appname;

	/** the help text for the unhandled arguments */
	private String			_arghelp;

	/** our map for the long options */
	private HashMap			_longOpts = new HashMap();

	/** our map of the short options */
	private HashMap			_shortOpts = new HashMap();

	/** our registered listeners */
	private ArrayList		_listeners = new ArrayList();

	/** indicate if we should handle autohelp */
	private boolean			_autohelp = true;

	/** indicate if allow no arguments */
	private boolean			_zeroarg = true;

	/** the short switch */
	private char			_sswitch;

	/** the long switch */
	private String			_lswitch;

	/** string to signal end of the argument list */
	private String			_eoargs;

	/** the unhandled arguments */
	private ArrayList		_leftovers;

	/** controls if we exit on missing arguments */
	private boolean			_exitmissing;

	/** the exit code to use if exit on missing arguments */
	private int			_exitstatus;

	/** the preamble to print before the options */
	private String			_preamble = null;

	/** the postamble to print */
	private String			_postamble = null;

	/** keep track of the registered commands */
	private ArrayList		_commands = new ArrayList();

	/** track the constraints */
	private ArrayList		_constraints = new ArrayList();

	/** track if we've checked our constraints */
	private boolean			_checkedConstraints = false;

	/** the current command event listener */
	private ParserListener		_parserl = new DefaultParserListener();

	/** the current help listener */
	private AutoHelpListener	_helpl = null;
}
