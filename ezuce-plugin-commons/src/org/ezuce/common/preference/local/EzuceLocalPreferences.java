package org.ezuce.common.preference.local;

import java.util.Properties;

import org.jivesoftware.spark.PluginRes;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;

public class EzuceLocalPreferences extends LocalPreferences {

	private Properties props;

	public EzuceLocalPreferences(Properties props) {
		super(props);
		this.props = props;
	}

	public EzuceLocalPreferences() {
		super();
		this.props = new Properties();
	}

	public void setContactListFontName(String contactListFontName) {
		setString("contactListFontName", contactListFontName);
	}

	public void setChatRoomFontName(String chatRoomFontName) {
		setString("chatRoomFontName", chatRoomFontName);
	}

	public String getContactListFontName() {
		return getString("contactListFontName", "Droid");
	}

	public String getChatRoomFontName() {
		return getString("chatRoomFontName", "Droid");
	}

	public int getNotificationPosition() {
		return getInt("notificationPosition", 1);
	}

	public void setNotificationPosition(int value) {
		setInt("notificationPosition", value);
	}

	public int getNotificationDurationTime() {
		return getInt("notificationDurationTime", 3);
	}

	public void setNotificationDurationTime(int value) {
		setInt("notificationDurationTime", value);
	}

	public void setShowNotificationPopup(boolean show) {
		setBoolean("notificationPopupEnabled", show);
	}

	public boolean isShowNotificationPopup() {
		String toasterPopup = PluginRes
				.getPreferenceRes("notificationPopupEnabled");
		return getBoolean("notificationPopupEnabled",
				toasterPopup != null ? new Boolean(toasterPopup) : false);
	}

	private int getInt(String property, int defaultValue) {
		return Integer.parseInt(props.getProperty(property,
				Integer.toString(defaultValue)));
	}

	private void setInt(String property, int value) {
		props.setProperty(property, Integer.toString(value));
	}

	private String getString(String property, String defaultValue) {
		return props.getProperty(property, defaultValue);
	}

	private void setString(String property, String value) {
		props.setProperty(property, value);
	}

	private boolean getBoolean(String property, boolean defaultValue) {
		return Boolean.parseBoolean(props.getProperty(property,
				Boolean.toString(defaultValue)));
	}

	private void setBoolean(String property, boolean value) {
		props.setProperty(property, Boolean.toString(value));
	}

}
