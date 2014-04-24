package org.ezuce.archive.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.ezuce.archive.ArchiveController;
import org.ezuce.archive.impl.local.LocalHistoryFileController;
import org.ezuce.archive.model.xml.Chat;
import org.ezuce.archive.ui.ResultNavigationPanel.NavigationPanelEvent;
import org.ezuce.archive.ui.calendar.CalendarWidget;
import org.ezuce.archive.ui.util.ArchiveUtil;

public class SearchPanel extends JPanel implements Observer {

	// private static final Dimension gPanelSize = new Dimension(1, 40);
	private static final long serialVersionUID = -9090079839144650067L;

	private SearchField mSearchField;
	private CalendarWidget mCalendarWidget;
	private ResultNavigationPanel mResultNavigationPanel;
	private JButton mBtnClose;
	// private ProgressPanel mProgressPanel;
	private ArchiveController mArchiveController;
	private SearchPanelObservable mSearchPanelObservable = new SearchPanelObservable();

	public SearchPanel(String userHomeDir, String with) {
		ArchiveUtil.debugBorder(this);
		initComponents();
		initLayout();
		showProgress(false);
		mArchiveController = new LocalHistoryFileController(userHomeDir, with);
		super.setVisible(false);
		setBackground(ArchiveUtil.gMainBgColor);
	}

	private void initComponents() {
		mSearchField = new SearchField();
		mSearchField.addActionListener(searchActionListner());
		ArchiveUtil.debugBorder(mSearchField);

		mCalendarWidget = new CalendarWidget();
		mCalendarWidget.addObserver(this);
		ArchiveUtil.debugBorder(mCalendarWidget);

		mResultNavigationPanel = new ResultNavigationPanel();
		mResultNavigationPanel.setVisible(false);
		mResultNavigationPanel.addOvserver(this);
		ArchiveUtil.debugBorder(mResultNavigationPanel);

		mBtnClose = new ImageButton(ArchiveUtil.gCloseImage,
				ArchiveUtil.gCloseHoverImage, ArchiveUtil.gCloseHoverImage);
		mBtnClose.addActionListener(closeAction());
		// mProgressPanel = new ProgressPanel();
	}

