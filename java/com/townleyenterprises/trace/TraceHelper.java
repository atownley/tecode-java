//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2001-2003, Andrew S. Townley
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
// Name:	TraceHelper.java
// Author:	Andrew S. Townley <adz1092 at netscape dot net>
// Created:	Tue Apr  8 15:22:15 IST 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * This class allows generating standard trace representations of
 * various JDK data structures.
 *
 * @since 2.3
 */

public final class TraceHelper
{
	public static Object trace(Map m)
	{
		return trace(m, DEFAULT_SEP);
	}

	/**
	 * This overloaded method takes the object separator string.
	 *
	 * @since 3.0
	 */

	public static Object trace(Map m, String sep)
	{
		TraceMessage buf = new TraceMessage("{ ");
	
		Set keys = m.keySet();
		for(Iterator i = keys.iterator(); i.hasNext(); )
			{
			Object key = i.next();
			if(key instanceof String)
				{
				buf.append("'");
				buf.append(key);
				buf.append("'");
				}
			else
				{
				buf.append(key);
				}
			buf.append(": ");

			Object val = m.get(key);
			if(val instanceof String)
				{
				buf.append("'");
				buf.append(val);
				buf.append("'");
				}
			else
				{
				buf.append(val);
				}

			if(i.hasNext())
				buf.append(sep);
			}
		buf.append(" }");

		return buf;
	}

	/**
	 * Generates a representation of the list using the trace string
	 * rather than toString for all list objects.
	 *
	 * @since 3.0
	 */

	public static Object trace(List l)
	{
		return trace(l, DEFAULT_SEP);
	}

	/**
	 * Overloaded method takes the object separator string
	 */

	public static Object trace(List l, String sep)
	{
		TraceMessage msg = new TraceMessage("[");
		for(Iterator i = l.iterator(); i.hasNext();)
		{
			msg.append(i.next());
			if(i.hasNext())
			{
				msg.append(sep);
			}
		}

		msg.append("]");
		return msg.toString();
	}

	/**
	 * Generates a representation of the set using the trace string
	 * rather than toString for all list objects.
	 *
	 * @since 3.0
	 */

	public static Object trace(Set s)
	{
		return trace(s, DEFAULT_SEP);
	}

	public static Object trace(Set s, String sep)
	{
		TraceMessage msg = new TraceMessage("((");
		for(Iterator i = s.iterator(); i.hasNext();)
		{
			msg.append(i.next());
			if(i.hasNext())
			{
				msg.append(sep);
			}
		}

		msg.append("))");
		return msg.toString();
	}

	private static final String DEFAULT_SEP = ", ";
}
