package org.ezuce.archive.ui.calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import org.ezuce.archive.ui.ImageButton;
import org.ezuce.archive.ui.util.ArchiveUtil;

public class CalendarPopup extends Observable {

	private static final SimpleDateFormat gMMMMyyyy = new SimpleDateFormat(
			"MMMM yyyy");

	private JWindow mCalendarWindow;
	private String[] gShortDays;
	private JLabel mMonthBox;

	private Calendar mCurrentDate;
	private Calendar mSelectedDate;
	private JPanel mDaysPanel;
	private JComponent mTodayWidget;

	public CalendarPopup() {
		initComponents();
		initLayout();
		updateDate();
	}

	private void initComponents() {
		mCalendarWindow = new JWindow();
		mCalendarWindow.setBackground(ArchiveUtil.gMainCalendarColor);
		ArchiveUtil.debugBorder(mCalendarWindow.getRootPane());

		gShortDays = ArchiveUtil.removeEmpty(new DateFormatSymbols()
				.getShortWeekdays());

		mDaysPanel = new JPanel(new GridLayout(0, 7));
		mDaysPanel.setBackground(ArchiveUtil.gMainCalendarColor);

		ArchiveUtil.debugBorder(mDaysPanel);

		mCurrentDate = Calendar.getInstance();
		ArchiveUtil.normalizeCalendar(mCurrentDate);
		mSelectedDate = Calendar.getInstance();
		mSelectedDate.setTime(mCurrentDate.getTime());
	}

	private void initLayout() {
		mCalendarWindow.setLayout(new GridBagLayout());
		mCalendarWindow.add(getMonthPanel(), new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		mCalendarWindow.add(mDaysPanel, new GridBagConstraints(0, 1, 1, 1, 1,
				1, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	private void updateDate() {
		String currentDate = gMMMMyyyy.format(mCurrentDate.getTime());
		mMonthBox.setText(currentDate);
		updateDays();
	}

	private void updateDays() {
		int numberOfDaysInMonth = mCurrentDate
				.getActualMaximum(Calendar.DAY_OF_MONTH);

		mDaysPanel.removeAll();
		addWeekDayNames();
		addDays(numberOfDaysInMonth);

		mDaysPanel.validate();
		mDaysPanel.repaint();
	}

	private void addDays(int numberOfDaysInMonth) {
		Calendar calc = Calendar.getInstance();
		calc.setTime(mCurrentDate.getTime());
		calc.set(Calendar.DATE, 1);
		ArchiveUtil.normalizeCalendar(calc);

		int startOffset = calc.get(Calendar.DAY_OF_WEEK);
		for (int offset = 0; offset < startOffset - 1; offset++) {
			JPanel stub = new JPanel();
			stub.setBackground(ArchiveUtil.gMainCalendarColor);
			ArchiveUtil.debugBorder(stub);
			mDaysPanel.add(stub);
		}

		for (int day = startOffset; day < numberOfDaysInMonth + startOffset; day++) {
			String dayString = String.valueOf(calc.get(Calendar.DATE));
			JButton dayButton = new ImageButton(dayString);

			dayButton.setBackground(ArchiveUtil.gMainCalendarColor);
			dayButton.addActionListener(dayChangedAction());
			dayButton.setFont(ArchiveUtil.gFont);
			ArchiveUtil.debugBorder(dayButton);

			if (ArchiveUtil.isInFeature(calc)) {
				dayButton.setEnabled(false);
				dayButton.setForeground(Color.lightGray);
				// dayButton.setForeground(Color.black);
			} else {

				if (ArchiveUtil.isToday(calc)) {
					dayButton.setBackground(Color.white);
					dayButton.setForeground(Color.black);
					mTodayWidget = dayButton;
				}

				if (isDateSelected(calc)) {
					dayButton.setBackground(ArchiveUtil.gMainCalendarColor);
					dayButton.setForeground(Color.black);
					dayButton
							.setFont(dayButton.getFont().deriveFont(Font.BOLD));
				}
			}

			mDaysPanel.add(dayButton);
			calc.add(Calendar.DATE, 1);
		}
	}

	private void addWeekDayNames() {
		for (String weekDayName : gShortDays) {
			JLabel weekDayLabel = new JLabel(weekDayName.toUpperCase(),
					SwingConstants.CENTER);
			weekDayLabel.setFont(ArchiveUtil.gFont);
			ArchiveUtil.debugBorder(weekDayLabel);
			mDaysPanel.add(weekDayLabel);
		}
	}

	public void setVisible(boolean visible) {
		updateDate();
		mCalendarWindow.setVisible(visible);
		if (visible) {
			// mCalendarWindow.setAlwaysOnTop(visible);
			mCalendarWindow.toFront();
			// mCalendarWindow.requestFocus();
			if (mTodayWidget != null)
				mTodayWidget.requestFocus();

			mCalendarWindow.pack();
		}
	}

	public void setLocation(Point location) {
		mCalendarWindow.setLocation(location);
	}

	public boolean isVisible() {
		return mCalendarWindow.isVisible();
	}

	private JPanel getMonthPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBackground(ArchiveUtil.gMainCalendarColor);
		ArchiveUtil.debugBorder(p);

		JButton leftButton = new ImageButton(ArchiveUtil.gLeftSign);
		leftButton.setFont(ArchiveUtil.gFont);
		leftButton.setBackground(ArchiveUtil.gMainCalendarColor);
		leftButton.addActionListener(changeMonthAction(-1));

		JButton rightButton = new ImageButton(ArchiveUtil.gRightSign);
		rightButton.setFont(ArchiveUtil.gFont);
		rightButton.setBackground(ArchiveUtil.gMainCalendarColor);
		rightButton.addActionListener(changeMonthAction(1));

		JButton closeButton = new ImageButton(ArchiveUtil.gCloseSmallImage);
		closeButton.setBackground(ArchiveUtil.gMainCalendarColor);
		closeButton.addActionListener(closeAction());

		mMonthBox = new JLabel();
		mMonthBox.setHorizontalAlignment(SwingConstants.CENTER);

		p.add(leftButton, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						2, 0, 2), 0, 0));
		p.add(mMonthBox, new GridBagConstraints(1, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 2, 0, 2), 0, 0));
		p.add(rightButton, new GridBagConstraints(2, 0, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
						2, 0, 2), 0, 0));
		p.add(closeButton, new GridBagConstraints(3, 0, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,
						2, 2, 4), 0, 0));
		return p;
	}

	private ActionListener changeMonthAction(final int step) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mCurrentDate.add(Calendar.MONTH, step);
				updateDate();
			}
		};
	}

	private ActionListener dayChangedAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object dayButton = e.getSource();
				if (dayButton instanceof JButton) {

					String dayString = ((JButton) dayButton).getText();
					mCurrentDate.set(Calendar.DATE, Integer.valueOf(dayString));
					mSelectedDate.setTime(mCurrentDate.getTime());
					dateChanged();
					setVisible(false);
				}

			}

		};
	}

	private void dateChanged() {
		setChanged();
		notifyObservers(mSelectedDate.getTime());
	}

	private boolean isDateSelected(Calendar calc) {
		return mSelectedDate.compareTo(calc) == 0;
	}

	private ActionListener closeAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		};
	}

}
