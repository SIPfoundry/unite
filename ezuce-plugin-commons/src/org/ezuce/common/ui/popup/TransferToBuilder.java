package org.ezuce.common.ui.popup;

import java.util.Collections;
import java.util.List;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;

import org.ezuce.common.resource.Config;
import org.ezuce.common.rest.ActiveCdr;
import org.ezuce.common.rest.RestManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

public class TransferToBuilder extends SwingWorker<List<ActiveCdr>, Void> {
	private final JMenu menu;
	private final Action action;
	private final String number;
	private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

	public TransferToBuilder(JMenu menu, Action action, String number) {
		this.menu = menu;
		this.action = action;
		this.number = number;
	}

	@Override
	protected List<ActiveCdr> doInBackground() {
		List<ActiveCdr> result;

		try {
			result = RestManager.getInstance().getActiveCalls();
		} catch (Exception e) {
			e.printStackTrace();
			result = Collections.emptyList();
		}

		return result;
	}

	@Override
	protected void done() {
		menu.removeAll();

		try {
			final List<ActiveCdr> cdrs = get();

			for (ActiveCdr cdr : cdrs) {
				//The transfer destination has to be different than the callee party
				//and only calls initiated by the logged in user can be transfered
				String sipUserId = Config.getInstance().getSipUserId();
				String callee = cdr.getCallee();
				String caller = cdr.getCaller();
				boolean iAmCaller = cdr.getCaller().equals(sipUserId);
				if (!callee.equals(number)  && !caller.equals(number) && !callee.equals(caller)) {
					JMenuItem jmi = new JMenuItem(action);
					if (iAmCaller) {
						jmi.setActionCommand(cdr.getCallee());
						jmi.setText(resourceMap.getString("active.outgoing.call", cdr.getCalleeAor()));
					} else {
						jmi.setActionCommand(cdr.getCaller());
						jmi.setText(resourceMap.getString("active.outgoing.call", cdr.getCallerAor()));
					}
					menu.add(jmi);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (menu.getMenuComponentCount() == 0) {
			JMenuItem dummyJmi = new JMenuItem(resourceMap.getString("no.transferable.active.calls"));
			dummyJmi.setEnabled(false);
			menu.add(dummyJmi);
		}
	}

}
