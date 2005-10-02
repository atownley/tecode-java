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
// File:	LocalizedCommandListener.java
// Created:	Mon Jul 19 17:35:58 GMTDT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.command;

import com.townleyenterprises.common.InjectResourceManager;
import com.townleyenterprises.common.ResourceManager;

/**
 * This class provides a common locale for the command
 * listener as well as for all of the options managed by it.
 * It will not localize the short or long names because, in
 * nearly all cases, you want your scripts to work regardless
 * of what locale is currently set.  This behavior is present
 * in the standard UNIX tools who support long options.
 *
 * @version $Id: LocalizedCommandListener.java,v 1.1 2005/10/02 00:02:29 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class LocalizedCommandListener extends DefaultCommandListener
{
	/**
	 * This is an option decorator to pull back the given
	 * option description and help using a common
	 * ResourceManager that the programmer does not have
	 * to manage themselves.
	 */

	private static class OptionDecorator extends CommandOption
	{
		public OptionDecorator(ResourceManager resources,
				CommandOption option)
		{
			super(option.getLongName(),
				option.getShortName().charValue(),
				option.getExpectsArgument(),
				resources.getString(option.getHelp()),
				resources.getString(option.getDescription()),
				option.getArgumentDefault());
			_option = option;
		}

		public void execute() throws Exception
		{
			_option.execute();
		}

		public Object getArgValue()
		{
			return _option.getArgValue();
		}

		public boolean getMatched()
		{
			return _option.getMatched();
		}

		public void optionMatched(String val)
		{
			_option.optionMatched(val);
		}

		public void reset()
		{
			_option.reset();
		}

		private final CommandOption	_option;
	}

	/**
	 * The constructor is used to supply the arguments for the
	 * listener.
	 *
	 * @param res the ResourceManager with the resource
	 * map for this listener and its options
	 * @param key the resource key for the description
	 * @param opts the options
	 */

	public LocalizedCommandListener(ResourceManager res,
				String key, CommandOption[] opts)
	{
		super(res.getString(key), null);
		_options = new CommandOption[opts.length];
		for(int i = 0; i < _options.length; ++i)
		{
			if(opts[i] instanceof InjectResourceManager)
			{
				InjectResourceManager irm;
				irm = (InjectResourceManager)opts[i];
				_options[i] = opts[i];
				irm.injectResourceManager(res);
			}
			else
			{
				_options[i] = new OptionDecorator(res, opts[i]);
			}
		}
	}

	/**
	 * This method is called by the CommandParser to determine all
	 * of the arguments that should be handled by the listener.
	 *
	 * @return an array of CommandOption arguments.  In the case
	 * 	where no arguments are specified (why would you
	 * 	bother?), a zero-length array should be returned.
	 */

	public CommandOption[] getOptions()
	{
		return _options;
	}

	private final CommandOption[]	_options;
}
