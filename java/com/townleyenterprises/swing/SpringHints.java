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
// File:	SpringHints.java
// Created:	Sat Nov 15 13:56:23 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

/**
 * This class is used to provide per-component layout hints to the
 * SpringUtils utility class.  This class describes the component's
 * layout constraints in a similar manner to the way you can describe
 * component layout behavior in the NEXTSTEP/MacOS X InterfaceBuilder
 * application.
 * <p>
 * To use this class, create a new instance and put it on the
 * component to be layed out using the {@link
 * javax.swing.JComponent#putClientProperty} method.  The SpringUtils
 * class will attempt to access this information before applying the
 * layout.
 * </p>
 * <p>
 * The following example illustrates this use:
 * <pre>
 *	JPanel container = new JPanel(new SpringLayout());
 * 	JLabel label = new JLabel("Label:");
 * 	JTextField text = new JTextField();
 * 	
 * 	// add the spring hints
 * 	text.putClientProperty(SpringHints.PROPERTY,
 * 			new SpringHints(true, false));
 *
 * 	container.add(label);
 * 	container.add(text);
 *
 *	SpringUtils.applyLayout(content, 2, 2, 6, 6, 12, 6);
 * </pre>
 *
 * @since 2.1
 * @version $Id: SpringHints.java,v 1.2 2004/01/25 19:26:32 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class SpringHints
{
	/** 
	 * this is the property name used to access the hints
	 * instances on the component
	 */

	public static final String PROPERTY	= "springhints";

	/**
	 * This version of the constructor specifies the internal
	 * "springy-ness" of the component.
	 *
	 * @param springWidth true to say the component can expand in
	 * 	width
	 * @param springHeight true to say if the component can expand
	 * 	in height
	 */

	public SpringHints(boolean springWidth, boolean springHeight)
	{
		this(false,false,false,false,springWidth,springHeight);
	}

	/**
	 * This version of the constructor specifies the complete ways
	 * that the component may be affected by the springs.
	 *
	 * @param springNorth true if the top of the component can move
	 * 	relative to the container
	 * @param springSouth true if the bottom of the component can
	 * 	move relative to the container
	 * @param springEast true if the right side of the component
	 * 	can move relative to the container
	 * @param springWest true if the left side of the component
	 * 	can move relative to the container
	 * @param springWidth true to say the component can expand in
	 * 	width
	 * @param springHeight true to say if the component can expand
	 * 	in height
	 */

	public SpringHints(boolean springNorth, boolean springSouth,
			boolean springEast, boolean springWest,
			boolean springWidth, boolean springHeight)
	{
		_snorth = springNorth;
		_ssouth = springSouth;
		_seast = springEast;
		_swest = springWest;
		_swidth = springWidth;
		_sheight = springHeight;
	}

	public boolean getSpringNorth()
	{
		return _snorth;
	}

	public boolean getSpringSouth()
	{
		return _ssouth;
	}

	public boolean getSpringEast()
	{
		return _seast;
	}

	public boolean getSpringWest()
	{
		return _swest;
	}

	public boolean getSpringWidth()
	{
		return _swidth;
	}

	public boolean getSpringHeight()
	{
		return _sheight;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer("[SpringHints:  snorth=");
		buf.append(_snorth);
		buf.append("; ssouth=");
		buf.append(_ssouth);
		buf.append("; seast=");
		buf.append(_seast);
		buf.append("; swest=");
		buf.append(_swest);
		buf.append("; swidth=");
		buf.append(_swidth);
		buf.append("; sheight=");
		buf.append(_sheight);
		buf.append(" ]");

		return buf.toString();
	}
	
	private boolean _snorth;
	private boolean _ssouth;
	private boolean _seast;
	private boolean _swest;
	private boolean _swidth;
	private boolean _sheight;
}
