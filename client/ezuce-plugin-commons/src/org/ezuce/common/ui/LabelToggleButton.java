package org.ezuce.common.ui;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JToggleButton;

public class LabelToggleButton extends JToggleButton {
	private static final long serialVersionUID = -1231776366814218212L;
	protected final Icon icon;
	protected final Icon pressedIcon;

	public LabelToggleButton(Icon icon) {
		this(icon, null, null);
	}

	public LabelToggleButton(Icon icon, Icon pressedIcon, String tooltip) {
		this.icon = icon;
		this.pressedIcon = pressedIcon;
		setIcon(icon);
		setOpaque(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		if (pressedIcon != null) {
			setPressedIcon(pressedIcon);
			setSelectedIcon(pressedIcon);
		}

		if (tooltip != null) {
			setToolTipText(tooltip);
		}
	}

}
