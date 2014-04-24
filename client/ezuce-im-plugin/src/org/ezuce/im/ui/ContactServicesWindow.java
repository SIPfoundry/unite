package org.ezuce.im.ui;

import javax.swing.JFrame;

import org.ezuce.common.EzuceConferenceServices;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;

public class ContactServicesWindow extends JFrame {
	private static final long serialVersionUID = -1360432513319583409L;

	public ContactServicesWindow() {
		setTitle(Res.getString("conference.services"));
		setIconImage(SparkManager.getApplicationImage().getImage());
		add(EzuceConferenceServices.getBookmarksUI());
	}
}
