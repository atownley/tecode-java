//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003, Andrew S. Townley
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
// File:	AbstractMonitoredTask.java
// Created:	Thu Nov 20 15:03:08 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.townleyenterprises.swing.event.TaskEvent;
import com.townleyenterprises.swing.event.TaskListener;

/**
 * This is an implementation of the MonitoredTask interface which
 * requires only that a single method be implemneted.
 *
 * @since 2.1
 * @version $Id: AbstractMonitoredTask.java,v 1.4 2003/11/27 00:03:03 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public abstract class AbstractMonitoredTask implements MonitoredTask
{
	/**
	 * This method returns true if the task has received a request
	 * to be cancelled.
	 *
	 * @return true if should stop; false otherwise
	 */

	public boolean shouldStop()
	{
		return _shouldStop;
	}

	/**
	 * This method returns true if the task has completed.
	 *
	 * @return true if done; false if not
	 */

	public boolean isComplete()
	{
		return _done;
	}

	/**
	 * This method returns true if the task encountered any errors.
	 *
	 * @return true if errors; false if no
	 */

	public boolean hasError()
	{
		if(_throwable != null)
			return true;

		return false;
	}

	/**
	 * This method returns the length of the current task.
	 *
	 * @return an integer indicating the length of the task
	 */

	public int getTaskLength()
	{
		return _length;
	}

	/**
	 * This method is used to return a reference to the throwable
	 * encountered during the performing of the task.
	 *
	 * @return the Throwable
	 */

	public Throwable getError()
	{
		return _throwable;
	}

	/**
	 * This method returns the current progress of the task.  In
	 * cases where the current progress is unknown, this method
	 * can indicate this state by returning a value < 0.
	 *
	 * @return positive integer < the task length or -1 if unknown
	 */

	public int getCurrentProgress()
	{
		return _progress;
	}

	/**
	 * This method is used to return the last status message
	 * associated with the task.  Useful for tasks that perform a
	 * number of steps.
	 *
	 * @return the message
	 */

	public String getStatus()
	{
		return _status;
	}

	/**
	 * This method is used to start the task.
	 */

	public final void start()
	{
		final SwingWorker worker = new SwingWorker() {
			public Object construct()
			{
				setProgress(0);
				setStatus("");
				setComplete(false);
				_shouldStop = false;
				initialize();
				return performTask();
			}

			public void finished()
			{
				fireTaskCompleted();
			}
		};
		worker.start();
		fireTaskStarted();
	}

	/**
	 * This method is used to request that the task be stopped.
	 */

	synchronized public void requestStop()
	{
			_shouldStop = true;
	}

	/**
	 * This method is used to register a new task listener.
	 *
	 * @param listener the listener to add
	 */

	public void addTaskListener(TaskListener listener)
	{
		if(listener == null)
			return;

		_listeners.add(listener);
	}

	/**
	 * This method is used to return the list of task listeners.
	 *
	 * @return the list of listeners
	 */

	public List getTaskListeners()
	{
		return Collections.unmodifiableList(_listeners);
	}

	/**
	 * This method is used to remove a specific listener.
	 *
	 * @param listener the listener to remove
	 */

	public void removeTaskListener(TaskListener listener)
	{
		if(listener == null)
			return;

		_listeners.remove(listener);
	}

	/**
	 * This method can be overridden to add extra initialization prior
	 * to the iterations performed by the thread.
	 */

	protected void initialize()
	{
	}

	/**
	 * This method is the actual re-entrant method that is called
	 * for each chunk of work that should be done.  This method
	 * should not take a long time to return and should make calls
	 * to the protected methods to update status.
	 *
	 * @param stop indicates that the task should stop
	 * @return the current amount of progress made this iteration
	 */

	protected abstract int performWork(boolean stop);

	/**
	 * This method manages the concrete task.
	 */

	private Object performTask()
	{
		while(!isComplete() && !hasError())
		{
			_progress += performWork(shouldStop());
			if(getCurrentProgress() >= getTaskLength())
			{
				setComplete(true);
			}
		}

		setProgress(0);
		setStatus("");
		return this;
	}

	/**
	 * This method is used to indicate that the task is complete
	 * and should be stopped.
	 *
	 * @param val true to indicate task is finished
	 */

	synchronized protected void setComplete(boolean val)
	{
		_done = val;
	}

	/**
	 * This method is used to set the error encountered by the
	 * task.
	 *
	 * @param error the throwable
	 */

	synchronized protected void setError(Throwable error)
	{
		_throwable = error;
	}

	/**
	 * This method is used to set the progress of the current
	 * task.
	 *
	 * @param progress the new value
	 */

	synchronized protected void setProgress(int progress)
	{
		_progress = progress;
	}

	/**
	 * This method is used to set the length of the task.
	 *
	 * @parm length the new length
	 */

	synchronized protected void setTaskLength(int length)
	{
		_length = length;
	}

	/**
	 * This method is used to set the status text
	 *
	 * @param text the text
	 */

	synchronized protected void setStatus(String s)
	{
		_status = s;
	}

	/**
	 * This method is used to fire the task started event.
	 */

	protected void fireTaskStarted()
	{
		TaskEvent te = new TaskEvent(this,
				getStatus(), getCurrentProgress());

		for(Iterator i = _listeners.iterator(); i.hasNext();)
		{
			TaskListener tl = (TaskListener)i.next();
			tl.taskStarted(te);
		}
	}

	/**
	 * This method is used to fire the task aborted event.
	 */

	protected void fireTaskAborted()
	{
		TaskEvent te = new TaskEvent(this,
				getStatus(), getCurrentProgress());
		for(Iterator i = _listeners.iterator(); i.hasNext();)
		{
			TaskListener tl = (TaskListener)i.next();
			tl.taskAborted(te);
		}
	}

	/**
	 * This method is used to fire the task completed event.
	 */

	protected void fireTaskCompleted()
	{
		TaskEvent te = new TaskEvent(this,
				getStatus(), getCurrentProgress());
		for(Iterator i = _listeners.iterator(); i.hasNext();)
		{
			TaskListener tl = (TaskListener)i.next();
			tl.taskCompleted(te);
		}
	}

	/** indicates if the task is complete */
	private boolean		_done = false;

	/** indicates if we received a request to stop */
	private boolean		_shouldStop = false;

	/** the length of the task */
	private int		_length = 0;

	/** our current progress */
	private int		_progress = 0;

	/** the last status message */
	private String		_status = "";

	/** our error, if any */
	private Throwable	_throwable;

	/** our registerd task listeners */
	private List		_listeners = new ArrayList();
}
