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
// File:	feather3.java
// Created:	Fri Jul 30 16:26:31 IST 2004
//
//////////////////////////////////////////////////////////////////////

// now, you wouldn't do this in actual code, would you???
import com.townleyenterprises.command.*;

import java.util.Iterator;

/**
 * This is an example of a hypothetical file archive program roughly
 * based on the UNIX tar command.  It is intended to illustrate the
 * proper use of the CommandParser and the command package.
 *
 * @version $Id: feather3.java,v 1.1 2004/11/28 20:17:10 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 3.0
 */

public class feather3
{
	private CommandParser _parser = null;

	private CommandOption _create = new CommandOption("create",
					'c', false, null,
					"create a new archive") {
		public void execute() throws Exception
		{
			String[] largs = _parser.getUnhandledArguments();

			if(largs.length == 0)
			{
				System.err.println("error:  refusing to create empty archive.");
				_parser.usage();
				System.exit(-2);
			}

			if(_verbose.getMatched() && _file.getMatched())
			{
				System.out.println("creating archive " + _file.getArg());
			}

			for(int i = 0; i < largs.length; ++i)
			{
				if(_verbose.getMatched())
					System.out.println("adding " + largs[i]);
			}

			if(_xclude.getMatched())
			{
				if(_verbose.getMatched())
				{
					for(Iterator i = _xclude.getArgs().iterator(); i.hasNext();)
					{
						System.out.println("excluded " + i.next());
					}
				}
			}
		}
	};
	
	private CommandOption _extract = new CommandOption(
				"extract",
				'x', 
				false,
				null,
				"extract files from the named archive");
	
	private CommandOption _file = new CommandOption(
				"file",
				'f',
				true,
				"ARCHIVE",
				"specify the name of the archive (default is stdout)");
	
	private CommandOption _verbose = new CommandOption(
				"verbose",
				'v',
				false,
				null,
				"print status information during execution");

	private RepeatableCommandOption _xclude = new RepeatableCommandOption(
				"exclude",
				'X',
				"[ FILE | DIRECTORY ]",
				"exclude the named file or directory from the archive");

	private PosixCommandOption _display = new PosixCommandOption(
				"display",
				true,
				"DISPLAY",
				"specify the display on which the output should be written");

	private JoinedCommandOption _options = new JoinedCommandOption(
				'D',
				false,
				"PROPERTY=VALUE[,PROPERTY=VALUE...]",
				"set specific run-time properties",
				true);

	private CommandOption[] _mainopts = {
				_create, _extract, _file, _verbose, _xclude };

	private CommandOption[] _examples = { _display, _options };

	public static void main(String[] args)
	{
		new feather3(args);
	}
	
	private feather3(String[] args)
	{
		_parser = new CommandParser("feather", "FILE...");
		_parser.setExitOnMissingArg(true, -10);

		// this is ugly and you wouldn't do this in real code,
		// but it serves to illustrate the method call
		_parser.setExtraHelpText("This is the TE-Code feather program.  It is used to illustrate the features of the com.townleyenterprises.command package.\n\nExamples:\n  # create archive.feather from files one, two, three and four\n  feather -cvf archive.feather one two three four\n\n  # exclude files five and six from an archive\n  feather -cvf archive.feather -X five -X six one two three\n\nAll options are not required unless otherwise stated in the description.", "This utility does not actually create an archive.\nAny bugs in the software should be reported to the te-code mailing lists.\n\nhttp://te-code.sourceforge.net");

		_parser.addCommandListener(
			new DefaultCommandListener("feather options", _mainopts));
		_parser.addCommandListener(
			new DefaultCommandListener("Example options", _examples));

		_parser.parse(args);

		_parser.addConstraint(
			new MutexOptionConstraint(-500, _create, _extract));
		_parser.addConstraint(
			new RequiresAnyOptionConstraint(-501, _file, 
				new CommandOption[] { _create, _extract }));
		_parser.addConstraint(new RequiresAnyOptionConstraint(-502,
			_xclude, new CommandOption[] { _create, _extract }));

		try
		{
			_parser.executeCommands();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-111);
		}
	}
}
