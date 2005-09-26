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
 * @version $Id: CommandParser.java,v 1.21 2005/09/26 03:51:09 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public final class CommandParser implements CommandListener
{
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
	 * Specify the autohelp default handler options
	 */

	private static CommandOption[] ahopts = {
		new CommandOption("help", '?', false, null, 
			Strings.get("sParserOptionHelp")),
		new CommandOption("usage", (char)0, false, null,
			Strings.get("sParserUsageHelp"))
	};

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
			addCommandListener(this);
		}
		else
		{
			removeCommandListener(this);
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
			i = processArg(i, args);
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

	/// OptionListener interface
	
	public void optionMatched(CommandOption opt, String arg)
	{
		switch(opt.getShortName().charValue())
		{
			case '?':
				help();
				System.exit(0);
				break;
			case 0:
				usage();
				System.exit(0);
				break;
		}
	}

	public CommandOption[] getOptions()
	{
		return ahopts;
	}

	public String getDescription()
	{
		return Strings.get("sParserHelpOptionsDesc");
	}

	/**
	 * This method prints the automatically generated help
	 * messages for the registered options.
	 */

	public void help()
	{
		System.out.print(Strings.format("fParserUsage",
					new Object[] { _appname }));
		if(_arghelp != null && _arghelp.length() != 0)
		{
			System.out.print(" " + _arghelp);
		}
		System.out.println("");

		if(_preamble != null)
		{
			System.out.println("");
			printWrappedText(_preamble, ' ', 80, 0);
		}

		for(Iterator e = _listeners.iterator(); e.hasNext(); )
		{
			CommandListener l = (CommandListener)e.next();
			System.out.println("\n" + l.getDescription() + ":");

			printOptionsHelp(l.getOptions());
		}

		if(_postamble != null)
		{
			System.out.println("");
			printWrappedText(_postamble, ' ', 80, 0);
		}
	}

	/**
	 * This method is used to print the usage summary information.
	 */

	public void usage()
	{
		StringBuffer buf = new StringBuffer(Strings.get("sParserUsage"));
		buf.append(_appname);

		for(Iterator e = _listeners.iterator(); e.hasNext(); )
		{
			CommandListener l = (CommandListener)e.next();
			CommandOption[] opts = l.getOptions();
			for(int i = 0; i < opts.length; ++i)
			{
				Character sn = opts[i].getShortName();
				String ln = opts[i].getLongName();
				boolean show = opts[i].getShowArgInHelp();
				String hlp = opts[i].getHelp();

				if(!show)
				{
					continue;
				}

				buf.append(" [");
				if(sn.charValue() != 0)
				{
					buf.append(_sswitch);
					buf.append(sn);
					if(ln != null)
						buf.append("|");
				}
				if(ln != null)
				{
					if(opts[i] instanceof PosixCommandOption)
						buf.append(_sswitch);
					else
						buf.append(_lswitch);
					buf.append(ln);
				}

				if(opts[i].getExpectsArgument())
				{
					if((sn.charValue() != 0 &&
						!(opts[i] instanceof JoinedCommandOption))
						|| opts[i] instanceof PosixCommandOption)
						buf.append(" ");
					else if(sn.charValue() == 0 &&
						!(opts[i] instanceof JoinedCommandOption))
						buf.append("=");

					if(hlp != null)
					{
						buf.append(hlp);
					}
					else
					{
						buf.append(Strings.get("sParserDefaultArg"));
					}
				}
				
				buf.append("]");
			}
		}
		
		if(_arghelp != null && _arghelp.length() != 0)
		{
			// ok, this is cheating a little for when it
			// wraps based on the ] being in col 72...
			buf.append(" ");
			buf.append(_arghelp);
		}

		// now, we split the lines
		printWrappedText(buf.toString(), ']', 72, 8);
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
	 * @param preamble the text to be printed before the option
	 * 	descriptions
	 * @param postamble the text to be printed after the option
	 * 	descriptions
	 */

	public void setExtraHelpText(String preamble, String postamble)
	{
		_preamble = preamble;
		_postamble = postamble;
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

	public void setPaserListener(ParserListener listener)
	{
		_parserl = listener;
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
	}

	/**
	 * This method controls what happens when a missing argument
	 * for an option is encountered.
	 *
	 * @param val the OptionHolder
	 */

	private void handleMissingArg(OptionHolder val)
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
		_parserl.onMissingArgument(event);
	}

	/**
	 * This method is responsible for printing the options block
	 * for a given command listener.
	 *
	 * @param opts the command options
	 */

	private void printOptionsHelp(CommandOption[] opts)
	{
		for(int i = 0; i < opts.length; ++i)
		{
			StringBuffer buf = new StringBuffer("  ");
			Character sn = opts[i].getShortName();
			String ln = opts[i].getLongName();
			boolean show = opts[i].getShowArgInHelp();
			String ad = opts[i].getArgumentDefault();
			String hlp = opts[i].getHelp();
			String desc = opts[i].getDescription();
			Object val = opts[i].getArgValue();

			if(!show)
			{
				continue;
			}

			if(sn.charValue() != 0)
			{
				buf.append(_sswitch);
				buf.append(sn);

				if(ln != null)
				{
					buf.append(", ");
				}
			}
			if(ln != null)
			{
				if(opts[i] instanceof PosixCommandOption)
					buf.append(_sswitch);
				else
					buf.append(_lswitch);
				buf.append(ln);
			}

			if(opts[i].getExpectsArgument())
			{
				if(ln != null)
				{
					if(opts[i] instanceof PosixCommandOption)
						buf.append(" ");
					else
						buf.append("=");
				}
				else if(!(opts[i] instanceof JoinedCommandOption))
				{
					buf.append(" ");
				}
				if(hlp != null)
				{
					buf.append(hlp);
				}
				else
				{
					buf.append(Strings.get("sParserDefaultArg"));
				}
			}

			if(buf.length() >= SWITCH_LENGTH)
			{
				buf.append(" ");
			}

			for(int j = buf.length(); j < SWITCH_LENGTH; ++j)
			{
				buf.append(" ");
			}

			buf.append(desc);

			if(ad != null && ad.length() > 0)
			{
				buf.append(" (");
				buf.append(Strings.get("lParserDefault"));
				
				if(val instanceof String)
					buf.append("\"");
				else if(val instanceof Character)
					buf.append("'");
				
				buf.append(ad);
				
				if(val instanceof String)
					buf.append("\"");
				else if(val instanceof Character)
					buf.append("'");
				
				buf.append(")");
			}

			printWrappedText(buf.toString(), ' ',
					80, SWITCH_LENGTH);
		}
	}

	/**
	 * This method handles the multi-line formatting of the
	 * indicated text based on the cut character, and prefix
	 * indent.
	 *
	 * @param text the text to wrap
	 * @param cchar the character at which wrapping should take
	 * 	place (if necessary)
	 * @param width the width at which wrapping should take place
	 * @param indent the number of spaces to indent the text
	 */

	private void printWrappedText(String text, char cchar, 
				int width, int indent)
	{
		// check if we have a newline
		int nl = text.indexOf('\n');
		if(nl != -1)
		{
			int start = 0;
			while(nl != -1)
			{
				String sstr = text.substring(start, nl);
				printWrappedText(sstr, cchar,
						width, indent);
				start = nl+1;
				int x = sstr.indexOf('\n');
				if(x == -1)
				{
					printWrappedText(text.substring(start),
						cchar, width, indent);
					return;
				}
				
				nl += x;
			}
		}

		String line = text;
		int lwidth = width;
		while(line.length() > lwidth)
		{
			String t = null;
			int cut = lwidth;
			char c = line.charAt(cut);
			if(c != cchar)
			{
				int ocut = cut;
				cut = line.lastIndexOf(cchar, cut);
				if(cut > lwidth || cut == -1)
				{
					cut = line.lastIndexOf(' ', ocut);
					if(cut == -1)
					{
						// then we can't wrap
						// correctly, so just
						// bail and chop at
						// the edge
						cut = lwidth - 1;
					}
				}
				t = line.substring(0, cut + 1);
			}
			else if(c == cchar && Character.isWhitespace(c))
			{
				// we don't want the cchar
				t = line.substring(0, cut);
			}
			else
			{
				// we need to keep the cchar
				t = line.substring(0, ++cut);
			}

			System.out.println(t);
			line = line.substring(cut + 1).trim();
			for(int xx = 0; xx < indent; ++xx)
			{
				System.out.print(" ");
			}
			lwidth = width - indent;
		}
		System.out.println(line);
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
			return args.length;
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
					handleMissingArg(val);
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
					handleMissingArg(val);
					return ++argc;
				}

				// FIXME:  needs to be handled
				// better...
				if(arg.startsWith(_lswitch)
						|| arg.charAt(0) == _sswitch)
				{
					handleMissingArg(val);
					return ++argc;
				}
			}
		}

		// give the option a chance to do what it
		// wants
		val.option.optionMatched(arg);
		
		// notify the listeners
		val.listener.optionMatched(val.option, arg);

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
				return args.length;
			}

			if(oh.option instanceof JoinedCommandOption)
			{
				if(i == 0)
				{
					arg = sw.substring(1);
					oh.option.optionMatched(arg);
					oh.listener.optionMatched(oh.option, arg);
					break;
				}
				else
				{
					ParseEvent event = new ParseEvent(this, ch.toString());
					_parserl.onUnknownSwitch(event);
					return args.length;
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
						handleMissingArg(oh);
					}

					if(arg.startsWith(_lswitch)
							|| arg.charAt(0) == _sswitch)
					{
						handleMissingArg(oh);
					}
				}
				else if(oh.option.getExpectsArgument())
				{
					ParseEvent event = new ParseEvent(this, sw);
					_parserl.onInvalidOptionCombination(event);
					return args.length;
				}
			}

			// match the option
			oh.option.optionMatched(arg);
			oh.listener.optionMatched(oh.option, arg);
		}		

		return argc;
	}

	/** the name of our application */
	private String		_appname;

	/** the help text for the unhandled arguments */
	private String		_arghelp;

	/** our map for the long options */
	private HashMap		_longOpts = new HashMap();

	/** our map of the short options */
	private HashMap		_shortOpts = new HashMap();

	/** our registered listeners */
	private ArrayList	_listeners = new ArrayList();

	/** indicate if we should handle autohelp */
	private boolean		_autohelp = true;

	/** indicate if allow no arguments */
	private boolean		_zeroarg = true;

	/** the short switch */
	private char		_sswitch;

	/** the long switch */
	private String		_lswitch;

	/** string to signal end of the argument list */
	private String		_eoargs;

	/** the unhandled arguments */
	private ArrayList	_leftovers;

	/** controls if we exit on missing arguments */
	private boolean		_exitmissing;

	/** the exit code to use if exit on missing arguments */
	private int		_exitstatus;

	/** the preamble to print before the options */
	private String		_preamble = null;

	/** the postamble to print */
	private String		_postamble = null;

	/** the maximum width of the switch part */
	private final int	SWITCH_LENGTH = 35;

	/** keep track of the registered commands */
	private ArrayList	_commands = new ArrayList();

	/** track what we do on execute errors */
	private boolean		_abortExecOnError = true;

	/** track the constraints */
	private ArrayList	_constraints = new ArrayList();

	/** track if we've checked our constraints */
	private boolean		_checkedConstraints = false;

	/** the current command event listener */
	private ParserListener	_parserl = new DefaultParserListener();
}
