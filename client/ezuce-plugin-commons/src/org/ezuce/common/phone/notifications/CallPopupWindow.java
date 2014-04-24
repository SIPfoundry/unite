package org.ezuce.common.phone.notifications;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.ezuce.common.ui.wrappers.interfaces.AudioCallPanelCommonInterface;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.media.ui.AudioCallMugshotPanel;
import org.ezuce.media.ui.GraphicUtils;
import org.ezuce.media.ui.WidgetBuilder;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class CallPopupWindow extends PopupWindow implements
		AudioCallPanelCommonInterface {

	private static final long serialVersionUID = 3519882317577633167L;
	private static final String CALL_POPUP_HEADER_TXT = "Incoming Call";
	private static final Icon ICON_CALL = GraphicUtils
			.createImageIcon("/resources/images/icon_call.png");
	private static final Color CALL_POPUP_BACKGROUND = new Color(215, 232, 235);

	private String phone;
	private JButton btnAnswer;
	private JButton btnAnswerWithVideo;
	private JButton btnDecline;
	private boolean m_video;

	public CallPopupWindow(boolean video) {
		m_video = video;
		setHeaderIcon(ICON_CALL);
		setHeaderText(CALL_POPUP_HEADER_TXT);

		btnAnswer = WidgetBuilder.createButton(
				"/resources/images/call_start.png",
				"/resources/images/call_start_on.png");
		if (m_video) {
			btnAnswerWithVideo = WidgetBuilder.createButton(
					"/resources/images/call_video_start.png",
					"/resources/images/call_video_start_on.png");
		}
		btnDecline = WidgetBuilder.createButton(
				"/resources/images/call_end.png",
				"/resources/images/call_end_on.png");
	}

	@Override
	protected Color getContentBackgroundColor() {
		return CALL_POPUP_BACKGROUND;
	}

	@Override
	protected Component getContentPanelContent() {
		JPanel mainPanel = new JPanel();
		// mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		mainPanel.setBackground(getContentBackgroundColor());
		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(
				getUserInfoPanel(getUserAvatar(), getFrom(), getTitle(), phone,
						"", null), c);

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(getButtonPanel(), c);
		return mainPanel;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void addOnAcceptAction(ActionListener onAcceptAction) {
		btnAnswer.addActionListener(onAcceptAction);
	}

	public void addOnAcceptVideoAction(ActionListener onAcceptAction) {
		if (btnAnswerWithVideo != null) {
			btnAnswerWithVideo.addActionListener(onAcceptAction);
		}
	}

	public void addOnDeclineAction(ActionListener onDeclineAction) {
		btnDecline.addActionListener(onDeclineAction);
	}

	private Component getButtonPanel() {
		// / buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		// buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		buttonPanel.setBackground(getContentBackgroundColor());

		GridBagConstraints c = new GridBagConstraints();

		int i = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = i++;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 10);
		buttonPanel.add(btnAnswer, c);

		if (m_video) {
			c.gridx = i++;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 10, 10);
			buttonPanel.add(btnAnswerWithVideo, c);
		}

		c.gridx = i++;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 10);
		buttonPanel.add(btnDecline, c);

		return buttonPanel;
	}

	@Override
	public void displayPhonebookUser(ContactListEntry cle) {
		this.setFrom(cle.getUserDisplayName());
		this.setPhone(cle.getDescription());
		// org.ezuce.common.ui.MugshotPanel mp = new
		// org.ezuce.common.ui.MugshotPanel(true);
		// mp.setMugshot(cle.getUserAvatar());
		AudioCallMugshotPanel mp = new AudioCallMugshotPanel();
		mp.setMugshot(cle.getUserAvatar());
		this.setUserAvatar(mp);
	}

	@Override
	public void displayNonPhonebookUser(String nbr) {
		this.setFrom(nbr);
	}

}
