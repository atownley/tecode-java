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
// Name:	TraceCore.java
// Created:	Thu Dec  4 22:42:49 GMT 2003
//
///////////////////////////////////////////////////////////////////////

package com.townleyenterprises.trace;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * This class represents the core of the original ErrorTrace class and
 * contains the basic management of the trace facilities.
 * Additionally, since it is being essentially reimplemented (again),
 * it now supports a lot more fine-grained configuration options to
 * allow the output to be controlled at run-time.
 *
 * @version $Id: TraceCore.java,v 1.1 2004/11/27 17:32:50 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public class TraceCore
{
	/**
	 * The constructor restricts creation of direct instances of
	 * the class except by subclasses.  For each instance created,
	 * it is automatically added to the map of configured
	 * instances so that finer control of the trace output can be
	 * provided.
	 *
	 * @param name the name of the class being traced
	 * @param maturity the maturity level of the class being
	 * 	traced
	 */

	protected TraceCore(String name, int maturity)
	{
		_tracers.put(name, this);
		
		_className = name;
		_cnhash = new Integer(_className.hashCode());
		_maturity = maturity;

		loadInstanceSettings(name, _props);
	}

	/**
	 * This method is used to print the exception's stack trace to
	 * the trace log.
	 *
	 * @param threshold the trace threshold to print
	 * @param ex the exception to dump
	 */

	public synchronized void printStackTrace(int threshold, Throwable ex)
	{
		if(willTrace(threshold))
		{
			_ts.printStackTrace(ex);
		}
	}

	/**
	 * This method is a generic method that is used by all of the
	 * other, simpler methods.  If the current trace level value
	 * is greater or equal to the maturity level * 10 plus the
	 * threshold level, the message will be printed.
	 *
	 * @param threshold the threshold at which the message should
	 * 	be traced
	 * @param fmt the message format string
	 * @param args any arguments to the message format
	 */

	public synchronized void tprintln(int threshold, String fmt, Object[] args)
	{
		if(!willTrace(threshold))
			return;

		if(_showts)
		{
			Date d = new Date();
			_ts.print(_dateFormatter.format(d) + " ");
		}

		// only print the thread name if we're supposed to...
		String threadName = Thread.currentThread().getName();
		if(_showthreadalways || !("main".equals(threadName))) 
		{
			_ts.print(MessageFormat.format(TRACE_CLASS_THREAD_FMT,
				new Object[] { _className, 
					_cnhash, threadName }));
		}
		else
		{
			_ts.print(MessageFormat.format(TRACE_CLASS_FMT,
				new Object[] { _className, _cnhash }));
		}

		if(args != null)
			_ts.println(MessageFormat.format(fmt, args));
		else
			_ts.println(fmt);
	}

	/**
	 * This is a short-hand way to call the tprintln method if no
	 * arguments are needed.
	 *
	 * @param threshold the threshold at which the message should
	 * 	be traced
	 * @param fmt the message to be printed
	 */

	public void tprintln(int threshold, String fmt)
	{
		tprintln(threshold, fmt, null);
	}

	/**
	 * This method can be used to determine if trace messages will
	 * be printed for a certain threshold value.  This is
	 * especially useful at higher thresholds when dumping the
	 * contents of data structures.  To dump the contents of an
	 * array to the trace log, the following mechanism should be
	 * used:
	 * <pre>
	 * 	if(willTrace(10))
	 * 	{
	 * 		for(int i = 0; i < arr.length; ++i)
	 * 		{
	 * 			tprintln(10, "arr[" + i + "] = " + arr[i]);
	 * 		}
	 * 	}
	 * </pre>
	 */

	public boolean willTrace(int threshold)
	{
		int thresh = (_maturity * 10) + threshold;

		if(_ts == null)
			return false;

		// allow the instance to override
		if((_iTraceLevel != -1 && _iTraceLevel >= thresh) ||
			(_iTraceLevel == -1 && _traceLevel >= thresh))
		{
			return true;
		}

		return false;
	}

	/**
	 * This is the attribute accessor for the current global trace
	 * level for all instances.
	 *
	 * @return the current trace level
	 */

	public static int getTraceLevel()
	{
		return _traceLevel;
	}

	/**
	 * This is the attribute accessor to retrieve the current
	 * trace level for the given trace object.
	 *
	 * @param name the name of the trace object
	 * @return the trace level
	 */

	public static int getTraceLevel(String name)
	{
		TraceCore tc = null;

		synchronized(_tracers)
		{
			tc = (TraceCore)_tracers.get(name);
		}

		if(tc != null)
			return tc.getInstanceTraceLevel();
	
		return 0;
	}

	/**
	 * This method returns the trace level for this specific
	 * instance.
	 *
	 * @return the trace level
	 */

	public int getInstanceTraceLevel()
	{
		return _iTraceLevel;
	}

	/**
	 * This method is used to set the global trace level for all
	 * objects.
	 *
	 * @param l the new trace level
	 */

	public static synchronized void setTraceLevel(int l)
	{
		_traceLevel = l;
	}

	/**
	 * This method is used to set the specific trace level for the
	 * named trace object.
	 *
	 * @param name the named object
	 * @param l the new trace level
	 */

	public static void setTraceLevel(String name, int l)
	{
		TraceCore tc = null;

		synchronized(_tracers)
		{
			tc = (TraceCore)_tracers.get(name);
		}

		if(tc != null)
			tc.setInstanceTraceLevel(l);
	}

	/**
	 * This method is used to set the instance trace level for
	 * this object.
	 *
	 * @param l the new trace level
	 */

	public synchronized void setInstanceTraceLevel(int l)
	{
		_iTraceLevel = l;
	}

	/**
	 * This method returns a reference to the underlying trace
	 * PrintStream.
	 *
	 * @return the trace stream
	 */

	public static PrintStream getTraceStream()
	{
		return _ts.getPrintStream();
	}

	/**
	 * Accessor to tell if we're displaying the timestamp.
	 */

	public static synchronized boolean getShowTimestamp()
	{
		return _showts;
	}

	/**
	 * Setter for the timestamp.
	 */

	public static synchronized void setShowTimestamp(boolean x)
	{
		_showts = x;
	}

	/**
	 * Accessor to tell if we're displaying the thread name.
	 */

	public static synchronized boolean getShowThreadNameAlways()
	{
		return _showthreadalways;
	}

	/**
	 * Setter for showing the thread name.
	 */

	public static synchronized void setShowThreadNameAlways(boolean x)
	{
		_showthreadalways = x;
	}

	/**
	 * Accessor for the timestamp format string.
	 */

	public static String getTimestampFormat()
	{
		return _dateFormatter.toPattern();
	}

	public static synchronized void setTimestampFormat(String fmt)
	{
		_dateFormatter.applyPattern(fmt);
	}

	/**
	 * This is the attribute accessor for the current trace file
	 * name.
	 *
	 * @return the current trace file name
	 */

	public static String getTraceFile()
	{
		return _traceFileName;
	}

	/**
	 * This method is used to set the global trace file name for
	 * all objects.
	 *
	 * @param filename the name of the trace file
	 * @param append true to append to existing file
	 */

	public static synchronized void setTraceFile(String filename,
							boolean append)
	{
		if(_ts != null)
		{
			_ts.close();
		}

		if("".equals(filename))
		{
			_ts = SystemTraceStream.getInstance();
			return;
		}

		try
		{
			_ts = new FileTraceStream(filename, append);
			_traceFileName = filename;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			_ts = SystemTraceStream.getInstance();
		}
	}

	/**
	 * This method is used to load all of the configuration
	 * settings for ErrorTrace from the specified set of
	 * Properties.
	 *
	 * @param props the properties to use to configure the trace
	 * utility.
	 */

	public static void loadSettings(Properties props)
	{
		synchronized(_props)
		{
			if(!_props.equals(props))
			{
				_props.putAll(props);
			}
		}

		String s = props.getProperty("errortrace.tracefile");
		if(s != null)
		{
			String	a = props.getProperty("errortrace.append");
			boolean	append = true;

			if(a != null)
			{
				char c = a.toLowerCase().charAt(0);
				if(c != 'y' && c != 't')
					append = false;
			}
			setTraceFile(s, append);
		}

		s = props.getProperty("errortrace.showts");
		if(s != null)
		{
			char c = s.toLowerCase().charAt(0);
			if(c != 'y' && c != 't')
				setShowTimestamp(false);
		}
		s = props.getProperty("errortrace.showthreadalways");
		if(s != null)
		{
			char c = s.toLowerCase().charAt(0);
			if(c != 'y' && c != 't')
				setShowThreadNameAlways(false);
		}
		s = props.getProperty("errortrace.tsfmt");
		if(s != null)
		{
			setTimestampFormat(s);
		}

		s = props.getProperty("errortrace.tracelevel");
		if(s != null)
		{
			setTraceLevel(Integer.parseInt(s));
		}

		// take care of the instances
		Set keys = _tracers.keySet();
		for(Iterator i = keys.iterator(); i.hasNext();)
		{
			String key = (String)i.next();
			TraceCore tc = (TraceCore)_tracers.get(key);
			tc.loadInstanceSettings(key, props);
		}
	}

	/**
	 * This method is used to allow the instance to load the
	 * settings once it has been created.  Each instance can have
	 * its own trace level independently controlled.
	 *
	 * @param props the properties
	 */

	public void loadInstanceSettings(String name, Properties props)
	{
		String pname = "errortrace." + name + ".tracelevel";
		String s = props.getProperty(pname);

		if(s == null)
		{
			pname = name.concat(".tracelevel");
			s = props.getProperty(pname);
		}

		if(s != null)
		{
			setInstanceTraceLevel(Integer.parseInt(s));
		}
	}

	/**
	 * This method is responsible for handing the bootstrap
	 * configuration of the ErrorTrace system.  The default load
	 * order of the properties are:
	 *
	 * <ol>
	 * <li>The system properties</li>
	 * <li>properties file (-Derrortrace.properties=filename)</li>
	 * <li>/errortrace.properties in the classpath</li>
	 * </ol>
	 */

	private synchronized static void initialize()
	{
		if(_init)
			return;

		_props = new Properties();

		try
		{
			// load in reverse order
			InputStream is = ErrorTrace.class.getResourceAsStream("/errortrace.properties");
			if(is != null)
			{
				_props.load(is);
			}

			// check for the errortrace.properties in the system
			String s = System.getProperty("errortrace.properties");
			if(s != null)
			{
				is = new FileInputStream(s);
				_props.load(is);
			}

			// load from the command-line
			_props.putAll(System.getProperties());

			// take care of setting them all
			loadSettings(_props);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/** the default date format string */
	private static final String		TRACE_DATE_FMT = "yyyy-MM-dd HH:mm:ss.SSS z";

	/** format string for the thread */
	private static final String		TRACE_CLASS_THREAD_FMT = "{0}[{1,number,#}:{2}] ";

	/** format string for the thread */
	private static final String		TRACE_CLASS_FMT = "{0}[{1,number,#}] ";

	/** this is the name of the trace file */
	private static String			_traceFileName = "";

	/** the trace stream for the trace messages */
	private static TraceStream		_ts = SystemTraceStream.getInstance();

	/** the trace level for all classes */
	private static int			_traceLevel;

	/** the date formatter we're going to use */
	private static SimpleDateFormat		_dateFormatter = new SimpleDateFormat(TRACE_DATE_FMT);

	/** our static properties */
	private static Properties		_props;

	/** if we're supposed to show the date */
	private static boolean			_showts = true;

	/** if we're supposed to always show the thread */
	private static boolean			_showthreadalways = false;

	/** if we've been initialized */
	private static boolean			_init = false;

	/** this map keeps track of all the trace instances */
	private static Map			_tracers = new HashMap();
	
	/** the maturity level for the class being traced */
	private int				_maturity;

	/** the name of the class being traced */
	private String				_className;

	/** save the hash code for the class name string */
	private final Integer			_cnhash;

	/** the instance trace level for this instance */
	private int				_iTraceLevel = -1;

	static
	{
		initialize();
	}
}
