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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.townleyenterprises.command.AbstractCommandListener;
import com.townleyenterprises.command.CommandOption;
import com.townleyenterprises.command.CommandParser;

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
 * @version $Id: mkcpbat.java,v 1.1 2004/02/09 16:07:25 atownley Exp $
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
		_parser = new CommandParser("mkcpbat", "FILE...");
		_parser.setExitOnMissingArg(true, -1);
		_parser.addCommandListener(new OptionHandler());

		_parser.parse(args);

		if(_ofname.getMatched())
		{
			try
			{
				_writer = new PrintStream(new FileOutputStream(_ofname.getArg()));
			}
			catch(IOException e)
			{
				System.err.println("error:  " + e.getMessage());
				System.exit(-1);
			}
		}

		String[] largs = _parser.getUnhandledArguments();
		if(largs.length == 0)
		{
			System.err.println("error:  no arguments specified.  Exiting.");
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
		for(int i = 0; i < args.length; ++i)
		{
			try
			{
				File pc = new File(args[i]);
				_writer.print("set CLASSPATH=%CLASSPATH%;");
				_writer.println(pc.getCanonicalPath());
			}
			catch(IOException e)
			{
				System.err.println("error:  " + e.getMessage());
				System.exit(-1);
			}
		}
	}

	/** our command parser */
	private CommandParser	_parser;

	/** the output writer to use */
	private PrintStream		_writer = System.out;

	/** specify the output file */
	private static CommandOption	_ofname = new CommandOption("outfile", (char)0, true, "FILE", "place the batch commands in the named file rather than to the console");

	/** the command line options */
	private static CommandOption[]	_options = { _ofname };

	/**
	 * This static class is responsible for handling the
	 * command-line arguments for the utility.
	 */

	private static class OptionHandler extends AbstractCommandListener
	{
		public String getDescription()
		{
			return "mmcpbat options";
		}

		public CommandOption[] getOptions()
		{
			return _options;
		}
	}
}
