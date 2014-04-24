package org.ezuce.commons.ui.custom.message;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.media.ui.WidgetBuilder;
import org.jivesoftware.smack.packet.Presence;

public class PrecenceCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 231395792337412173L;
	private static Dimension gSize = new Dimension(1, 30);

	public PrecenceCellRenderer() {
		setFont(WidgetBuilder.gFont);
		setPreferredSize(gSize);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		JLabel label = (JLabel) super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);

		if (value instanceof Presence) {
			Presence p = (Presence) value;
			Icon icon = EzucePresenceManager.getIcon(p);
			setIcon(icon);
			label.setText(p.getStatus());
		}

		return label;

	}

}
