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
// File:	ThemeLoader.java
// Created:	Thu Nov 13 23:49:14 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.UIDefaults;

/**
 * This class is similar to the {@link
 * com.townleyenterprises.swing.ResourceLoader}, but it isn't
 * dealing with the same sort of information.  For themes, arbitrary
 * properties files (with a '.theme' suffix) are used rather than
 * properties.  These are also not localized, so the locale concept
 * doesn't apply in this case.
 *
 * @version $Id: ThemeLoader.java,v 1.3 2004/01/25 19:26:42 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class ThemeLoader
{
	/**
	 * The constructor takes an object to use as the basis for
	 * finding the theme and the name.
	 *
	 * @param obj the object to provide the base resource location
	 * @param theme the name of the theme to load
	 */

	public ThemeLoader(Object obj, String theme)
	{
		this(obj.getClass(), theme);
	}

	/**
	 * This constructor takes a class to use as the basis for
	 * finding the theme and the name.
	 *
	 * @param cls the class providing the base resource location
	 * @param theme the name of the theme to load
	 */

	public ThemeLoader(Class cls, String name)
	{
		this(null, cls, name);
	}

	/**
	 * This constructor is used to support inheritance of themes.
	 * This is mainly useful if only the default look & feel
	 * colors should be changed, but other themeable things should
	 * remain the same.
	 *
	 * @param parent the parent theme loader
	 * @param cls the class providing the context for the theme
	 * @param name the name of the theme to load
	 */

	public ThemeLoader(ThemeLoader parent, Class cls, String name)
	{
		// FIXME:  there's redundant code here... :(
		_klass = cls;
		_name = name;

		_theme.clear();
		if(parent != null)
			_theme.putAll(parent._theme);

		try
		{
			InputStream is;
			is = _klass.getResourceAsStream("resources/" 
						+ name + ".theme");
			if(is != null)
			{
				_theme.load(is);
				is.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This constructor is used to load themes directly from the
	 * specified input stream.
	 *
	 * @param stream the InputStream
	 */

	public ThemeLoader(InputStream stream)
	{
		this(null, stream);
	}

	/**
	 * This constructor is used to support inheritance from
	 * arbitrary input streams.
	 *
	 * @param parent the parent theme
	 * @param stream the input stream
	 */

	public ThemeLoader(ThemeLoader parent, InputStream stream)
	{
		_theme.clear();
		if(parent != null)
			_theme.putAll(parent._theme);

		try
		{
			if(stream != null)
			{
				_theme.load(stream);
				stream.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to return theme colors.
	 *
	 * @param key the resource key
	 * @return the color
	 */

	public Color getColor(String key)
	{
		String colorName = getSetting("color." + key);
		if(colorName == null)
			return null;

		Color color = null;
		try
		{
			color = Color.decode(colorName);
		}
		catch(NumberFormatException e)
		{
			System.err.println("error:  unable to load color '" + key + "'.");
			e.printStackTrace();
		}

		return color;
	}

	/**
	 * This method is used to load a specific font from the theme.
	 *
	 * @param name the symbolic name of the font to load
	 * @return an initialized font object
	 */

	public Font getFont(String key)
	{
		String name = getSetting("font." + key + ".name");
		String sstyle = getSetting("font." + key + ".style");
		String ssize = getSetting("font." + key + ".size");
		int style = Font.PLAIN;
		int size = 12;

		// if nothing is specified, return nothing so the
		// defaults will be used
		if(name == null && sstyle == null && ssize == null)
			return null;

		// if something is specified, do our best to oblige
		if(sstyle != null)
		{
			sstyle = sstyle.toLowerCase();
			if(sstyle.indexOf("bold") != -1)
				style |= Font.BOLD;
			if(sstyle.indexOf("italic") != -1)
				style |= Font.ITALIC;
		}

		if(ssize != null)
			size = Integer.parseInt(ssize);

		return new Font(name, style, size);
	}

	/**
	 * This method is used to return the appropriately initialized
	 * Metal look and feel theme based on the theme properties.
	 *
	 * @return the theme suitable for use in the Metal L&F
	 */
	
	public MetalTheme getMetalTheme()
	{
		if(_metalTheme == null)
			_metalTheme = new PrivateMetalTheme(this);

		return _metalTheme;
	}

	/**
	 * Accessor for the theme name.
	 *
	 * @return the name of this theme
	 */

	public String getName()
	{
		return _name;
	}

	/**
	 * This method is used to find a given theme setting.
	 *
	 * @param key the resource string key
	 * @return the string for the key
	 */

	public String getSetting(String key)
	{
		return _theme.getProperty(key.toLowerCase());
	}

	/**
	 * This is a utility method for loading color UI resources.
	 *
	 * @param key the name
	 * @return the UIResource for the key
	 */

	protected ColorUIResource getColorUIResource(String key)
	{
		Color color = getColor(key);
		if(color == null)
			return null;

		return new ColorUIResource(color);
	}

	/**
	 * This is a utility method for loading font UI resources.
	 *
	 * @param key the name
	 * @return the UIResource for the key
	 */

	protected FontUIResource getFontUIResource(String key)
	{
		Font font = getFont(key);
		if(font == null)
			return null;

		return new FontUIResource(font);
	}

	/**
	 * This class is used to provide the MetalTheme based on the
	 * values in the theme file.
	 */

	private static class PrivateMetalTheme extends DefaultMetalTheme
	{
		public PrivateMetalTheme(ThemeLoader loader)
		{
			_name = loader.getName();

			// try the colors
			_primary1 = loader.getColorUIResource("primary1");
			_primary2 = loader.getColorUIResource("primary2");
			_primary3 = loader.getColorUIResource("primary3");
			_secondary1 = loader.getColorUIResource("secondary1");
			_secondary2 = loader.getColorUIResource("secondary2");
			_secondary3 = loader.getColorUIResource("secondary3");

			_controlbg = loader.getColorUIResource("control.background");
			_controlbgdisabled = loader.getColorUIResource("control.background.disabled");
			_controlbgselected = loader.getColorUIResource("control.background.selected");
			_controlfg = loader.getColorUIResource("control.foreground");
			_controlfgdisabled = loader.getColorUIResource("control.foreground.disabled");

			// NOTE:  not currently supported
//			_controlfgselected = loader.getColorUIResource("control.foreground.selected");
			
			_buttonbg = loader.getColorUIResource("button.background");
			_buttonbgdisabled = loader.getColorUIResource("button.background.disabled");
			_buttonbgselected = loader.getColorUIResource("button.background.selected");
			_buttonfg = loader.getColorUIResource("button.foreground");
			_buttonfgdisabled = loader.getColorUIResource("button.foreground.disabled");
			
			// NOTE:  not currently supported
//			_buttonfgselected = loader.getColorUIResource("button.foreground.selected");

			_toolbarbg = loader.getColorUIResource("toolbar.background");
			
			_text = loader.getColorUIResource("text");
			_textselected = loader.getColorUIResource("text.selected");
			_textselectedbg = loader.getColorUIResource("text.selected.background");
			
			_windowbg = loader.getColorUIResource("window.background");
			_windowtitlebg = loader.getColorUIResource("window.title.background");
			_windowtitlefg = loader.getColorUIResource("window.title.foreground");
			_windowtitleinactivebg = loader.getColorUIResource("window.title.inactive.background");
			_windowtitleinactivefg = loader.getColorUIResource("window.title.inactive.foreground");

			_black = loader.getColorUIResource("black");
			_white = loader.getColorUIResource("white");
		
			// try the fonts
			_controlTextFont = loader.getFontUIResource("controltext");
			_menuTextFont = loader.getFontUIResource("menutext");
			_subTextFont = loader.getFontUIResource("subtext");
			_systemTextFont = loader.getFontUIResource("systemtext");
			_userTextFont = loader.getFontUIResource("usertext");
			_windowTitleFont = loader.getFontUIResource("windowtitle");
		}

		public void addCustomEntriesToTable(UIDefaults table)
		{
			super.addCustomEntriesToTable(table);

			// now, we have to take care of all our
			// special overrides here

			table.put("Button.background", _buttonbg);
			table.put("Button.disabledText", _buttonfgdisabled);
			table.put("ComboBox.buttonBackground", _buttonbg);
			table.put("ComboBox.background", _buttonbg);
			table.put("ComboBox.disabledText", _buttonfgdisabled);
			table.put("ComboBox.disabledForeground", _buttonfgdisabled);
			table.put("ComboBox.disabledBackground", _buttonbg);
			table.put("Button.select", _buttonbgselected);
			table.put("ToolBar.background", _toolbarbg);

			if(_textselected != null)
			{
				table.put("Menu.selectionForeground", _textselected);
				table.put("MenuItem.selectionForeground", _textselected);
				table.put("RadioButtonMenuItem.selectionForeground", _textselected);
				table.put("ToolTip.foreground", _textselected);
				table.put("activeCaptionText", _textselected);
				table.put("ComboBox.selectionForeground", _textselected);
			}
		}

		public String getName()
		{
			return _name;
		}

		protected ColorUIResource getBlack()
		{
			if(_black != null)
				return _black;

			return super.getBlack();
		}

		public FontUIResource getControlTextFont()
		{
			if(_controlTextFont != null)
				return _controlTextFont;

			return super.getControlTextFont();
		}

		public FontUIResource getMenuTextFont()
		{
			if(_menuTextFont != null)
				return _menuTextFont;

			return super.getMenuTextFont();
		}

		protected ColorUIResource getPrimary1()
		{
			if(_primary1 != null)
				return _primary1;

			return super.getPrimary1();
		}

		protected ColorUIResource getPrimary2()
		{
			if(_primary2 != null)
				return _primary2;

			return super.getPrimary2();
		}

		protected ColorUIResource getPrimary3()
		{
			if(_primary3 != null)
				return _primary3;

			return super.getPrimary3();
		}

		protected ColorUIResource getSecondary1()
		{
			if(_secondary1 != null)
				return _secondary1;

			return super.getSecondary1();
		}

		protected ColorUIResource getSecondary2()
		{
			if(_secondary2 != null)
				return _secondary2;

			return super.getSecondary2();
		}

		protected ColorUIResource getSecondary3()
		{
			if(_secondary3 != null)
				return _secondary3;

			return super.getSecondary3();
		}

		public ColorUIResource getControl()
		{
			if(_controlbg != null)
				return _controlbg;

			return super.getControl();
		}

		public ColorUIResource getControlDisabled()
		{
			if(_controlbgdisabled != null)
				return _controlbgdisabled;

			return super.getControlDisabled();
		}

		public ColorUIResource getControlHighlight()
		{
			if(_controlbgselected != null)
				return _controlbgselected;

			return super.getControlHighlight();
		}
		
		public ColorUIResource getControlTextColor()
		{
			if(_controlfg != null)
				return _controlfg;

			return super.getControlTextColor();
		}
		
		public ColorUIResource getInactiveControlTextColor()
		{
			if(_controlfgdisabled != null)
				return _controlfgdisabled;

			return super.getInactiveControlTextColor();
		}
	
		public ColorUIResource getUserTextColor()
		{
			if(_text != null)
				return _text;

			return super.getUserTextColor();
		}
	
		public ColorUIResource getHighlightedTextColor()
		{
			if(_textselected != null)
				return _textselected;

			return super.getHighlightedTextColor();
		}
	
		public ColorUIResource getTextHighlightColor()
		{
			if(_textselectedbg != null)
				return _textselectedbg;

			return super.getTextHighlightColor();
		}
	
		public ColorUIResource getWindowBackground()
		{
			if(_windowbg != null)
				return _windowbg;

			return super.getWindowBackground();
		}
	
		public ColorUIResource getWindowTitleBackground()
		{
			if(_windowtitlebg != null)
				return _windowtitlebg;

			return super.getWindowTitleBackground();
		}
	
		public ColorUIResource getWindowTitleForeground()
		{
			if(_windowtitlefg != null)
				return _windowtitlefg;

			return super.getWindowTitleForeground();
		}
	
		public ColorUIResource getWindowTitleInactiveBackground()
		{
			if(_windowtitleinactivebg != null)
				return _windowtitleinactivebg;

			return super.getWindowTitleInactiveBackground();
		}
	
		public ColorUIResource getWindowTitleInactiveForeground()
		{
			if(_windowtitleinactivefg != null)
				return _windowtitleinactivefg;

			return super.getWindowTitleInactiveForeground();
		}
	
		public FontUIResource getSubTextFont()
		{
			if(_subTextFont != null)
				return _subTextFont;

			return super.getSubTextFont();
		}

		public FontUIResource getSystemTextFont()
		{
			if(_systemTextFont != null)
				return _systemTextFont;

			return super.getSystemTextFont();
		}

		public FontUIResource getUserTextFont()
		{
			if(_userTextFont != null)
				return _userTextFont;

			return super.getUserTextFont();
		}

		protected ColorUIResource getWhite()
		{
			if(_white != null)
				return _white;

			return super.getWhite();
		}

		public FontUIResource getWindowTitleFont()
		{
			if(_windowTitleFont != null)
				return _windowTitleFont;

			return super.getWindowTitleFont();
		}
		
		private FontUIResource	_controlTextFont = null;
		private FontUIResource	_menuTextFont = null;
		private ColorUIResource	_primary1 = null;
		private ColorUIResource	_primary2 = null;
		private ColorUIResource	_primary3 = null;
		private ColorUIResource	_secondary1 = null;
		private ColorUIResource	_secondary2 = null;
		private ColorUIResource	_secondary3 = null;
		private ColorUIResource	_controlbg = null;
		private ColorUIResource	_controlbgdisabled = null;
		private ColorUIResource	_controlbgselected = null;
		private ColorUIResource	_controlfg = null;
		private ColorUIResource	_controlfgdisabled = null;
		private ColorUIResource	_controlfgselected = null;
		private ColorUIResource	_buttonbg = null;
		private ColorUIResource	_buttonbgdisabled = null;
		private ColorUIResource	_buttonbgselected = null;
		private ColorUIResource	_buttonfg = null;
		private ColorUIResource	_buttonfgdisabled = null;
		private ColorUIResource	_buttonfgselected = null;
		private ColorUIResource	_toolbarbg = null;
		private ColorUIResource	_text = null;
		private ColorUIResource	_textselected = null;
		private ColorUIResource	_textselectedbg = null;
		private ColorUIResource	_windowbg = null;
		private ColorUIResource	_windowtitlebg = null;
		private ColorUIResource	_windowtitlefg = null;
		private ColorUIResource	_windowtitleinactivebg = null;
		private ColorUIResource	_windowtitleinactivefg = null;
		private ColorUIResource	_black = null;
		private ColorUIResource	_white = null;
		private FontUIResource	_subTextFont = null;
		private FontUIResource	_systemTextFont = null;
		private FontUIResource	_userTextFont = null;
		private FontUIResource	_windowTitleFont = null;
		private String		_name = null;
	}
	
	/** a reference to the class */
	private Class			_klass;

	/** save the theme name */
	private String			_name;

	/** the resource bundle to be accessed */
	private Properties		_theme = new Properties();

	/** the metal theme */
	private MetalTheme		_metalTheme = null;
}
