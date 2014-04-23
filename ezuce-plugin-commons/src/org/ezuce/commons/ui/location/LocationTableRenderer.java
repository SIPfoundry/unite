package org.ezuce.commons.ui.location;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class LocationTableRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1101012637223917332L;
	protected static final Color gBgColorDefault = new Color(248, 248, 248);
	private final String mDefaultText;

	public LocationTableRenderer(String defaultText) {
		this.mDefaultText = defaultText;
		setOpaque(true);
		setSize(100, 30);
		setBackground(Color.white);

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		if ((value instanceof String) && mDefaultText.equals(value.toString())) {
			setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createEmptyBorder(0, 30, 0, 30), BorderFactory
					.createMatteBorder(0, 0, 2, 0, gBgColorDefault)));
		} else
			setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		if (isSelected) {
			setBackground(gBgColorDefault);
		} else {
			setBackground(Color.white);
		}

		setText(value == null ? "" : value.toString());

		return this;
	}
}