	private void initLayout() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(ArchiveUtil.gMainBorderColor));

		JPanel panel1 = new JPanel(new GridBagLayout());
		// panel1.setMinimumSize(gPanelSize);

		panel1.setBackground(ArchiveUtil.gMainBgColor);

		ArchiveUtil.debugBorder(panel1);
		panel1.add(mSearchField, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		panel1.add(mCalendarWidget, new GridBagConstraints(1, 0, 1, 0, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 5, 0, 5), 0, 0));
		panel1.add(mResultNavigationPanel, new GridBagConstraints(2, 0, 1, 1,
				1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		panel1.add(mBtnClose, new GridBagConstraints(3, 0, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1,
						1, 1, 10), 0, 0));

		//
		add(panel1, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		/*
		 * add(mProgressPanel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
		 * GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new
		 * Insets(0, 0, 0, 0), 0, 0));
		 */

	}

	public void showProgress(boolean visible) {
		// mProgressPanel.setVisible(visible);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof NavigationPanelEvent) {
			moveToTextOccurence(arg);
		}
	}

	public void moveToTextOccurence(Object arg) {
		NavigationPanelEvent event = (NavigationPanelEvent) arg;
		mSearchPanelObservable.setChanged();
		mSearchPanelObservable.notifyObservers(SearchPanelEvent
				.moveToNextOccurrenceEvent(event.getCurrentNumber() - 1));
	}

	public void toggleHistoryPanelVisibility() {
		setVisible(!isVisible());
	}

	// ##### service methods #####

	public List<Chat> retreiveHistory(String with, long start, long after) {
		return mArchiveController.listCollections(null, with, start, after,
				null);
	}

	public List<Chat> retreiveLast(String with, int limit) {
		return mArchiveController.listCollectionsWithLimit(null, with, limit);
	}

	private void close() {
		setVisible(false);
		mResultNavigationPanel.reset();
		mSearchField.reset();
		clearHighlights();
		mCalendarWidget.hide();
	}

	private void search(String text) {
		// showProgress(true);
		clearHighlights();
		searchText(text);
		// showProgress(false);
	}

	@Override
	public void setVisible(boolean aFlag) {
		sendVisibilityChangedEvent(aFlag);
		if (!aFlag)
			setInfoText(ArchiveUtil.gEmpty);
		super.setVisible(aFlag);
	}

	public void setInfoText(String text) {
		mResultNavigationPanel.setInfo(text);
	}

	// #### listeners #####
	private ActionListener searchActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String text = mSearchField.getText();
				if (!ArchiveUtil.hasText(text)) {
					showProgress(false);
					return;
				}
				search(text);
			}
		};
	}

	private ActionListener closeAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		};
	}

	public void setSearchText(String text, int matchCount) {
		mResultNavigationPanel.setSearch(text, matchCount);
	}

	public List<Chat> searchFor(String with, String text) {
		return mArchiveController.search(with, text);
	}

	// #### observers #####

	public void addCalendarObserver(Observer ob) {
		mCalendarWidget.addObserver(ob);
	}

	public void addSearchPanelObserver(Observer ob) {
		mSearchPanelObservable.addObserver(ob);
	}

	// #### events ####

	private void sendVisibilityChangedEvent(boolean aFlag) {
		mSearchPanelObservable.setChanged();
		mSearchPanelObservable
				.notifyObservers(aFlag ? SearchPanelEvent.PANEL_VISIBLE_EVENT
						: SearchPanelEvent.PANEL_HIDDEN_EVENT);
	}

	private void clearHighlights() {
		mSearchPanelObservable.setChanged();
		mSearchPanelObservable
				.notifyObservers(SearchPanelEvent.CLEAR_HIGHLIGHT_EVENT);
	}

	private void searchText(String text) {
		mSearchPanelObservable.setChanged();
		mSearchPanelObservable.notifyObservers(SearchPanelEvent
				.searchForText(text));
	}

	public void enableDebugOutput(boolean enable) {
		mArchiveController.enableDebugOutput(enable);
	}

	// ###### Helper classes

	public static class SearchPanelEvent {
		public static final int PANEL_VISIBLE = 0;
		public static final int PANEL_HIDDEN = 1;
		public static final int MOVE_TO_NEXT_OCCURRENCE = 2;
		public static final int MOVE_TO_PREV_OCCURRENCE = 3;
		public static final int CLEAR_HIGHLIGHTS = 4;
		public static final int HIGHLIGHT_OCCURRENCE = 5;
		public static final int SEARCH_FOR_TEXT = 6;

		// events //
		public static final SearchPanelEvent CLEAR_HIGHLIGHT_EVENT = new SearchPanelEvent(
				SearchPanelEvent.CLEAR_HIGHLIGHTS);
		public static final SearchPanelEvent PANEL_VISIBLE_EVENT = new SearchPanelEvent(
				SearchPanelEvent.PANEL_VISIBLE);
		public static final SearchPanelEvent PANEL_HIDDEN_EVENT = new SearchPanelEvent(
				SearchPanelEvent.PANEL_HIDDEN);

		public static SearchPanelEvent searchForText(String text) {
			return new SearchPanelEvent(SEARCH_FOR_TEXT, text);
		}

		public static SearchPanelEvent moveToNextOccurrenceEvent(Integer pos) {
			return new SearchPanelEvent(MOVE_TO_NEXT_OCCURRENCE, pos);
		}

		private final Object[] mData;
		private final int mType;

		public SearchPanelEvent(int type, Object... data) {
			this.mType = type;
			this.mData = data;
		}

		public int getType() {
			return mType;
		}

		public Object[] getData() {
			return mData;
		}
	}

	private class SearchPanelObservable extends Observable {

		@Override
		protected synchronized void setChanged() {
			super.setChanged();
		}
	}

}
