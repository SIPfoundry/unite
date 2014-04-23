package org.ezuce.call.events;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;

import org.ezuce.common.event.ForwarderListener;
import org.ezuce.common.ui.CounterLabel;
import org.ezuce.panels.CallPanel2;
import org.ezuce.wrappers.interfaces.HistoryItem.HistoryItemTypes;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;

public class ForwarderListenerImpl implements ForwarderListener {
	private final CallPanel2 panel;

	public ForwarderListenerImpl(CallPanel2 panel) {
		this.panel = panel;
	}

	@Override
	public void missedCallsClicked(AWTEvent evt) {
		handleEvent(evt, HistoryItemTypes.MISSED_CALL.name());
	}

	@Override
	public void voiceMessageClicked(AWTEvent evt) {
		handleEvent(evt, HistoryItemTypes.VOICE_MAIL.name());
		CounterLabel.getMissedVoicemailsLabel().refresh();
	}

        /**
         * Treats the mouseClicked event. It uses the event argument to obtain the
         * event source and the event ID. It uses the command argument to configure
         * what to filter.
         * @param evt
         * @param command
         */
	private void handleEvent(AWTEvent evt, String command) {
		final SparkTabbedPane tabbedPane = Workspace.getInstance().getWorkspacePane();
		boolean found = false;
		panel.getCallHistoryPanel().setReload(false);
		
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			final Component comp = tabbedPane.getComponentAt(i);

			if (comp instanceof SparkTabbedPane) {
				final SparkTabbedPane stp = (SparkTabbedPane) comp;

				for (int j = 0; j < stp.getComponentCount(); j++) {
					final Component comp2 = stp.getComponent(j);

					if (panel.equals(comp2)) {
						tabbedPane.setSelectedIndex(i);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
        ActionEvent ae=new ActionEvent(evt.getSource(), evt.getID(),command);        
		panel.BringUpCallHistoryPanel(ae);		
	}
}
