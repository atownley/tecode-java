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
// File:	TAboutBox.java
// Created:	Wed Nov 26 02:15:37 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * This is a pretty basic about box.
 *
 * @since 2.1
 * @version $Id: TAboutBox.java,v 1.3 2004/07/28 10:33:59 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class TAboutBox extends JDialog
{
	/**
	 * The constructor creates the dialog.
	 *
	 * @param parent the parent frame for the dialog
	 * @param title the title for the dialog
	 * @param modal true to make the dialog modal
	 * @param background the image to use for the background
	 * @param decorated true to use window decorations; false
	 * otherwise
	 */

	public TAboutBox(Frame parent, String title, boolean modal,
				ImageIcon background, boolean decorated)
	{
		super(parent, title, modal);
		setResizable(false);
		setUndecorated(!decorated);

		_background = background; //.getImage();
		Dimension dim = new Dimension(background.getIconWidth(),
							background.getIconHeight());

		_content = new ImagePanel();
		_content.setPreferredSize(dim);
		_content.setMinimumSize(dim);
		_content.setMaximumSize(dim);
		getContentPane().add(_content, BorderLayout.CENTER);
		pack();
	}

	/**
	 * This method is used to set the default font to be used when
	 * printing the version and the copyright information.
	 *
	 * @param font the font
	 */

	public void setFont(Font font)
	{
		_font = font;
	}

	/**
	 * This method is used to set the default font color used to print
	 * the version and copyright information.
	 *
	 * @param color the color
	 */

	public void setColor(Color color)
	{
		_color = color;
	}

	/**
	 * This method is used to set the copyright information.
	 *
	 * @param point the point where the text should be drawn
	 * @param copyright the copyright to be shown
	 */

	public void setCopyright(Point point, String copyright)
	{
		_cpoint = point;
		_copyright = copyright;
	}

	/**
	 * This method is used to set the version information.
	 *
	 * @param point the point where the text should be drawn
	 * @param version the version to be shown
	 */

	public void setVersion(Point point, String version)
	{
		_vpoint = point;
		_version = version;
	}

	private class ImagePanel extends JPanel
	{
		public ImagePanel()
		{
			super(true);
		}

		public void paintComponent(Graphics g)
		{
			// set up the antialising
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			if(isOpaque())
			{
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
			}

			_background.paintIcon(this, g, 0, 0);

			g.setColor(_color);
			if(_version != null && _vpoint != null)
				g.drawString(_version, _vpoint.x, _vpoint.y);
			
			if(_copyright != null && _cpoint != null)
				g.drawString(_copyright, _cpoint.x, _cpoint.y);
		}
	}

	/** the copyright informaiton to be displayed */
	private String		_copyright;

	/** the version information to be displayed */
	private String		_version;

	/** the image we're displaying */
	private ImageIcon	_background;

	/** the content panel */
	private ImagePanel	_content;

	/** the version point */
	private Point		_vpoint;

	/** the copyright point */
	private Point		_cpoint;

	/** the font color */
	private Color		_color = Color.black;

	/** the font */
	private Font		_font = UIManager.getFont("TextField.font");
}
