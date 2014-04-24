package org.ezuce.archive.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.ezuce.archive.ui.util.ArchiveUtil;

public class ImageButton extends JButton {

	private static final long serialVersionUID = -2376546976946653292L;

	public ImageButton(String text) {
		this(null, null, null);
		setText(text);
	}

	public ImageButton(ImageIcon normal) {
		this(normal, normal, normal);
	}

	public ImageButton(ImageIcon normal, ImageIcon pressed, ImageIcon hover) {
		if (normal != null)
			setIcon(normal);

		if (pressed != null)
			setPressedIcon(pressed);

		if (hover != null)
			setRolloverIcon(hover);

		setBackground(ArchiveUtil.gMainBgColor);
		setContentAreaFilled(false);
		setDoubleBuffered(true);
		setFocusPainted(true);
		setFocusable(true);
		setHideActionText(true);

		setHorizontalTextPosition(SwingConstants.CENTER);
		setOpaque(false);
		setMargin(ArchiveUtil.gDefaultButtonMargin);

	}

}
