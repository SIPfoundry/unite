package org.ezuce.im.action;

import java.awt.event.ActionEvent;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.ezuce.im.ui.ContactServicesWindow;
import org.jivesoftware.resource.Res;

public class ConferenceServicesAction extends AbstractAction {
	private static final long serialVersionUID = -7129541456484899987L;

	public ConferenceServicesAction() {
		super(Res.getString("conference.services"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFrame conferenceServices = new ContactServicesWindow();

		conferenceServices.pack();

		final Dimension size = conferenceServices.getPreferredSize();

		conferenceServices.setSize(new Dimension(size.width + 25, size.height));
		conferenceServices.setPreferredSize(new Dimension(size.width + 25,
				size.height));

		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		final int w = conferenceServices.getSize().width;
		final int h = conferenceServices.getSize().height;
		final int x = (dim.width - w) / 2;
		final int y = (dim.height - h) / 2;

		conferenceServices.setLocation(x, y);
		conferenceServices.setVisible(true);
	}
}
