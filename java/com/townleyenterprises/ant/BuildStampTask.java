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
// File:	BuildStampTask.java
// Created:	Sat Oct  1 14:43:44 IST 2005
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

/**
 * This class is used to provide automatic build counting,
 * useful in implementing nightly builds.
 *
 * @version $Id: BuildStampTask.java,v 1.1 2005/10/01 17:29:27 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class BuildStampTask extends Task
{
	public void setVersioninfo(String versioninfo)
	{
		_versioninfo = versioninfo;
	}

	public void setCount(boolean val)
	{
		_count = val;
	}

	public void setDevname(String name)
	{
		_devname = name;
	}

	public void setClean(boolean val)
	{
		_clean = val;
	}

	public void setDryrun(boolean val)
	{
		_dryrun = val;
	}

	public void addPath(Path path)
	{
		_stampPath.add(path);
	}

	public void setVerbose(boolean val)
	{
		_verbose = val;
	}

	public void setDateformat(String fmt)
	{
		_dateFormat = fmt;
	}

	public void setImport(boolean val)
	{
		_import = val;
	}

	public void setSuffix(String val)
	{
		_suffix = val;
	}

	private void addFilter(String key, String val)
	{
		StringBuffer buf = new StringBuffer(_delimiter);
		buf.append(key);
		buf.append(_delimiter);

		_props.addFilter(key, val);

		if(_verbose)
			log("Added filter:  " + buf + " => " + val);
	
		// make sure we inject the properties
		getProject().setNewProperty(key, val);
	}

	private void validate()
	{
		// there's nothing to do for a clean dependent
		// on the versioninfo information
		if(_clean) return;

		try
		{
			log("Using '" + _versioninfo + "' for version information.");

			Properties props = new Properties();
			props.load(new FileInputStream(_versioninfo));
			
			// build a filterset for each property
			// in the file
			_props = new FilterSet();

			for(Enumeration e = props.keys(); e.hasMoreElements();)
			{
				String key = (String)e.nextElement();
				String val = props.getProperty(key);
				if("build.version.count".equals(key) && _count)
				{
					// increment the count
					int count = Integer.parseInt(val);
					val = "" + ++count;
					props.setProperty(key, val);
				}
				else if("build.version.count".equals(key) && !_count)
				{
					val = _devname;
				}
				else if("build.version.date".equals(key))
				{
					// ignore it
					continue;
				}

				addFilter(key, val);
			}

			// add the date
			SimpleDateFormat fmt = new SimpleDateFormat(_dateFormat);
			String date = fmt.format(new Date());
			addFilter("build.version.date", date);
			props.setProperty("build.version.date", date);

			StringBuffer ver = new StringBuffer();
			ver.append(props.getProperty("build.version.major"));
			ver.append(".");
			ver.append(props.getProperty("build.version.minor"));
			ver.append(".");
			ver.append(props.getProperty("build.version.release"));
			
			// set the build.version property
			getProject().setNewProperty("build.version", ver.toString());

			if(!_dryrun)
			{
				props.store(new FileOutputStream(_versioninfo),
					" Official build file. DO NOT EDIT.");
			}
		}
		catch(IOException e)
		{
			throw new BuildException(e);
		}
	}

	public void execute()
	{
		validate();

		// if we only want to import, there's no other
		// processing to do
		if(_import) return;

		FilterSetCollection fsc = new FilterSetCollection(_props);
		FileUtils futl = FileUtils.newFileUtils();

		try
		{
			for(Iterator i = _stampPath.iterator(); i.hasNext();)
			{
				Path path = (Path)i.next();
				String[] files = path.list();
				for(int j = 0; j < files.length; ++j)
				{
					String src = files[j];
					
					// build the destination path
					StringBuffer dest = new StringBuffer(com.townleyenterprises.common.Path.pathname(src, File.separator));
					dest.append(File.separator);
					dest.append(com.townleyenterprises.common.Path.basename(src, File.separator, _suffix));
					
					StringBuffer msg = new StringBuffer();
					if(_clean)
					{
						msg.append("Deleting ");
						msg.append(dest);
					}
					else
					{
						msg.append("Processing ");
						msg.append(src);
					}

					log(msg.toString());

					if(_dryrun)
					{
						// don't actually do anything
						return;
					}
					
					if(_clean)
					{
						File file = new File(dest.toString());
						file.delete();
					}
					else
					{
						futl.copyFile(src,
							dest.toString(),
							fsc, true);
					}
				}
			}
		}
		catch(Exception e)
		{
			throw new BuildException(e);
		}
	}

	/** this is the file containing the version information */
	private String		_versioninfo = null;

	/** this flag controls if the count should be done or not */
	private boolean		_count = false;

	/** this is the name used for developer builds */
	private String		_devname = "DEVELOPER";

	/** this indicates if we should delete files */
	private boolean		_clean = false;

	/** this indicates if we are supposed to do something */
	private boolean		_dryrun = false;

	/** this tracks the files to be stamped */
	private List		_stampPath = new ArrayList();

	/** say to only set properties or not */
	private boolean		_import = false;

	/** this is the delimiter to use */
	private String		_delimiter = "@";

	/** this is the suffix to use */
	private String		_suffix = ".in";

	/** this controls the verbosity of the task */
	private boolean		_verbose = false;

	/** the properties to substitute */
	private FilterSet	_props = null;

	/** the date format string to use */
	private String		_dateFormat = "yyyy-MM-dd HH:mm:ss z";
}
