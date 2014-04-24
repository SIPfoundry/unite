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
package org.ezuce.im.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.ezuce.common.ui.EzuceContactItem;

public class EzucePanelRenderer extends EzuceContactItem implements ListCellRenderer {
	private static final long serialVersionUID = -6648401779698144513L;

	/**
	 * Construct Default JPanelRenderer.
	 */
	public EzucePanelRenderer() {
		super("", "", "");
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setForeground((Color)UIManager.get("List.selectionForeground"));
            setBackground((Color)UIManager.get("List.selectionBackground"));
            setBorder(BorderFactory.createLineBorder((Color)UIManager.get("List.selectionBorder")));
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setBorder(BorderFactory.createLineBorder((Color)UIManager.get("ContactItem.background")));
        }	
		list.setBackground(new Color(250, 250, 251));

		EzuceContactItem renderItem = (EzuceContactItem)value;
		setFocusable(false);
		setNickname(renderItem.getNickname());
		setAlias(renderItem.getAlias());
		getNicknameLabel().setFont(renderItem.getNicknameLabel().getFont());
		getNicknameLabel().setForeground(renderItem.getNicknameLabel().getForeground());
		setBackground(new Color(250, 250, 251));
		getDescriptionLabel().setText(renderItem.getDescriptionLabel().getText());
		getImgPanel().setMugshot(renderItem.getImgPanel().getMugshot());
		getImgPanel().setStatus(renderItem.getImgPanel().getStatus());
		getImgPanel().setState(renderItem.getImgPanel().getState());
		getImgPanel().setBackgroundImage(renderItem.getImgPanel().getBackgroundImage());
		getStatusLabel().setText(renderItem.getStatusLabel().getText());
		getStatusLabel().setFont(renderItem.getStatusLabel().getFont());
		getLocationLabel().setText(renderItem.getLocationLabel().getText());
		getLocationLabel().setFont(renderItem.getLocationLabel().getFont());
		return this;
	}
}
