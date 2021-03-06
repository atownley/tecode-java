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
// File:	TaskEvent.java
// Created:	Sun Nov 23 22:11:45 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing.event;

import com.townleyenterprises.swing.MonitoredTask;

/**
 * This is a simple class that is used to propogate task information.
 * It is essentially a snapshot of the "interesting" bits of the task.
 *
 * @since 2.1
 * @version $Id: TaskEvent.java,v 1.3 2004/07/28 10:33:59 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class TaskEvent
{
	/**
	 * The constructor takes the task information.
	 *
	 * @param source the task generating the event
	 * @param status the status text when the event happened
	 * @param progress the progress when the event happened
	 */

	public TaskEvent(MonitoredTask source, String status, int progress)
	{
		_source = source;
		_status = status;
		_progress = progress;
	}

	public MonitoredTask getSource()
	{
		return _source;
	}

	public String getStatus()
	{
		return _status;
	}

	public int getProgress()
	{
		return _progress;
	}

	private MonitoredTask	_source;
	private String		_status;
	private int		_progress;
}
