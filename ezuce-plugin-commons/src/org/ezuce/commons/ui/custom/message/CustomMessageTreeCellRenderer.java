package org.ezuce.commons.ui.custom.message;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.media.ui.WidgetBuilder;
import org.jivesoftware.smack.packet.Presence;

public class CustomMessageTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -95026094455424659L;
	private static Dimension gSize = new Dimension(220, 25);

	public CustomMessageTreeCellRenderer() {

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		JLabel label = new JLabel();
		label.setPreferredSize(gSize);
		label.setFont(WidgetBuilder.gFont);

		if (value instanceof CustomMessageTreeNode) {
			CustomMessageTreeNode node = (CustomMessageTreeNode) value;

			String text = node.getStatus();

			Presence p = node.getPresence();
			if (p != null) {
				text = p.getStatus();

				if (!leaf)
					label.setIcon(EzucePresenceManager.getIcon(p));
			}

			if (leaf)
				text = "  " + text;

			label.setForeground(node.isCustom() ? Color.blue : Color.black);
			label.setText(text);

			return label;
		}

		return super.getTreeCellRendererComponent(tree, value, sel, expanded,
				leaf, row, hasFocus);
	}
}
