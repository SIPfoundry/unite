package com.ezuce.preferences.category;

import static com.ezuce.util.GraphicUtils.createImageIcon;

import javax.swing.JComponent;

public class Chat extends Preference {

	/**  */
	private static final long serialVersionUID = 1803617032322406575L;
	private static final String iconName = "/resources/images/prefs/chat";
	private static final String TITLE = "Chat";

	public Chat() {
		super(TITLE, createImageIcon(iconName + "_on.png"),
				createImageIcon(iconName + "_off.png"),
				createImageIcon(iconName + ".png"));
	}

	@Override
	public JComponent getGUI() {
		// TODO: to be implemented
		System.out.println("TODO: PreferencesCategory#getGUI()");
		return null;
	}
}
