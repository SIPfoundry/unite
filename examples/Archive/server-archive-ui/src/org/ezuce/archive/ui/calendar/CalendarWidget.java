package org.ezuce.archive.ui.calendar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.ezuce.archive.ui.ImageButton;
import org.ezuce.archive.ui.util.ArchiveUtil;

public class CalendarWidget extends JPanel {

	private static final long serialVersionUID = 8335453097726985111L;
	private CalendarPopup mCalendarPopup;
	private JButton mIcon;

	public CalendarWidget() {
		initComponents();
		initLayout();
	}

	private void initComponents() {
		setBackground(ArchiveUtil.gMainBgColor);

		mIcon = new ImageButton(ArchiveUtil.gCalendarImage,
				ArchiveUtil.gCalendarPressedImage,
				ArchiveUtil.gCalendarHoverImage);
		mIcon.setOpaque(false);
		ArchiveUtil.debugBorder(mIcon);

		mCalendarPopup = new CalendarPopup();
		mIcon.addActionListener(onCalendarBtnClick());
	}

	private void initLayout() {
		setLayout(new GridBagLayout());
		add(mIcon, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						1, 1, 1, 1), 0, 0));
	}

	@Override
	public void show() {
		mCalendarPopup.setLocation(getCalendarLocation());
		mCalendarPopup.setVisible(true);
	}

	@Override
	public void hide() {
		mCalendarPopup.setVisible(false);
	}

	private ActionListener onCalendarBtnClick() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mCalendarPopup.isVisible())
					hide();
				else
					show();
			}
		};
	}

	private Point getCalendarLocation() {
		Point p = mIcon.getLocationOnScreen();
		Point location = new Point(p.x + mIcon.getWidth(), p.y);
		return location;
	}

	public void addObserver(Observer o) {
		mCalendarPopup.addObserver(o);
	}

}
