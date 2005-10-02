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
// File:	DefaultAutoHelpListener.java
// Created:	Sat Oct  1 20:29:18 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Iterator;

import com.townleyenterprises.common.ResourceManager;
import com.townleyenterprises.command.event.ConstraintEvent;
import com.townleyenterprises.command.event.OptionEvent;
import com.townleyenterprises.command.event.OptionExceptionEvent;
import com.townleyenterprises.command.event.ParseEvent;

/**
 * This class pulls out the autohelp handling capabilities
 * from the core parser and allows other implementations to be
 * more easily developed.
 *
 * @version $Id: DefaultAutoHelpListener.java,v 1.2 2005/10/02 00:07:01 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class DefaultAutoHelpListener implements AutoHelpListener
{
	/**
	 * Specify the autohelp default handler options
	 */

	private static CommandOption[] ahopts = {
		new LocalizedCommandOption("help", '?', false,
			"sParserOption"),
		new LocalizedCommandOption("usage", (char)0, false,
			"sParserUsage")
	};

	public DefaultAutoHelpListener()
	{
		this(true);
	}

	public DefaultAutoHelpListener(boolean exitOnHelp)
	{
		this(null, exitOnHelp);
	}

	public DefaultAutoHelpListener(ResourceManager res,
				boolean exitOnHelp)
	{
		_resources = res;
		_exit = exitOnHelp;
		if(_resources == null)
		{
			_delegate = new LocalizedCommandListener(
				Strings.getResourceManager(),
				"sParserHelpOptionsDesc", ahopts);
		}
		else
		{
			_delegate = new LocalizedCommandListener(res,
				"sParserHelpOptionsDesc", ahopts);
		}
	}

	public void optionMatched(CommandOption opt, String arg)
	{
		switch(opt.getShortName().charValue())
		{
			case '?':
				help();
				if(_exit) System.exit(0);
				break;
			case 0:
				usage();
				if(_exit) System.exit(0);
				break;
		}
	}

	public CommandOption[] getOptions()
	{
		return _delegate.getOptions();
	}

	public String getDescription()
	{
		return _delegate.getDescription();
	}

	/**
	 * This method prints the automatically generated help
	 * messages for the registered options.
	 */

	public void help()
	{
		System.out.print(formatResourceString("fParserUsage",
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

		CommandListener[] listeners = _parser.getCommandListeners();
		for(int i = 0; i < listeners.length; ++i)
		{
			CommandListener l = listeners[i];
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
		StringBuffer buf = new StringBuffer(getResourceString("sParserUsage"));
		buf.append(_appname);

		CommandListener[] listeners = _parser.getCommandListeners();
		for(int z = 0; z < listeners.length; ++z)
		{
			CommandListener l = listeners[z];
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
						buf.append(getResourceString("sParserDefaultArg"));
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
	 * This method is used to set optional text which can be
	 * printed before the command option descriptions.
	 *
	 * @param header the text to be printed before the option
	 * 	descriptions
	 */

	public void setHeader(String header)
	{
		_preamble = header;
	}

	/**
	 * This method is used to set optional text which can be
	 * printed after the command option descriptions.
	 *
	 * @param footer the text to be printed after the option
	 * 	descriptions
	 */

	public void setFooter(String footer)
	{
		_postamble = footer;
	}

	/**
	 * This method allows the parser to be injected when
	 * we're registered.
	 */

	public void injectParser(CommandParser parser)
	{
		_parser = parser;
		_appname = _parser.getApplicationName();
		_arghelp = _parser.getArgumentHelp();
		_lswitch = _parser.getLongSwitch();
		_sswitch = _parser.getShortSwitch();
	}

	/**
	 * @return true if the program will exit on help or
	 * usage.
	 */

	public boolean getExitOnHelp()
	{
		return _exit;
	}

	/**
	 * Sets if the program will exit on help or usage.
	 *
	 * @param val true to make the program exit
	 */

	public void setExitOnHelp(boolean val)
	{
		_exit = val;
	}

	/**
	 * This method is a hook to allow for dynamic resource
	 * resolution at the time the help is printed.  This
	 * is designed to facilitate printing help in more
	 * than one language at a time.
	 * <p>
	 * This implementation does not provide any mapping by
	 * default because the preferred method is to
	 * establish the resource mappings when the objects
	 * are created.
	 * </p>
	 *
	 * @param key the resource key
	 * @return the resolved resource string
	 */

	protected String getResourceString(String key)
	{
		if(_resources != null)
			return _resources.getString(key);

		return Strings.get(key);
	}

	/**
	 * This method is used to resolve the resource strings
	 * and format them with the object list.
	 *
	 * @param key the resource key
	 * @param args the object arguments
	 * @return the message
	 */

	protected String formatResourceString(String key, Object[] args)
	{
		if(_resources != null)
			return _resources.format(key, args);

		return Strings.format(key, args);
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
					buf.append(getResourceString("sParserDefaultArg"));
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
				buf.append(getResourceString("lParserDefault"));
				
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

	/** the name of our application */
	private String		_appname;

	/** the help text for the unhandled arguments */
	private String		_arghelp;

	/** the short switch */
	private char		_sswitch;

	/** the long switch */
	private String		_lswitch;

	/** the preamble to print before the options */
	private String		_preamble = null;

	/** the postamble to print */
	private String		_postamble = null;

	/** the maximum width of the switch part */
	private final int	SWITCH_LENGTH = 35;

	/** track if we exit */
	private boolean		_exit = true;

	/** the command parser because we need the switches */
	private CommandParser	_parser = null;

	/** the resource loader to use (overriding the default) */
	private ResourceManager	_resources = null;

	/** our option listener delegate */
	private CommandListener	_delegate = null;
}
