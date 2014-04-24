package org.ezuce.common.preference;

import java.util.Iterator;

import javax.swing.DefaultListModel;

import org.jivesoftware.spark.preference.Preference;

public class PreferenceDataBuilder {

	public static void buildPreferensCategories(
			Iterator<Preference> preferences, DefaultListModel listModel) {

		while (preferences.hasNext()) {
			Preference pref = preferences.next();
			listModel.addElement(pref);
		}
	}
}
