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
// File:	VersionHelper.java
// Created:	Mon Jan  3 17:59:41 GMT 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.StringTokenizer;

/**
 * This class provides a container which is populated by instances and
 * referenced by a static wrapper deployed in each project.  It
 * provides the majority of the functionality present in the previous
 * version of the Version class, but it provides an easier way to
 * integrate version information into other projects.
 *
 * @version $Id: VersionHelper.java,v 1.1 2005/01/03 18:55:46 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class VersionHelper
{
	/**
	 * The constructor takes all of the information which is
	 * unique for a given version of a software package.
	 * Normally, these values are provided by the build system
	 * based on infrequently changed configuration files.
	 *
	 * @param project the project name
	 * @param major the project's major version
	 * @param minor the project's minor version
	 * @param release the project's release for a given major and
	 * 	minor version
	 * @param build the project's build number or type
	 * @param date the project's build date
	 */

	public VersionHelper(String project, int major, int minor,
			String release, String build, String date)
	{
		_project = project;
		_major = major;
		_minor = minor;
		_rel = release;
		_count = build;
		_date = date;
	}

	/**
	 * This method is used to retrieve the formatted version number
	 * suitable for displaying in about boxes, version outputs, log
	 * files, etc.
	 *
	 * @return the formatted version string
	 */

	public String getFullVersion()
	{
		return Strings.format("fFullVersion",
				new Object[] { getVersion(), getBuild() });
	}

	/**
	 * This method is used to just get the formatted version number
	 * without the build information.
	 *
	 * @return the version string
	 */

	public String getVersion()
	{
		StringBuffer buf = new StringBuffer();
		buf.append(_major);
		buf.append(".");
		buf.append(_minor);
		buf.append(".");
		buf.append(_rel);
		
		return buf.toString();
	}

	/**
	 * This method is used to just retrieve the build portion of the
	 * version number.
	 *
	 * @return the build information
	 */

	public String getBuild()
	{
		return Strings.format("fBuild", new Object[] { _count, _date });
	}

	/**
	 * This method is used to require the specific minimum
	 * version of the library be available.   If the requested
	 * version or newer is not available, an exception is thrown.
	 * <p>
	 * In this case, <strong><em>newer</em></strong> means
	 * numerically later with the special exception of DEVELOPER
	 * builds, which will automatically always succeed.  For
	 * prerelease builds, the <code>-pre#</code> suffix should be
	 * omitted and the build number should be used instead.
	 *
	 * @param rmaj the required major version
	 * @param rmin the required minor version
	 * @param rrel the required release number
	 * @param rbuild the required build number
	 * @exception VersionMismatchException
	 * 	if the requested version is not available
	 * @exception NumberFormatException
	 * 	if the required numeric values can't be converted to
	 * 	numbers
	 */

	public void require(int rmaj, int rmin,
				int rrel, int rbuild)
			throws VersionMismatchException
	{
		if(compare(_major, _minor, _rel, _count,
				rmaj, rmin,
				Integer.toString(rrel),
				Integer.toString(rbuild)) < 0)
		{
			// FIXME:  I know this is ugly, but...
			throw new VersionMismatchException(
				"" + rmaj + "." + rmin + "." + rrel +
				" (Build " + rbuild + ")",
				getFullVersion());
		}
	}

	/**
	 * This is an overloaded version of require which facilitates
	 * easier version checking.
	 *
	 * @param checkver the version of the required build
	 * @exception VersionMismatchException
	 * 	if the requested version is not available
	 * @exception NumberFormatException
	 * 	if the required numeric values can't be converted to
	 * 	numbers
	 */

	public void require(String checkver)
			throws VersionMismatchException
	{
		if(compare(_major, _minor, _rel, _count, checkver) < 0)
			throw new VersionMismatchException(checkver,
					getFullVersion());
	}

	/**
	 * This utility method is used to compare two sets of version
	 * values and report how they relate to each other.  If any of
	 * the version strings contain a release of
	 * <code>DEVELOPER</code>, that version will always be greater
	 * than the other, unless the value of the system property
	 * <code>te-common.version.developer-override</code> is
	 * <code>false</code>.  By default, the developer override
	 * functionality is present.
	 *
	 * @param maj1 the fist major version
	 * @param min1 the fist minor version
	 * @param rel1 the fist release number
	 * @param bld1 the fist build number
	 * @param maj2 the fist major version
	 * @param min2 the fist minor version
	 * @param rel2 the fist release number
	 * @param bld2 the fist build number
	 * @return &lt; 0 if the first is less than the second; 0 if
	 * 	they are equal or &gt; 0 if the first is greater than
	 * 	the second
	 */

	public int compare(int maj1, int min1, String rel1,
				String bld1, int maj2, int min2,
				String rel2, String bld2)
	{
		boolean override = true;
		String s = System.getProperty("te-common.version.developer-override");
		if(s != null && "false".equals(s))
		{
			override = false;
		}

		if("DEVELOPER".equals(bld1) && override)
		{
			System.err.println(Strings.get("sWarnDevOverride"));
			return 1;
		}
		else if("DEVELOPER".equals(bld1) && !override)
		{
			bld1 = "0";
		}
		else if("DEVELOPER".equals(bld2) && override)
		{
			System.err.println(Strings.get("sWarnDevOverride"));
			return -1;
		}
		else if("DEVELOPER".equals(bld2) && !override)
		{
			bld2 = "0";
		}

		int irel1 = getReleaseNumber(rel1);
		int ibld1 = (bld1 == null) ? 0 : Integer.parseInt(bld1);
		
		int irel2 = getReleaseNumber(rel2);
		int ibld2 = (bld2 == null) ? 0 : Integer.parseInt(bld2);
	
		// perform the check
		int diff = maj1 - maj2;
		if(diff != 0)
			return diff;

		diff = min1 - min2;
		if(diff != 0)
			return diff;

		diff = irel1 - irel2;
		if(diff != 0)
			return diff;

		return ibld1 - ibld2;
	}

	/**
	 * This version of compare deals with arrays of 4 string
	 * values (as parsed by the parse method) instead of the 8
	 * string values.
	 *
	 * @param ver1 the string array for version 1
	 * @param ver2 the string array for version 2
	 * @return &lt; 0 if the first is less than the second; 0 if
	 * 	they are equal or &gt; 0 if the first is greater than
	 * 	the second
	 */

	public int compare(String[] ver1, String[] ver2)
	{
		return compare(Integer.parseInt(ver1[0]), 
				Integer.parseInt(ver1[1]),
				ver1[2], ver1[3],
				Integer.parseInt(ver2[0]),
				Integer.parseInt(ver2[1]),
				ver2[2], ver2[3]);
	}

	/**
	 * This version of compare deals with two version strings which
	 * are expected to be in the format provided by either {@link
	 * #getVersion} or {@link #getFullVersion}.
	 *
	 * @param verstr1 the first version string
	 * @param verstr2 the second version string
	 * @return &lt; 0 if the first is less than the second; 0 if
	 * 	they are equal or &gt; 0 if the first is greater than
	 * 	the second
	 */

	public int compare(String verstr1, String verstr2)
	{
		return compare(parse(verstr1), parse(verstr2));
	}

	/**
	 * This version of the compare method is used to support the
	 * second require method.
	 *
	 * @param maj1 the fist major version
	 * @param min1 the fist minor version
	 * @param rel1 the fist release number
	 * @param bld1 the fist build number
	 * @param verstr the string to compare against
	 * @return &lt; 0 if the first is less than the second; 0 if
	 * 	they are equal or &gt; 0 if the first is greater than
	 * 	the second
	 */

	private int compare(int maj1, int min1,
			String rel1, String bld1, String verstr)
	{
		String[] v = parse(verstr);
		return compare(maj1, min1, rel1, bld1,
			Integer.parseInt(v[0]), Integer.parseInt(v[1]),
			v[2], v[3]);
	}

	/**
	 * This method is used to parse the supplied string into an
	 * array of String objects representing the version in the
	 * format:
	 * <code><em>major</em>.<em>minor</em>.<em>release</em> (Build
	 * <em>build</em>[; <em>date</em>])</code>.  If it doesn't
	 * match this format,
	 * an empty array is returned.
	 *
	 * @param verstr the version string
	 * @return the array containing the individual values
	 */

	public String[] parse(String verstr)
	{
		// FIXME:  should use the parse capabilities...
		
		String[] rval = new String[4];

		// first parse the version
		int idx = verstr.indexOf(" ");
		String sstr = verstr.substring(0,idx);
		StringTokenizer st = new StringTokenizer(sstr, ".");
		rval[0] = st.nextToken();
		rval[1] = st.nextToken();
		rval[2] = st.nextToken();

		// now, grab the build number
		sstr = verstr.substring(idx+1);
		idx = sstr.indexOf(";");
		if(idx == -1)
		{
			// no date
			rval[3] = sstr.substring(sstr.indexOf(" ") + 1,
				sstr.length() - 1);
		}
		else
		{
			rval[3] = sstr.substring(sstr.indexOf(" ") + 1, idx);
		}

		return rval;
	}

	/**
	 * This method returns the integer value of a release number.
	 *
	 * @param release the release string
	 */

	private int getReleaseNumber(String release)
	{
		if(release == null)
			return 0;

		int rel = 0;

		int didx = release.indexOf("-");
		if(didx != -1)
			rel = Integer.parseInt(release.substring(0, didx));
		else
			rel = Integer.parseInt(release);

		return rel;
	}

	/**
	 * This method prints the version information to
	 * <code>stdout</code>.
	 */

	public void printVersion()
	{
		System.out.print(_project);
		System.out.print(": ");
		System.out.println(getFullVersion());
	}

	/** the project name */
	private final String	_project;
	
	/** the major version */
	private final int	_major;

	/** the minor version */
	private final int	_minor;

	/** the release number */
	private final String	_rel;

	/** the build number or type */
	private final String	_count;
	
	/** the build date */
	private final String	_date;
}
