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
// File:	WriteCaptureStrategy.java
// Created:	Sun Dec 26 15:59:36 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.config;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.townleyenterprises.common.OverrideNode;
import com.townleyenterprises.common.UseLastOverrideStrategy;

/**
 * This class provides an implementation of the OverrideStrategy which
 * will always return the config supplier with which it was
 * initialized.
 *
 * @version $Id: WriteCaptureStrategy.java,v 1.1 2004/12/26 20:35:18 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class WriteCaptureStrategy extends UseLastOverrideStrategy
{
	/**
	 * The constructor takes a reference to the actual
	 * config supplier where the writes should be placed.
	 * All other operations are delegated to the
	 * appropriate config supplier.
	 *
	 * @param supplier the supplier to decorate
	 */

	public WriteCaptureStrategy(ConfigSupplier supplier)
	{
		_config = supplier;
		_node = new OverrideNode(_config);
	}

	/**
	 * This method resolves to the config supplier which was
	 * specified at object creation unless we don't have the key
	 * yet.  In this case, it behaves like the
	 * UseLastOverrideStrategy.
	 */

	public OverrideNode resolve(Object key, List list)
	{
		return _node;
	}

	/** the config supplier */
	private final ConfigSupplier	_config;

	/** the node we're going to return */
	private final OverrideNode	_node;
}
