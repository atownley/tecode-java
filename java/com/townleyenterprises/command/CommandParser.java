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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class provides support for parsing command-line arguments.
 *
 * @version $Id: CommandParser.java,v 1.10 2004/01/27 20:14:22 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
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

	// FIXME:  needs l10n support!!
	
	private static CommandOption[] ahopts = {
		new CommandOption("help", '?', false, null, "show this help message"),
		new CommandOption("usage", (char)0, false, null, "show brief usage message")
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
			throw new RuntimeException("long switch must be at least 2 characters");
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

	public void enableAutohelp(boolean autohelp, boolean allowZeroArgs)
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
		_leftovers = new Vector();
		
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

			char	c0 = s.charAt(0);
			char	c1 = 0;
			int	idx = s.indexOf('=');

			if(s.length() > 1)
				c1 = s.charAt(1);

			// FIXME:  there may be better ways to do
			// this, but I'm going to do the easy way
			// first.
			
			if((_sswitch == c0) || (s.startsWith(_lswitch)))
			{
				// it is supposed to be an option
				// is it a combined short option?
				if(s.length() > 2 && _sswitch == c0 &&
						(_sswitch != c1))
				{
					System.err.println("error:  combined options are not yet supported (" + s + ")");
					continue;
				}
				else if(s.length() == 2 &&
						_sswitch == c0)
				{
					// should be in the short map
					val = (OptionHolder)_shortOpts.get(new Character(s.charAt(1)));
				}
				else
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
			}
			else
			{
				_leftovers.add(s);
				continue;
			}

			// if we get here should have a value
			if(val == null)
			{
				System.err.println("error:  unknown option specified (" + s + ")");
				usage();
				break;
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
					if(++i < args.length)
					{
						arg = args[i];
					}
					else
					{
						handleMissingArg(val);
						continue;
					}
				}
			}

			// give the option a chance to do what it
			// wants
			val.option.optionMatched(arg);
			
			// notify the listeners
			val.listener.optionMatched(val.option, arg);
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
		return "Help options";
	}

	/**
	 * This method prints the automatically generated help
	 * messages for the registered options.
	 */

	public void help()
	{
		System.out.print("Usage:  " + _appname + " [OPTION...]");
		if(_arghelp != null && _arghelp.length() != 0)
		{
			System.out.print(" " + _arghelp);
		}
		System.out.println("");

		for(Enumeration e = _listeners.elements(); e.hasMoreElements(); )
		{
			CommandListener l = (CommandListener)e.nextElement();
			System.out.println("\n" + l.getDescription() + ":");

			printOptionsHelp(l.getOptions());
		}
	}

	/**
	 * This method is used to print the usage summary information.
	 */

	public void usage()
	{
		StringBuffer buf = new StringBuffer("Usage:  ");
		buf.append(_appname);

		for(Enumeration e = _listeners.elements(); e.hasMoreElements(); )
		{
			CommandListener l = (CommandListener)e.nextElement();
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
					buf.append("|");
				}
				if(ln != null)
				{
					buf.append(_lswitch);
					buf.append(ln);
				}

				if(opts[i].getExpectsArgument())
				{
					if(sn.charValue() != 0)
						buf.append(" ");
					else
						buf.append("=");

					if(hlp != null)
					{
						buf.append(hlp);
					}
					else
					{
						buf.append("<arg>");
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
	 */

	public void setExitOnMissingArg(boolean val, int status)
	{
		_exitmissing = val;
		_exitstatus = status;
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
			String desc = null;
			System.err.print("warning:  overriding option");
			System.err.print(" '");
			if(lobj != null)
			{
				System.err.print(lname);
				desc = lobj.listener.getDescription();
			}
			else if(sobj != null)
			{
				System.err.print(c);
				desc = sobj.listener.getDescription();
			}
			System.err.print("' from '");
			System.err.print(desc);
			System.err.print("' by '");
			System.err.print(l.getDescription());
			System.err.println("'.");
		}

		// set up the maps
		_longOpts.put(lname, holder);

		if(c.charValue() != 0)
		{
			_shortOpts.put(c, holder);
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
			hlp = "<arg>";
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

		String msg = "error:  option " + name + " requires parameter '" + hlp + "'.";
		if(_exitmissing)
		{
			System.err.print(msg);
			System.err.println("  Exiting.");
			usage();
			System.exit(_exitstatus);
		}
		else
		{
			System.err.print(msg);
			System.err.println("  Ignored.");
		}
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
				buf.append(_lswitch);
				buf.append(ln);
			}

			if(opts[i].getExpectsArgument())
			{
				if(ln != null)
				{
					buf.append("=");
				}
				else
				{
					buf.append(" ");
				}
				if(hlp != null)
				{
					buf.append(hlp);
				}
				else
				{
					buf.append("<arg>");
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
				buf.append(" (default: ");
				
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
	}

	/**
	 * This method is used to reset the option state prior to parsing.
	 * It is necessary to ensure that each time the parse is
	 * performed, the correct results are returned.
	 */

	private void resetOptions()
	{
		Iterator i = _longOpts.values().iterator();
		while(i.hasNext())
		{
			OptionHolder holder = (OptionHolder)i.next();
			holder.option.reset();
		}
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
	private Vector		_listeners = new Vector();

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
	private Vector		_leftovers;

	/** controls if we exit on missing arguments */
	private boolean		_exitmissing;

	/** the exit code to use if exit on missing arguments */
	private int		_exitstatus;

	/** the maximum width of the switch part */
	private final int	SWITCH_LENGTH = 35;
}
