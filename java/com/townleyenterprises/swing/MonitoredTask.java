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
// File:	MonitoredTask.java
// Created:	Thu Nov 20 14:32:22 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

/**
 * The interface which provides monitoring feedback for long running
 * operations.
 *
 * @since 2.1
 * @version $Id: MonitoredTask.java,v 1.1 2003/11/20 16:40:44 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public interface MonitoredTask
{
	/**
	 * This method returns true if the task has received a request
	 * to be cancelled.
	 *
	 * @return true if should stop; false otherwise
	 */

	boolean shouldStop();

	/**
	 * This method returns true if the task has completed.
	 *
	 * @return true if done; false if not
	 */

	boolean isComplete();

	/**
	 * This method returns the length of the current task.
	 *
	 * @return an integer indicating the length of the task
	 */

	int getTaskLength();

	/**
	 * This method returns the current progress of the task.  In
	 * cases where the current progress is unknown, this method
	 * can indicate this state by returning a value < 0.
	 *
	 * @return positive integer < the task length or -1 if unknown
	 */

	int getCurrentProgress();

	/**
	 * This method is used to return the last status message
	 * associated with the task.  Useful for tasks that perform a
	 * number of steps.
	 *
	 * @return the message
	 */

	String getStatus();

	/**
	 * This method is used to start the task.
	 */

	void start();

	/**
	 * This method is used to request that the task be stopped.
	 */

	void requestStop();
}
