package org.ezuce.archive.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ezuce.archive.ui.util.ArchiveUtil;

class ResultNavigationPanel extends JPanel {

	private static final long serialVersionUID = -448438486552225662L;
	private static final MessageFormat gSearchTextFormat = new MessageFormat(
			"\"{0}\" {1}/{2}");
	private final JButton mBtnPrev = new ImageButton(ArchiveUtil.gLeftSign);
	private final JButton mBtnNext = new ImageButton(ArchiveUtil.gRightSign);
	private JLabel mLabel;
	private int mCurrentSearchNumber = 1;
	private int mMatchCount;
	private final NavigationPanelObservable gObserver = new NavigationPanelObservable();
	private String mText;

	public ResultNavigationPanel() {
		initComponents();
		initLayout();
	}

	private void initComponents() {
		setBackground(ArchiveUtil.gMainBgColor);
		setFont(ArchiveUtil.gFont);
		mLabel = new JLabel();
		mLabel.setFont(ArchiveUtil.gFont);

		mBtnPrev.addActionListener(prevAction());
		mBtnNext.addActionListener(nextAction());
	}

	private void initLayout() {
		add(mBtnPrev);
		add(mLabel);
		add(mBtnNext);
	}

	public void setSearch(String text, int matchCount) {
		this.mMatchCount = matchCount;
		this.mText = text;

		if (mCurrentSearchNumber > mMatchCount)
			mCurrentSearchNumber = mMatchCount;

		if (mCurrentSearchNumber == 0 && mMatchCount != 0)
			mCurrentSearchNumber = 1;

		String searchText = gSearchTextFormat.format(new Object[] { mText,
				mCurrentSearchNumber, mMatchCount });

		mLabel.setText(searchText);

		boolean isVisible = mMatchCount == 0 ? false : true;
		mBtnPrev.setVisible(isVisible);
		mBtnNext.setVisible(isVisible);

		setVisible(true);
	}

	public void addOvserver(Observer obj) {
		gObserver.addObserver(obj);
	}

	private ActionListener nextAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				increase(1);
				notifyClients();
				setSearch(mText, mMatchCount);
			}
		};
	}

	private ActionListener prevAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				increase(-1);
				notifyClients();
				setSearch(mText, mMatchCount);
			}
		};
	}

	private void increase(int i) {
		mCurrentSearchNumber += i;

		if (mCurrentSearchNumber > mMatchCount) {
			mCurrentSearchNumber = 1;
			return;
		}

		if (mCurrentSearchNumber < 1) {
			mCurrentSearchNumber = mMatchCount;
			return;
		}
	}

	private void notifyClients() {
		gObserver.setChanged();
		gObserver.notifyObservers(new NavigationPanelEvent(
				mCurrentSearchNumber, mMatchCount));
	}

	public void reset() {
		mCurrentSearchNumber = 0;
		mMatchCount = 0;
		mText = "";
		setVisible(false);
	}

	public class NavigationPanelObservable extends Observable {

		@Override
		protected synchronized void setChanged() {
			super.setChanged();
		}
	}

	public static class NavigationPanelEvent {

		private int mCurrentNumber;
		private int mMatchCount;

		public NavigationPanelEvent(int current, int matchCount) {
			this.mCurrentNumber = current;
			this.mMatchCount = matchCount;
		}

		public int getCurrentNumber() {
			return mCurrentNumber;
		}

		public int getMatchCount() {
			return mMatchCount;
		}
	}

	public void setInfo(String text) {
		mLabel.setText(text);
		mBtnPrev.setVisible(false);
		mBtnNext.setVisible(false);
		setVisible(true);
	}

}
