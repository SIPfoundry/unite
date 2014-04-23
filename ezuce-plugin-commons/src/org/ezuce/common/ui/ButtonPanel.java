package org.ezuce.common.ui;

import java.awt.FlowLayout;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.ezuce.common.EzuceButtonFactory;
import org.jivesoftware.resource.Default;

public class ButtonPanel extends JPanel {
	private static final long serialVersionUID = -9136603724978040768L;

	private final JToggleButton connect;

	public ButtonPanel() {
		setLayout(new FlowLayout(FlowLayout.LEADING));
		setOpaque(false);
		EzuceButtonFactory factory = EzuceButtonFactory.getInstance();

		connect = factory.createConnectButton();
		add(connect);
		add(factory.createGroupChatButton());
		add(factory.createDownloadsButton());
	}

	public void setConnectListener(MouseListener listener) {
		connect.addMouseListener(listener);
	}

	public JToggleButton getConnectButton() {
		return connect;
	}
}
