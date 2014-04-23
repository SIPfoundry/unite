package org.ezuce.im;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.ConfigureVoice;
import org.ezuce.common.ui.EzuceContactGroup;
import org.ezuce.common.ui.EzuceContactInfoWindow;
import org.ezuce.common.ui.EzuceContactItem;
import org.ezuce.common.ui.EzuceContactList;
import org.ezuce.common.ui.MessageListenerRegistry;
import org.ezuce.common.VoiceUtil;
import org.ezuce.im.action.ConferenceServicesAction;
import org.ezuce.im.ui.EzuceChatContainer;
import org.ezuce.im.ui.EzuceChatRoom;
import org.ezuce.im.ui.EzuceCommandPanel;
import org.ezuce.im.ui.EzuceGroupChatParticipantList;
import org.ezuce.im.ui.EzuceGroupChatRoom;
import org.ezuce.im.ui.EzuceStatusBar;
import org.ezuce.im.ui.EzuceTranscriptWindow;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.spark.util.UIComponentRegistry;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class IMPlugin implements Plugin, ConfigureVoice {

	public IMPlugin() {
		// replace components asap
		UIComponentRegistry.registerContactItem(EzuceContactItem.class);
		UIComponentRegistry.registerContactInfoWindow(EzuceContactInfoWindow.class);
		UIComponentRegistry.registerContactGroup(EzuceContactGroup.class);
		UIComponentRegistry.registerContactList(EzuceContactList.class);
		UIComponentRegistry.registerStatusBar(EzuceStatusBar.class);
		UIComponentRegistry.registerCommandPanel(EzuceCommandPanel.class);
		UIComponentRegistry.registerTranscriptWindow(EzuceTranscriptWindow.class);
		UIComponentRegistry.registerChatRoom(EzuceChatRoom.class);
                UIComponentRegistry.registerGroupChatRoom(EzuceGroupChatRoom.class);
                UIComponentRegistry.registerGroupChatParticipantList(EzuceGroupChatParticipantList.class);
		UIComponentRegistry.registerChatContainer(EzuceChatContainer.class);
		// disable conferences tab
		SettingsManager.getLocalPreferences().setShowConferenceTab(false);
		
		MessageListenerRegistry.addNotifier(new IMMessageNotifier());
	}

	/**
	 * Called after Spark is loaded to initialize the new plugin.
	 */
	@Override
	public void initialize() {
            
		final Color ezuceBackground = new Color(250, 250, 251);
		final Workspace w = Workspace.getInstance();

		w.setBackground(ezuceBackground);
		w.getContactList().setBackground(ezuceBackground);
		final SparkTabbedPane pane = w.getWorkspacePane();
		pane.setBackground(ezuceBackground);

		addConferenceMenuItem();
        try {
			VoiceUtil.configure(this);
		} catch (Exception e) {
			Log.error("Error initialization ", e);
		}
	}

	/**
	 * Called when Spark is shutting down to allow for persistence of
	 * information or releasing of resources.
	 */
	@Override
	public void shutdown() {
		AsyncLoader.getInstance().shutdown();
	}

	/**
	 * Return true if the Spark can shutdown on users request.
	 *
	 * @return true if Spark can shutdown on users request.
	 */
	@Override
	public boolean canShutDown() {
		return true;
	}

	/**
	 * Is called when a user explicitly asked to uninstall this plugin. The
	 * plugin owner is responsible to clean up any resources and remove any
	 * components install in Spark.
	 */
	@Override
	public void uninstall() {
		// Remove all resources belonging to this plugin.
	}

	private void addConferenceMenuItem() {
		JMenuBar menuBar = SparkManager.getMainWindow().getJMenuBar();

		for (int i = 0; i < menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			if (Res.getString("menuitem.actions").equals(menu.getText())) {
				menu.addSeparator();
				menu.add(new ConferenceServicesAction());
			}
		}
	}

	@Override
	public void configureVoiceEnabled() {
	}

	@Override
	public void configureVoiceDisabled() {
	}
}
