/**
 * $RCSfile: ,v $
 * $Revision: $
 * $Date: $
 *
 * Copyright (C) 2004-2010 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ezuce.common.ui;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

public class LabelButton extends JButton {
	private static final long serialVersionUID = 7363189276572820946L;

	protected final Icon icon;
	protected final Icon hoverIcon;

	public LabelButton(Icon icon) {
		this(icon, null, null);
	}

	public LabelButton(Icon icon, Icon pressedIcon, String tooltip) {
		this.icon = icon;
		this.hoverIcon = pressedIcon;
		setIcon(icon);
		setOpaque(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		if (pressedIcon != null) {
			setPressedIcon(pressedIcon);
		}

		if (tooltip != null) {
			setToolTipText(tooltip);
		}
	}
}
