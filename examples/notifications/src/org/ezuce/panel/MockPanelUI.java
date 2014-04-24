package org.ezuce.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.ezuce.notification.UniteNotification;
import org.ezuce.notification.UniteNotificationImpl;
import org.ezuce.notification.listener.NotificationClickListener;

import com.ezuce.MugshotPanel;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class MockPanelUI extends JFrame {

	private static final long serialVersionUID = -1794011744208706492L;

	/** Constants */
	private static final String BTN_CHANGE_PRESENCE_TEXT = "Change Presents";
	private static final String BTN_INCOMING_CALL_TEXT = "Incoming Call";
	private static final String BTN_INCOMING_TEXT = "Incoming Text";
	private static final String WINDOW_TITLE = "Mock Panel.";
	private static final Dimension WINDOW_SIZE = new Dimension(500, 100);
	private static final int LOCATION_X = 400;
	private static final int LOCATION_Y = 300;
	private static final Insets LEFT_PADDING = new Insets(0, 5, 0, 0);

	/** Components */
	private JButton showIncomingTextNotification;
	private JButton showIncomingCallNotification;
	private JButton showChangePresentNotification;

	/** */
	private UniteNotification popup;

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

		popup = new UniteNotificationImpl();

		showIncomingTextNotification = new JButton();
		showIncomingCallNotification = new JButton();
		showChangePresentNotification = new JButton();

		showIncomingTextNotification.setText(BTN_INCOMING_TEXT);
		showIncomingCallNotification.setText(BTN_INCOMING_CALL_TEXT);
		showChangePresentNotification.setText(BTN_CHANGE_PRESENCE_TEXT);

		showIncomingTextNotification
				.addActionListener(actionIncomingTextNotification());
		showIncomingCallNotification
				.addActionListener(actionCallTextNotification());
		showChangePresentNotification
				.addActionListener(actionChangePresentsNotification());

		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		add(showIncomingTextNotification, c);
		c.insets = LEFT_PADDING;
		add(showIncomingCallNotification, c);
		c.insets = LEFT_PADDING;
		add(showChangePresentNotification, c);

	}

	/**
	 * @return an action to show a notification which has incoming text on it"
	 */
	private ActionListener actionIncomingTextNotification() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				/*
				popup.showText("Mr Dursley", "Hey",
						clickOnIncomingTextNotificationAction());
				popup.showText("Mr Dursley", "How are you?",
						clickOnIncomingTextNotificationAction());
				*/
				popup.showText("Mr Dursley", "I do not understand. Type help",
						clickOnIncomingTextNotificationAction());
				popup.showText(
						"mybuddy",
						"Commands: call, find, missed, listen, pickup, conference, who, mute, unmute, disconnect, lock, unlock or any short forms",
						clickOnIncomingTextNotificationAction());

				popup.showText(
						"Mr Dursley",
						"South Korea on Monday clarified comments made by the country's "
								+ "unification minister about the possibility of a new nuclear "
								+ "test by North Korea.",
						clickOnIncomingTextNotificationAction());
				popup.showText(
						"Mr Dursley",
						"It said that although the minister, Ryoo Kihl-jae, had said that "
								+ "there we\nre indications that North Korea may be preparing to"
								+ " carry out its fourth nuclear test, there were no unusual "
								+ "signs at this point, suggesting a test wasn't imminent. "
								+ "What he meant was that North Korea has been continuously "
								+ "preparing for a nuclear test since its third nuclear test"
								+ " and that it is waiting for a political decision to carry out the test, "
								+ "the Unification Ministry said.",
						clickOnIncomingTextNotificationAction());
				popup.showText(
						"Mr Dursley",
						"It said that \n althoughthe \r minister \t, Ryoo Kihl-jae, had said that "
								+ "there were indications that North Korea may be preparing to"
								+ " carry out its fourth nuclear test, there were no unusual "
								+ "signs at this point, suggesting a test wasn't imminent. "
								+ "What he meant was that North Korea has been continuously "
								+ "preparing for a nuclear test since its third nuclear test"
								+ " and that it is waiting for a political decision to carry out the test, "
								+ "the Unification Ministry said."
								+ "It said that although the minister, Ryoo Kihl-jae, had said that "
								+ "there were indications that North Korea may be preparing to"
								+ " carry out its fourth nuclear test, there were no unusual "
								+ "signs at this point, suggesting a test wasn't imminent. "
								+ "What he meant was that North Korea has been continuously "
								+ "preparing for a nuclear test since its third nuclear test"
								+ " and that it is waiting for a political decision to carry out the test, "
								+ "the Unification Ministry said.",
						clickOnIncomingTextNotificationAction());
			}
		};
	}

	private ActionListener actionCallTextNotification() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				popup.showCall("Mr Dursley", "Software Developer",
						"987-654-432", new MugshotPanel(), acceptAction(),
						declineAction());

				Timer closeTimer = new Timer(10000, null);
				closeTimer.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						((Timer) e.getSource()).stop();
						popup.hideCall();
					}
				});
				closeTimer.start();
			}
		};
	}

	private ActionListener actionChangePresentsNotification() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				popup.showEvent("Mr Dursley", "Director of technical service",
						"Away", "/resources/images/away_16x16.png",
						new MugshotPanel());
			}
		};
	}

	private NotificationClickListener clickOnIncomingTextNotificationAction() {
		return new NotificationClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out
						.println("= Click on Incoming Text Notification. A chat will be opened =");
			}
		};
	}

	private ActionListener declineAction() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("= Click on Decline Button =");
			}
		};
	}

	private ActionListener acceptAction() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("= Click on Accept Button =");
			}
		};
	}
}
