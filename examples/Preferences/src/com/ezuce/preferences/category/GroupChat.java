package com.ezuce.preferences.category;

import static com.ezuce.util.GraphicUtils.createImageIcon;

import javax.swing.JComponent;

public class GroupChat extends Preference {

	/**  */
	private static final long serialVersionUID = -5172249977428979687L;
	private static final String iconName = "/resources/images/prefs/groupchat";
	private static final String TITLE = "Group Chat";

	public GroupChat() {
		super(TITLE, createImageIcon(iconName + "_on.png"),
				createImageIcon(iconName + "_off.png"),
				createImageIcon(iconName + ".png"));
	}

	@Override
	public JComponent getGUI() {
		// TODO: to be implemented
		System.out.println("TODO: AudioSettings#getGUI()");
		return null;
	}

}
