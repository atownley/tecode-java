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
// File:	TStatusBar.java
// Created:	Thu Nov 20 11:41:04 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * This is a GUI class representing a status bar common to most
 * applications.  A simple status bar is pretty easy to manage with
 * the basic controls, but when you want to replace controls or add
 * certain things dynamically, it gets somewhat cumbersome.  This is
 * an attempt to solve those issues.
 *
 * @since 2.1
 * @version $Id: TStatusBar.java,v 1.6 2004/07/28 10:33:59 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class TStatusBar extends JPanel
{
	/**
	 * This constructor creates a status bar which will not
	 * display localized text.
	 */

	public TStatusBar()
	{
		this(null);
	}

	/**
	 * This constructor builds your basic, vanilla status bar
	 * using the indicated ResourceProvider to load resource
	 * strings.
	 *
	 * @param provider the resource provider
	 */

	public TStatusBar(ResourceProvider provider)
	{
		super(new BorderLayout());
		_resources = provider;
	
		_content = new JPanel();
		_content.setLayout(new BoxLayout(_content,
					BoxLayout.X_AXIS));
		_statusText.setEditable(false);
		_statusText.setRequestFocusEnabled(false);
		_statusText.setFocusable(false);
		_statusText.setBackground(UIManager.getColor("Button.background"));
		_content.add(_statusText);

		add(_content, BorderLayout.CENTER);
		
		// create the new status component
		_monitor = new JPanel(new BorderLayout());
		_monitor.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		_monitorLabel = new JLabel();
		_progress = new JProgressBar();
		_monitor.add(_monitorLabel, BorderLayout.WEST);
		_monitor.add(_progress, BorderLayout.CENTER);
		_monitor.setVisible(false);

		_monitorLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
	}

	/**
	 * This method is used to add components to the right side of
	 * the status bar.
	 *
	 * @param component
	 * @return the component argument
	 */

	public Component add(Component component)
	{
		Dimension max = component.getPreferredSize();
		if(component instanceof JComponent)
			((JComponent)component).setMaximumSize(max);
		_content.add(component);

		return(component);
	}
	
	/**
	 * This method returns a ChangeListener which is configured to
	 * automatically update the status text based on the currently
	 * selected menu item.
	 *
	 * @param property the client property name holding the status
	 * 	text of the menu item
	 * @param localized true to present localized strings; false
	 * 	to assume strings already localized
	 * @return a ChangeListener for menu events
	 */

	public ChangeListener getMenuStatusListener(String property,
					boolean localized)
	{
		ResourceProvider provider = _resources;
		if(localized == false)
			provider = null;

		return new MenuStatusListener(provider, _statusText,
					property);
	}

	/**
	 * This method is used to automatically monitor a
	 * MonitoredTask using a progress bar.
	 *
	 * @param task the task to monitor
	 * @param interval the number of seconds to wait between
	 * 	updates
	 */

	public void monitorTask(final MonitoredTask task, double interval)
	{
		int len = task.getTaskLength();
		_progress.setMinimum(0);
		if(len != -1)
			_progress.setMaximum(len);
		else
			_progress.setIndeterminate(true);
		
		_monitorLabel.setText(task.getStatus());
		_monitor.setVisible(true);
		setComponent(_monitor);
		revalidate();

		_timer = new Timer((int)(interval * 1000), new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(_progress.isIndeterminate())
				{
					int len = task.getTaskLength();
					if(len != -1)
					{
						_progress.setMaximum(task.getTaskLength());
						_progress.setIndeterminate(false);
					}
				}
				else
				{
					_progress.setMaximum(task.getTaskLength());
				}
			
				int val = task.getCurrentProgress();
				_progress.setValue(val);
				_monitorLabel.setText(task.getStatus());

				if(task.isComplete() || task.hasError())
				{
					_progress.setValue(_progress.getMaximum());
					_timer.stop();
					restore();
				}
			}
		});

		if(!task.isComplete())
		{
			_timer.start();
		}
		else
		{
			restore();
		}
	}

	public void monitorTask(MonitoredTask task, int interval)
	{
		monitorTask(task, (double)interval);
	}

	/**
	 * This method is used to remove the given component from the
	 * status bar.
	 *
	 * @param component
	 */

	public void remove(Component component)
	{
		_content.remove(component);
	}

	/**
	 * This method removes all of the components from the status
	 * bar except the default control.
	 */

	public void removeAll()
	{
		int size = _content.getComponentCount();
		for(int i = 1; i < size; ++i)
			_content.remove(i);
	}

	/**
	 * This method is used to restore the normal status component.
	 */

	public void restore()
	{
		Component component = getComponent(0);
		component.setVisible(false);
		remove(component);

		// make sure the progress bar is stopped
		_progress.setIndeterminate(false);
		_progress.setValue(0);
		add(_content, BorderLayout.CENTER);
		_content.setVisible(true);
		revalidate();
	}

	/**
	 * This method is used to set the main component of the status
	 * bar to something other than the default.  Normally, this
	 * only occurs for a short period (like loading or saving a
	 * file), and then the default status bar is restored.
	 *
	 * @param component the new component for the status bar
	 */

	public void setComponent(Component component)
	{
		_content.setVisible(false);
		remove(_content);
		add(component, BorderLayout.CENTER);
		component.setVisible(true);
		invalidate();
	}

	/**
	 * This method sets the status text on the status bar.  This
	 * method does not try and resolve the resource name.
	 *
	 * @param text the new status text
	 */

	public void setText(String text)
	{
		_statusText.setText(text);
	}

	/**
	 * This method sets the status text on the status bar base on
	 * the resource key.
	 *
	 * @param key the resource key
	 */

	public void setLocalizedText(String key)
	{
		if(_resources != null)
			setText(_resources.getString(key));
		else
			setText(key);
	}

	private static class MenuStatusListener implements ChangeListener
	{
		public MenuStatusListener(ResourceProvider res,
					JTextComponent status,
					String property)
		{
			_res = res;
			_status = status;
			_property = property;
		}

		public void stateChanged(ChangeEvent e)
		{
			JMenuItem item = (JMenuItem)e.getSource();
			String s = (String)item.getClientProperty(_property);
			if(item.isArmed())
			{
				if(_res != null)
					_status.setText(_res.getString(s));
				else
					_status.setText(s);
			}
			else if(_res != null)
			{
				_status.setText(_res.getString("sReady"));
			}
			else
			{
				_status.setText("");
			}
		}

		private final JTextComponent	_status;
		private final String		_property;
		private final ResourceProvider	_res;
	}

	/** our resource provider */
	private ResourceProvider	_resources;

	/** our default controls */
	private JTextField		_statusText = new JTextField();

	/** the default panel */
	private JPanel			_content;

	/** our special status monitor */
	private JPanel			_monitor;

	/** our monitor label */
	private JLabel			_monitorLabel;

	/** our monitor progress bar */
	private JProgressBar		_progress;

	/** a timer */
	private Timer			_timer;
}
