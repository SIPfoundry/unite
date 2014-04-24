package org.ezuce.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.ezuce.PresenceManager;
import com.ezuce.tray.TrayManager;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class MockPanelUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = -1794011744208706492L;

	/** Constants */

	private static final String WINDOW_TITLE = "Tray Manager";
	private static final Dimension WINDOW_SIZE = new Dimension(300, 400);
	private static final int LOCATION_X = 400;
	private static final int LOCATION_Y = 200;

	private static final String BTN_ONLINE = "Online";
	private static final String BTN_FREE_TO_CHAT = "Free to chat";
	private static final String BTN_AWAY = "Away";
	private static final String BTN_EXTENDED_AWAY = "Extended Away";
	private static final String BTN_ON_THE_PHONE = "On the phone";
	private static final String BTN_DO_NOT_DISTURB = "Do not disturb";
	private static final String BTN_INVISIBLE = "Invisible";
	private static final String BTN_REGISTER = "Register";
	private static final String BTN_UNREGISTER = "Unregister";
	private static final String BTN_NEW_MESSAGE = "New Message";
	private static final String BTN_TYPING = "Typing";

	private static final String BTN_BRB = "Brb";
	private static final String BTN_BUSY = "Busy";
	private static final String BTN_MEETING = "In a meeting";
	private static final String BTN_OFF_WORK = "Off Work";

	/** Components */
	// group 1
	private JRadioButton btnOnline;
	private JRadioButton btnFreeToChat;
	private JRadioButton btnAway;
	private JRadioButton btnExtendedAway;
	private JRadioButton btnOnThePhone;
	private JRadioButton btnDoNotDisturb;
	private JRadioButton btnInvisible;
	// group 2
	private JRadioButton btnRegister;
	private JRadioButton btnUnregister;
	// events
	private JButton btnNewMessage;
	private JButton btnTyping;

	private JRadioButton btnBrb;
	private JRadioButton btnBusy;
	private JRadioButton btnMeeting;
	private JRadioButton btnOffwork;

	/**
	 * Default ctor.
	 */
	public MockPanelUI() {
		initComponents();
	}

	/**
	 * Initialize all components.
	 */
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(WINDOW_TITLE);
		setSize(WINDOW_SIZE);
		setResizable(false);
		setLocation(LOCATION_X, LOCATION_Y);

		btnOnline = new JRadioButton();
		btnFreeToChat = new JRadioButton();
		btnAway = new JRadioButton();
		btnExtendedAway = new JRadioButton();
		btnOnThePhone = new JRadioButton();
		btnDoNotDisturb = new JRadioButton();
		btnInvisible = new JRadioButton();
		btnRegister = new JRadioButton();
		btnUnregister = new JRadioButton();
		btnNewMessage = new JButton();
		btnTyping = new JButton();

		btnBrb = new JRadioButton();
		btnBusy = new JRadioButton();
		btnMeeting = new JRadioButton();
		btnOffwork = new JRadioButton();

		btnOnline.setText(BTN_ONLINE);
		btnOnline.setActionCommand(BTN_ONLINE);
		btnOnline.setSelected(true);
		btnOnline.addActionListener(this);

		btnFreeToChat.setText(BTN_FREE_TO_CHAT);
		btnFreeToChat.setActionCommand(BTN_FREE_TO_CHAT);
		btnFreeToChat.addActionListener(this);

		btnAway.setText(BTN_AWAY);
		btnAway.setActionCommand(BTN_AWAY);
		btnAway.addActionListener(this);

		btnBrb.setText(BTN_BRB);
		btnBrb.setActionCommand(BTN_BRB);
		btnBrb.addActionListener(this);

		btnBusy.setText(BTN_BUSY);
		btnBusy.setActionCommand(BTN_BUSY);
		btnBusy.addActionListener(this);

		btnOnThePhone.setText(BTN_ON_THE_PHONE);
		btnOnThePhone.setActionCommand(BTN_ON_THE_PHONE);
		btnOnThePhone.addActionListener(this);

		btnDoNotDisturb.setText(BTN_DO_NOT_DISTURB);
		btnDoNotDisturb.setActionCommand(BTN_DO_NOT_DISTURB);
		btnDoNotDisturb.addActionListener(this);

		btnOnThePhone.setText(BTN_ON_THE_PHONE);
		btnOnThePhone.setActionCommand(BTN_ON_THE_PHONE);
		btnOnThePhone.addActionListener(this);

		btnExtendedAway.setText(BTN_EXTENDED_AWAY);
		btnExtendedAway.setActionCommand(BTN_EXTENDED_AWAY);
		btnExtendedAway.addActionListener(this);

		btnMeeting.setText(BTN_MEETING);
		btnMeeting.setActionCommand(BTN_MEETING);
		btnMeeting.addActionListener(this);

		btnOffwork.setText(BTN_OFF_WORK);
		btnOffwork.setActionCommand(BTN_OFF_WORK);
		btnOffwork.addActionListener(this);

		btnInvisible.setText(BTN_INVISIBLE);
		btnInvisible.setActionCommand(BTN_INVISIBLE);
		btnInvisible.addActionListener(this);

		btnRegister.setText(BTN_REGISTER);
		btnRegister.setActionCommand(BTN_REGISTER);
		btnRegister.addActionListener(this);

		btnUnregister.setText(BTN_UNREGISTER);
		btnUnregister.setActionCommand(BTN_UNREGISTER);
		btnUnregister.addActionListener(this);
		btnUnregister.setSelected(true);

		btnNewMessage.setText(BTN_NEW_MESSAGE);
		btnNewMessage.setActionCommand(BTN_NEW_MESSAGE);
		btnNewMessage.addActionListener(this);

		btnTyping.setText(BTN_TYPING);
		btnTyping.setActionCommand(BTN_TYPING);
		btnTyping.addActionListener(this);

		// group 1
		ButtonGroup statusGroup = new ButtonGroup();
		statusGroup.add(btnOnline);
		statusGroup.add(btnFreeToChat);
		statusGroup.add(btnAway);
		statusGroup.add(btnBrb);
		statusGroup.add(btnBusy);
		statusGroup.add(btnDoNotDisturb);
		statusGroup.add(btnOnThePhone);
		statusGroup.add(btnExtendedAway);
		statusGroup.add(btnMeeting);
		statusGroup.add(btnOffwork);
		statusGroup.add(btnInvisible);

		JPanel statusPanel = new JPanel(new GridLayout(0, 1));
		statusPanel.setBorder(BorderFactory.createTitledBorder("Presense"));

		statusPanel.add(btnOnline);
		statusPanel.add(btnFreeToChat);
		statusPanel.add(btnAway);
		statusPanel.add(btnBrb);
		statusPanel.add(btnBusy);
		statusPanel.add(btnDoNotDisturb);
		statusPanel.add(btnOnThePhone);
		statusPanel.add(btnExtendedAway);
		statusPanel.add(btnMeeting);
		statusPanel.add(btnOffwork);
		statusPanel.add(btnInvisible);

		// group 2
		ButtonGroup registerGroup = new ButtonGroup();
		registerGroup.add(btnRegister);
		registerGroup.add(btnUnregister);

		JPanel registerPanel = new JPanel(new GridLayout(0, 1));
		registerPanel.setBorder(BorderFactory
				.createTitledBorder("Registration"));
		registerPanel.add(btnRegister);
		registerPanel.add(btnUnregister);

		// events
		ButtonGroup eventsGroup = new ButtonGroup();
		eventsGroup.add(btnNewMessage);
		eventsGroup.add(btnTyping);

		JPanel eventsPanel = new JPanel(new GridLayout(1, 2));
		eventsPanel.setBorder(BorderFactory.createTitledBorder("Events"));
		eventsPanel.add(btnNewMessage);
		eventsPanel.add(btnTyping);

		// layout
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		add(statusPanel, c);

		c.gridx = 1;
		c.gridy = 0;
		add(registerPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(eventsPanel, c);

		// create

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("TODO: ActionListener#actionPerformed() "
				+ e.getActionCommand());

		if (BTN_ONLINE.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(1));

		} else if (BTN_FREE_TO_CHAT.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(0));

		} else if (BTN_AWAY.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(2));

		} else if (BTN_EXTENDED_AWAY.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(7));

		} else if (BTN_ON_THE_PHONE.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(6));

		} else if (BTN_DO_NOT_DISTURB.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(5));

		} else if (BTN_INVISIBLE.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(10));

		} else if (BTN_BRB.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(3));

		} else if (BTN_BUSY.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(4));

		} else if (BTN_MEETING.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(8));

		} else if (BTN_OFF_WORK.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(PresenceManager.PRESENCES.get(9));

		} else if (BTN_REGISTER.equals(e.getActionCommand())) {
			TrayManager.getInstance().updateRegisteredAsPhone(true);

		} else if (BTN_UNREGISTER.equals(e.getActionCommand())) {
			TrayManager.getInstance().updateRegisteredAsPhone(false);

		} else if (BTN_NEW_MESSAGE.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(TrayManager.TrayEvent.NEW_MESSAGE);

		} else if (BTN_TYPING.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(TrayManager.TrayEvent.TYPING);

		} else if (BTN_TYPING.equals(e.getActionCommand())) {
			TrayManager.getInstance().update(TrayManager.TrayEvent.TYPING);

		}
	}
}
