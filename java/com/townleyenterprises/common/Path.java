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
// File:	Path.java
// Created:	Fri Jan 23 16:15:57 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

/**
 * This class provides methods equivalent to the UNIX commands
 * <code>dirname</code> and <code>basename</code> based on arbitrary
 * path separators.
 *
 * @version $Id: Path.java,v 1.2 2004/07/29 18:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@nestscape.net">Andrew S. Townley</a>
 * @since 2.1
 */

public final class Path
{
	/**
	 * This method is used to strip the path name and optional
	 * suffix from a given delimited string representing some kind
	 * of path.  It is useful for dealing with package names as
	 * well as regular filesystem paths.
	 *
	 * @param path the path string
	 * @param pd the path delimiter
	 * @param suffix the suffix to remove
	 * @return the base path component
	 */

	public static String basename(String path, String pd, 
					String suffix)
	{
		if(path == null || pd == null)
		{
			return null;
		}

		int idx = path.lastIndexOf(pd);
		if(idx < 0)
		{
			return path;
		}

		String pc = path.substring(idx+1);

		if(suffix != null)
		{
			// strip the suffix
			if(suffix.length() < pc.length())
				pc = pc.substring(0, 
					pc.length() - suffix.length());
		}

		return pc;
	}

	/**
	 * This version of the basename command exposes just the path
	 * and the delimiter.  Suffix stripping is not supported.
	 *
	 * @param path the path string
	 * @param pd the path delimiter
	 * @return the base path component
	 */

	public static String basename(String path, String pd)
	{
		return basename(path, pd, null);
	}

	/**
	 * This method is used to remove the package name from a Java
	 * class.
	 *
	 * @param name the Java class name
	 * @return the class name 
	 */

	public static String classname(String name)
	{
		return basename(name, ".", null);
	}

	/**
	 * This method returns the path portion of the delimited
	 * string similar to the UNIX dirname command, but for
	 * arbitrary delimiters.
	 *
	 * @param path the path string
	 * @param pd the path delimiter
	 * @return the path portion of the path
	 */

	public static String pathname(String path, String pd)
	{
		if(path == null || pd == null)
		{
			return null;
		}

		int idx = path.lastIndexOf(pd);
		if(idx < 0)
			return "";

		return path.substring(0, idx);
	}

	/**
	 * This method provides the exact functionality of the UNIX
	 * dirname command.  All of the non-directory suffix is
	 * stripped from the path, delimited by the <code>'/'</code>
	 * character.
	 *
	 * @param path the path string
	 * @return the directory name or <code>"."</code> if the path
	 * 	does not have a directory.
	 */

	public static String dirname(String path)
	{
		String s = pathname(path, "/");
		if(s.length() == 0)
		{
			return ".";
		}

		return s;
	}

	/**
	 * This method is used to strip the trailing "file extension"
	 * from the given string.
	 *
	 * @param s the string
	 * @return the base of s
	 */

	public static String stripExtension(String s)
	{
		int idx = s.lastIndexOf(".");
		if(idx == -1)
			return s;

		return s.substring(0, idx);
	}

	/**
	 * This method is used to get the "file extension" from the
	 * string.
	 *
	 * @param s the string
	 * @return the extension or an empty string if none
	 */

	public static String getExtension(String s)
	{
		int idx = s.lastIndexOf(".");
		if(idx == -1)
			return "";

		return s.substring(idx + 1);
	}

	// prevent instantiation
	private Path() {}
}
