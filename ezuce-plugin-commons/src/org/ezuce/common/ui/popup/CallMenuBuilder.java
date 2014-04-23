package org.ezuce.common.ui.popup;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JMenuItem;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

public class CallMenuBuilder {
	public static JMenuItem buildCallSipMenuItem(final String extension, final ActionMap actionMap, final ResourceMap resourceMap) {
		if (extension == null) {
			Log.error("Extension is null...");
			return null;
		}
		JMenuItem sipExt = new JMenuItem(actionMap.get("calloffice"));
		sipExt.setActionCommand(extension);
		sipExt.setIcon(resourceMap.getIcon("callMenuJMenu.icon"));
		sipExt.setText(resourceMap.getString("callMenuJMenuSip.text"));
		return sipExt;
	}
	/**
	 * Builds a list of JMenuItem instances, used to fill the 'Call' JMenu.
	 * Basically, the options include the Home phone number, and the Work phone
	 * number.<br/>
	 * The phone types considered are: VOICE, CELL
	 *
	 * @param jid
	 * @return
	 */
	public static List<JMenuItem> buildCallMenuItemsFromId(final String jid, final ActionMap actionMap) {
		final List<JMenuItem> menuItems = new ArrayList<JMenuItem>();
		final VCard targetVCard = jid != null ? SparkManager.getVCardManager().getVCard(jid) : null;
		if (targetVCard != null) {
			if (targetVCard.getPhoneHome("VOICE") != null && !targetVCard.getPhoneHome("VOICE").isEmpty()) {
				JMenuItem jmiHome = new JMenuItem(actionMap.get("call"));
				jmiHome.setActionCommand(targetVCard.getPhoneHome("VOICE"));
				jmiHome.setText("Call home: " + targetVCard.getPhoneHome("VOICE"));
				menuItems.add(jmiHome);
			}

			if (targetVCard.getPhoneHome("CELL") != null && !targetVCard.getPhoneHome("CELL").isEmpty()) {
				JMenuItem jmiHome = new JMenuItem(actionMap.get("call"));
				jmiHome.setActionCommand(targetVCard.getPhoneHome("CELL"));
				jmiHome.setText("Call home (cell): " + targetVCard.getPhoneHome("CELL"));
				menuItems.add(jmiHome);
			}

			if (targetVCard.getPhoneWork("VOICE") != null && !targetVCard.getPhoneWork("VOICE").isEmpty()) {
				JMenuItem jmiWork = new JMenuItem(actionMap.get("call"));
				jmiWork.setActionCommand(targetVCard.getPhoneWork("VOICE"));
				jmiWork.setText("Call work: " + targetVCard.getPhoneWork("VOICE"));
				menuItems.add(jmiWork);
			}

			if (targetVCard.getPhoneWork("CELL") != null && !targetVCard.getPhoneWork("CELL").isEmpty()) {
				JMenuItem jmiWork = new JMenuItem(actionMap.get("call"));
				jmiWork.setActionCommand(targetVCard.getPhoneWork("CELL"));
				jmiWork.setText("Call work (cell): " + targetVCard.getPhoneWork("CELL"));
				menuItems.add(jmiWork);
			}

		}
		
		if (menuItems.isEmpty()) {
			JMenuItem dummyMenuItem = new JMenuItem("No available number");
			dummyMenuItem.setEnabled(false);
			menuItems.add(dummyMenuItem);
		}
		return menuItems;
	}

}
