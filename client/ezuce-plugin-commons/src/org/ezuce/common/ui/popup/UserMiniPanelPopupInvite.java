package org.ezuce.common.ui.popup;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;

public class UserMiniPanelPopupInvite extends UserMiniPanelPopupBase {
	private JMenuItem inviteMenuJMenu;

	public UserMiniPanelPopupInvite(Action inviteAction) {
		inviteMenuJMenu = new JMenuItem(inviteAction);
		this.add(inviteMenuJMenu);		
	}
		
	public void displayPopup(final Component comp, final MouseEvent me) {
		show(comp, me.getPoint().x, me.getPoint().y);
    }		
}
