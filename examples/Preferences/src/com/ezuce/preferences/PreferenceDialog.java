package com.ezuce.preferences;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ezuce.preferences.category.Preference;

public class PreferenceDialog extends JFrame implements PropertyChangeListener {

	/**  */
	private static final long serialVersionUID = -5184248354160623832L;

	public void invoke(JFrame parentFrame, Iterator<Preference> preferences,
			Preference selectedPref) {

	}

	public Container getDialog() {
		return new JPanel();
	}

	public void propertyChange(PropertyChangeEvent e) {
	}
}
