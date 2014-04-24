package com.ezuce.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;

import com.ezuce.preferences.category.Admin;
import com.ezuce.preferences.category.Appearance;
import com.ezuce.preferences.category.AudioSettings;
import com.ezuce.preferences.category.Chat;
import com.ezuce.preferences.category.FileTransfer;
import com.ezuce.preferences.category.GroupChat;
import com.ezuce.preferences.category.Login;
import com.ezuce.preferences.category.Notifications;
import com.ezuce.preferences.category.OTRMessaging;
import com.ezuce.preferences.category.Preference;
import com.ezuce.preferences.category.PrivacyPlugin;
import com.ezuce.preferences.category.Sounds;
import com.ezuce.preferences.category.Spellchecker;
import com.ezuce.preferences.category.TaskbarFlashing;
import com.ezuce.preferences.category.TroubleshootingSettings;
import com.ezuce.preferences.category.VideoSettings;

public class PreferenceDataBuilder {

	public static void buildPreferensCategories(
			Iterator<Preference> preferences, DefaultListModel listModel) {

		while (preferences.hasNext()) {
			Preference pref = preferences.next();
			listModel.addElement(pref);
		}
	}

	public static Iterator<Preference> buildPreferensCategories() {
		List<Preference> model = new ArrayList<Preference>();

		model.add(new TroubleshootingSettings());

		// 2 audio
		// model.add(new AudioSettings());

		// 3 Video Settings
		// model.add(new VideoSettings());

		// login
		model.add(new Login());

		// 4. chat
		model.add(new Chat());

		// 5. group chat
		model.add(new GroupChat());

		// 6. File Transfer
		model.add(new FileTransfer());

		// 7. Sounds
		model.add(new Sounds());

		// 8. Appearance
		model.add(new Appearance());

		// 9.Notifications
		model.add(new Notifications());

		// 10. Privacy Plugin
		model.add(new PrivacyPlugin());

		// 11. Taskbar Flashing
		model.add(new TaskbarFlashing());

		// 12.OTR
		model.add(new OTRMessaging());

		// 13. spellchecker
		model.add(new Spellchecker());

		// 14. Admin
		model.add(new Admin());

		return model.iterator();
	}
}
