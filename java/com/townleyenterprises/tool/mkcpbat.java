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
// File:	mkcpbat.java
// Created:	Mon Feb  9 15:25:42 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.tool;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.townleyenterprises.command.AbstractCommandListener;
import com.townleyenterprises.command.CommandOption;
import com.townleyenterprises.command.CommandParser;
import com.townleyenterprises.common.Path;

/**
 * This is a simple utility to automatically generate an MS-DOS
 * compatible COMMAND.COM script used to set the classpath for your
 * environment.
 * <p>
 * The basic usage of this command is something like the following:
 * <pre>
 * C:\project&gt; java -cp te-common.jar com.townleyenterprises.tool.mkcpbat --outfile classpath.bat lib/*.jar
 * C:\project&gt; call classpath.bat
 * </pre>
 * </p>
 * <p>
 * The main motivation behind this tool is that there's no easy way to
 * do this with the built-in mechanisms of the OS.  For UNIX, you can
 * do the following instead:
 *
 * <pre>
 * $ for file in `pwd`/lib/*.jar;do CLASSPATH=$file:$CLASSPATH;export CLASSPATH; done
 * </pre>
 * </p>
 *
 * @version $Id: mkcpbat.java,v 1.4 2004/07/29 18:28:28 atownley Exp $
 * @author <a href="mailto:adz1092@nestscape.net">Andrew S. Townley</a>
 * @since 2.1
 */

public final class mkcpbat
{
	public static void main(String[] args)
	{
		new mkcpbat(args);
	}

	/**
	 * The constructor is the 'main' of the application.
	 *
	 * @param args the command-line arguments
	 */

	private mkcpbat(String[] args)
	{
		_parser = new CommandParser("mkcpbat",
				Strings.get("sFileArgs"));
		_parser.setExitOnMissingArg(true, -1);
		_parser.addCommandListener(new OptionHandler());
		_parser.setExtraHelpText(
			Strings.get("smkcpbatExtraHelpTextPreamble"),
			Strings.get("smkcpbatExtraHelpTextPostamble"));

		_parser.parse(args);

		if(_ofname.getMatched())
		{
			try
			{
				File ofname = new File(_ofname.getArg());
				if(ofname.exists() && _overwrite.getMatched())
				{
					if(_verbose.getMatched())
					{
						System.err.println(Strings.format("fDeleteExistingFile", new Object[] { ofname.getName() }));
					}

					ofname.delete();
				}

				_writer = new PrintStream(new FileOutputStream(_ofname.getArg()));
			}
			catch(IOException e)
			{
				System.err.println(Strings.format("fGeneralError", new Object[] { e.getMessage() }));

				if(_verbose.getMatched())
				{
					e.printStackTrace();
				}
				System.exit(-1);
			}
		}

		String[] largs = _parser.getUnhandledArguments();
		if(largs.length == 0)
		{
			System.err.println(Strings.get("sErrorNoArgsExit"));
			_parser.usage();
			System.exit(-1);
		}

		processArgs(largs);
	}

	/**
	 * This method actually generates the script output for the
	 * given input arguments.  This method depends on the shell
	 * expanding the command-line wildcards.
	 *
	 * @param args the arguments to process
	 */

	private void processArgs(String[] args)
	{
		boolean verbose = _verbose.getMatched();

		// disable console echo
		_writer.println("@echo off");

		for(int i = 0; i < args.length; ++i)
		{
			try
			{
				// try and expand any wildcards not handled by DOS/Windows
				String[] xargs = expandArg(args[i]);

				for(int j = 0; j < xargs.length; ++j)
				{
					if(verbose)
					{
						System.err.println(Strings.format("fProcessArg", new Object[] { xargs[j] }));
					}

					File pc = new File(xargs[j]);
					_writer.print("set CLASSPATH=%CLASSPATH%;");
					_writer.println(pc.getCanonicalPath());
				}
			}
			catch(IOException e)
			{
				System.err.println(Strings.format("fGeneralError", new Object[] { e.getMessage() }));
				
				if(verbose)
				{
					e.printStackTrace();
				}

				System.exit(-1);
			}
		}
	}

	/**
	 * This is a method which attempts to expand the path given it if
	 * it contains the standard wildcards <code>*</code> or
	 * <code>?</code>.
	 *
	 * @param path some sort of path
	 * @return the expanded list of names
	 * @exception IOException
	 * 	if anything happens resolving the path
	 */

	private String[] expandArg(String arg) throws IOException
	{
		boolean verbose = _verbose.getMatched();

		if(verbose)
			System.err.println(Strings.format("fExpandArg", new Object[] { arg }));

		int idxs = arg.indexOf("*");
		int idxq = arg.indexOf("?");

		if(idxs == -1 && idxs == -1)
		{
			if(verbose)
				System.err.println(Strings.format("fNoExpand", new Object[] { arg }));

			return new String[] { arg };
		}

		// if we get here, we need to perform some regex magic.  Note,
		// we're not going to support full filename expansion, but we
		// will take care of the simple cases
		String bname = Path.basename(arg, File.separator);
		String dirname = Path.dirname(arg);

		if(verbose)
		{
			System.err.println(Strings.format("fBasename", new Object[] { arg, bname }));
			System.err.println(Strings.format("fDirname", new Object[] { arg, dirname }));
		}

		idxs = dirname.indexOf("*");
		idxq = dirname.indexOf("?");
		if(idxq != -1 || idxs != -1)
		{
			System.err.println(Strings.get("sErrorNoDirExpand"));
			System.exit(-1);
		}

		bname = bname.replaceAll("\\*", ".*");
		bname = bname.replaceAll("\\?", ".");
		bname = bname.toLowerCase();

		File dir = new File(dirname);
		return dir.list(new RegexFilenameFilter(bname));
	}

	/** our command parser */
	private CommandParser	_parser;

	/** the output writer to use */
	private PrintStream		_writer = System.out;

	/** specify the output file */
	private static CommandOption	_ofname = new CommandOption("outfile", (char)0, true, Strings.get("sFileOption"), Strings.get("sOfNameHelp"));

	private static CommandOption	_overwrite = new CommandOption("overwrite", (char)0, false, null, Strings.get("sOverwriteHelp"));

	private static CommandOption	_verbose = new CommandOption("verbose", (char)0, false, null, Strings.get("sVerboseHelp"));

	/** the command line options */
	private static CommandOption[]	_options = { _ofname, _overwrite, _verbose };

	/**
	 * This static class is responsible for handling the
	 * command-line arguments for the utility.
	 */

	private static class OptionHandler extends AbstractCommandListener
	{
		public String getDescription()
		{
			return Strings.get("sOptDescription");
		}

		public CommandOption[] getOptions()
		{
			return _options;
		}
	}

	/**
	 * This class is a regular expression filename filter.
	 */

	private static class RegexFilenameFilter implements FilenameFilter
	{
		public RegexFilenameFilter(String regex)
		{
			_regex = regex;
		}

		public boolean accept(File dir, String name)
		{
			return name.toLowerCase().matches(_regex);
		}
		
		private final String _regex;
	}
}
