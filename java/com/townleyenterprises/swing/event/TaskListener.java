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
// File:	TaskListener.java
// Created:	Sun Nov 23 22:11:45 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing.event;

/**
 * This interface should be implemented by code that wishes to be
 * notified of task events.  Currently, there are only two
 * notifications:  task started, task complete and task aborted.  The
 * notification listeners should be notified in a different thread
 * than the one the task is executing in so that Swing threading
 * problems do not occur.
 *
 * @since 2.1
 * @version $Id: TaskListener.java,v 1.2 2004/01/25 19:26:48 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public interface TaskListener
{
	/**
	 * This method is called when the task has been started.
	 *
	 * @param event the TaskEvent
	 */

	void taskStarted(TaskEvent event);

	/**
	 * This method is called when the task has been aborted.
	 *
	 * @param event the TaskEvent
	 */

	void taskAborted(TaskEvent event);

	/**
	 * This method is called when the task has completed normally
	 * (even if it has encountered an error).
	 *
	 * @param event the TaskEvent
	 */

	void taskCompleted(TaskEvent event);
}
