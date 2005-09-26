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
// File:	test1.java
// Created:	Mon Sep 26 00:00:44 IST 2005
//
//////////////////////////////////////////////////////////////////////

// now, you wouldn't do this in actual code, would you???
import com.townleyenterprises.command.*;

import java.util.Iterator;

class test2
{
	private CommandOption _one = new CommandOption("one", '1', false, null, "option one description");
	private CommandOption _two = new CommandOption("two", '2', false, null, "option two description");
	private CommandOption _arg = new CommandOption("arg", 'A', true, "ARG", "option arg description");
  private CommandOption _joined = new JoinedCommandOption('D', false, "KEY=VALUE[,KEY=VALUE...]", "joined description", true);
	
	private CommandParser _parser = null;
	private CommandOption[] _options = { _one, _two, _arg, _joined };

	public static void main(String[] args)
	{
		new test2(args);
	}
	
	private test2(String[] args)
	{
		_parser = new CommandParser("test1", "FILE...");
		_parser.addCommandListener(
			new DefaultCommandListener("options", _options));

		_parser.parse(args);

		_parser.addConstraint(
			new MutexOptionConstraint(2, _one, _two));
		_parser.addConstraint(
			new RequiresAnyOptionConstraint(3, _arg, 
				new CommandOption[] { _one, _two }));
		
		try
		{
			_parser.executeCommands();
		}
		catch(Exception e)
		{
			System.exit(4);
		}
	}
}
