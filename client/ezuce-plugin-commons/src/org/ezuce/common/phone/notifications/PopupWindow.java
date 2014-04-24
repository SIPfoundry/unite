/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.sf.fmj.media.Log;

import org.ezuce.media.ui.GraphicUtils;
import org.jivesoftware.spark.util.TaskEngine;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public abstract class PopupWindow extends JWindow {

	protected static final int HEADER_HEIGHT = 30;
	private static final Color COLOR_HEADER = new Color(80, 80, 80);
	private static final Insets INSETS_RIGHT = new Insets(0, 0, 0, 5);
	private static final Insets INSETS_LEFT = new Insets(0, 5, 0, 0);
	private static final String IMAGE_CLOSE_BTN = "/resources/images/close_btn.png";
	private static final Dimension SIZE_CLOSE_BTN = new Dimension(14, 14);
	private static final long serialVersionUID = -5936198123096434043L;
	private static final Font DEFAULT_HEADER_FONT = new Font("Arial",
			Font.PLAIN, 12);

	protected static Dimension SIZE = new Dimension(250, 200);
	protected static final Color COLOR_FROM = new Color(56, 88, 104);
	protected static final Font FONT_FROM = new Font("Arial", Font.BOLD, 16);
	protected static final Font FONT_TEXT = new Font("Arial", Font.PLAIN, 14);
	protected static final Color COLOR_TEXT = new Color(127, 127, 127);
	private boolean isOpen = false;

	private String headerText;
	private Icon headerIcon;
	private String from;
	private JButton closeButton;
	private Component userAvatar;
	private String title;
	private String status;
	private ActionListener closeButtonAction;
	private String location;

	public PopupWindow() {
		// setSize(SIZE);
		setAlwaysOnTop(true);
	}

	protected Component getHeaderPanel() {
		JPanel panel = new JPanel();

		panel.setFont(DEFAULT_HEADER_FONT);
		panel.setBackground(getHeaderBackgroundColor());
		panel.setPreferredSize(new Dimension(getSize().width, getHeaderHeight()));
		panel.setMinimumSize(new Dimension(getSize().width, getHeaderHeight()));
		panel.setMaximumSize(new Dimension(getSize().width, getHeaderHeight()));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.insets = INSETS_LEFT;

		panel.add(new JLabel(headerIcon), c);

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 2;
		JLabel headerTextLabel = new JLabel(headerText, SwingConstants.LEFT);
		headerTextLabel.setForeground(Color.WHITE);

		panel.add(headerTextLabel, c);

		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = INSETS_RIGHT;
		panel.add(closeButton, c);

		return panel;
	}

	protected abstract Component getContentPanelContent();

	protected abstract Color getContentBackgroundColor();

	protected Color getHeaderBackgroundColor() {
		return COLOR_HEADER;
	}

	protected int getHeaderHeight() {
		return HEADER_HEIGHT;
	}

	public void setHeaderIcon(Icon icon) {
		this.headerIcon = icon;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom() {
		return from;
	}

	public Component getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(Component userAvatar) {
		this.userAvatar = userAvatar;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	public void setLocationName(String locationName) {
		this.location = locationName;
	}
	
	public String getLocationName() {
		return location;
	}

	public void showWindow(Position pos) {
		initLayout();
		setLocation(pos.getPosition(this));
		setVisible(true);
		isOpen = true;
	}

	public void closeWindow() {
		isOpen = false;
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
			public void run() {
				dispose();
			}
		});
	}

	public boolean isOpen() {
		return isOpen;
	}

	protected Component getUserInfoPanel(Component userAvatar, String userName,
			String title, String status, String location, NotificationClickListener onClickAction) {

		if (userAvatar != null) {
			userAvatar.addMouseListener(onClickAction);
		}

		JPanel panel = new JPanel();
		panel.addMouseListener(onClickAction);

		panel.setBackground(getContentBackgroundColor());
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		if (userAvatar != null) {
			// mugshot
			c.anchor = GridBagConstraints.NORTHWEST;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 4;
			c.weighty = 0.2;
			c.insets = new Insets(10, 10, 10, 10);
			panel.add(userAvatar, c);
		}
		// name
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.weightx = 0.5;
		c.weighty = 0;
		c.insets = new Insets(10, 0, 0, 10);
		JLabel fromLabel = new JLabel(userName);
		fromLabel.addMouseListener(onClickAction);
		fromLabel.setFont(FONT_FROM);
		fromLabel.setForeground(COLOR_FROM);
		panel.add(fromLabel, c);

		// title
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 1;
		c.weighty = 0;
		c.insets = new Insets(2, 0, 0, 10);
		JLabel titleLabel = new JLabel(title);
		titleLabel.addMouseListener(onClickAction);
		titleLabel.setFont(FONT_TEXT);
		titleLabel.setForeground(COLOR_TEXT);
		panel.add(titleLabel, c);

		// status or phone
		c.gridx = 1;
		c.gridy = 2;
		c.gridheight = 1;
		c.weighty = 0;
		c.insets = new Insets(2, 0, 2, 10);
		JLabel statusLabel = new JLabel(status);
		statusLabel.addMouseListener(onClickAction);
		statusLabel.setFont(FONT_TEXT);
		statusLabel.setForeground(COLOR_TEXT);
		panel.add(statusLabel, c);
		
		// status or phone
		c.gridx = 1;
		c.gridy = 3;
		c.gridheight = 1;
		c.weighty = 0;
		c.insets = new Insets(2, 0, 2, 10);
		JLabel locationLabel = new JLabel(location);
		locationLabel.addMouseListener(onClickAction);
		locationLabel.setFont(FONT_TEXT);
		locationLabel.setForeground(COLOR_TEXT);
		panel.add(locationLabel, c);
		
		return panel;
	}

	private void initLayout() {
		initComponents();

		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		add(getHeaderPanel(), c);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;

		add(getContentPanelContent(), c);
		pack();
	}

	private void initComponents() {
		closeButton = new JButton(GraphicUtils.createImageIcon(IMAGE_CLOSE_BTN));
		closeButton.setPreferredSize(SIZE_CLOSE_BTN);
		closeButton.setMinimumSize(SIZE_CLOSE_BTN);
		closeButton.setMaximumSize(SIZE_CLOSE_BTN);
		closeButton.setBorderPainted(false);

		if (closeButtonAction != null) {
			closeButton.addActionListener(closeButtonAction);
		} else {
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					closeWindow();
				}
			});
		}
	}

	public void setCloseButtonAction(ActionListener action) {
		closeButtonAction = action;
	}

}
