package org.ezuce.common.preference;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.Border;

import org.jivesoftware.spark.preference.Preference;

public class EzucePreferenceListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 898315164890033018L;

	private static final Border BORDER_LIST_ITEM = BorderFactory
			.createEmptyBorder(8, 0, 0, 0);
	private static final Font FONT_LIST_ITEM = new Font("Droid Sans",
			Font.PLAIN, 16);
	private static final Color FONT_COLOR_LIST_ITEM = Color.WHITE;
	private static final Color FONT_COLOR_LIST_ITEM_ACTIVE = Color
			.decode("#99D4E5");

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		// draw a category
		if (value instanceof Preference) {
			Preference pref = (Preference) value;

			JLabel prefLabel = new JLabel();

			prefLabel.setFont(FONT_LIST_ITEM);
			prefLabel.setBorder(BORDER_LIST_ITEM);
			prefLabel.setText(pref.getListName());

			if (isSelected) {
				prefLabel.setIcon(pref.getActiveIcon());
				prefLabel.setForeground(FONT_COLOR_LIST_ITEM_ACTIVE);
			} else {
				prefLabel.setIcon(pref.getIcon());
				prefLabel.setForeground(FONT_COLOR_LIST_ITEM);
			}

			return prefLabel;
		}

		// or return a simple label
		return super.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);
	}
}
