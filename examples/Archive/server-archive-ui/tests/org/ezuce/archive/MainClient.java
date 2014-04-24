package org.ezuce.archive;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.ezuce.archive.ui.UISearchManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class MainClient {

	private static final MessageFormat gMessageFormat = new MessageFormat(
			"<tr><td width='200'>({0})</td><td width='130'>{1}:</td><td>{2}</td></tr>");

	private static final SimpleDateFormat gHistoryHeaderFormat = new SimpleDateFormat(
			"EEEE, MMMM dd, yyyy");
	private static final SimpleDateFormat gDateFormat = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm:ss aa");

	private static final Dimension gFrameSize = new Dimension(700, 500);
	private static final String gUserDirectory = Util.getUserHomeDir();

	private static final String TABLE_ID = "table";
	private HTMLDocument doc;
	private HTMLEditorKit hed;
	private Element table;

	private JTextPane mTranscriptWindow;
	private JScrollPane mScrollPane;
	private UISearchManager mSearchManager;

	public static void main(String[] args) throws XMPPException,
			BadLocationException, IOException, InterruptedException {
		setLookAndFeel();
		// connect();
		// Util.saveTodayHistory(0, 0);
		Util.saveTodayHistory(15, 30);
		// Thread.sleep(500);

		MainClient m = new MainClient();
		m.showArchiveWindow();
	}

	private void showArchiveWindow() throws BadLocationException, IOException {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(gFrameSize);
		f.setLocation(400, 210);

		initLayout(f);
		f.setVisible(true);

		// initial history
		mSearchManager.showHistoryAtStart();
	}

	private void initLayout(JFrame f) throws BadLocationException, IOException {

		hed = new HTMLEditorKit();

		doc = (HTMLDocument) hed.createDefaultDocument();

		mTranscriptWindow = new JTextPane();
		mTranscriptWindow.setContentType("text/html");

		mTranscriptWindow.setEditorKit(hed);
		mTranscriptWindow.setDocument(doc);
		mTranscriptWindow.setText(HtmlFormatter.formatInitialContent(TABLE_ID));
		table = doc.getElement(TABLE_ID);

		mTranscriptWindow.addKeyListener(paneKeyListener());
		mScrollPane = new JScrollPane(mTranscriptWindow);

		mSearchManager = new UISearchManager(gUserDirectory, Util.gWith) {

			@Override
			public JScrollPane getTranscriptScrollPane() {
				return mScrollPane;
			}

			@Override
			public JTextPane getTranscriptWindow() {
				return mTranscriptWindow;
			}

			@Override
			public HTMLDocument getHtmlDocument() {
				return doc;
			}

			@Override
			public void insertHistoryTranscript(List<Message> historyTranscript)
					throws Exception {
				Calendar prevDay = null;
				Calendar curDay = Calendar.getInstance();
				boolean insertHeader = false;

				StringBuilder html = new StringBuilder();
				for (Message message : historyTranscript) {

					Date date = (Date) message.getProperty("date");

					// set first and add header
					if (prevDay == null) {
						prevDay = Calendar.getInstance();
						prevDay.setTime(date);
						html.append(getHistorySeparatorHTML(date));
					}

					// if message's date has changed then add a header
					curDay.setTime(date);
					if (prevDay.get(Calendar.DATE) != curDay.get(Calendar.DATE)) {
						insertHeader = true;
						prevDay.setTime(curDay.getTime());
					} else
						insertHeader = false;

					if (insertHeader)
						html.append(getHistorySeparatorHTML(date));

					Boolean propertyEvent = (Boolean) message
							.getProperty("event");
					String add = propertyEvent == true ? "-- eventt" : "";
					html.append(gMessageFormat.format(new String[] {
							gDateFormat.format(date), message.getFrom(),
							message.getBody() + add }));

					prevDay.setTime(curDay.getTime());
				}

				doc.insertAfterStart(table, html.toString());
			}

		};

		f.setLayout(new GridBagLayout());
		f.add(mSearchManager.getSearchPanelUI(), new GridBagConstraints(0, 0,
				1, 1, 1, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		f.add(mScrollPane, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// /
		mSearchManager.enableDebugOutput(true);
	}

	private KeyAdapter paneKeyListener() {
		return new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (isHistoryKeyPressed(e)) {
					mSearchManager.toggleHistoryPanelVisibility();
				}
			}

		};
	}

	public void insertTopHorizontalLine() {
		try {
			doc.insertAfterStart(table,
					HtmlFormatter.formatBottomHistorySeparator());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertBottomHorizontalLine() {
		appendHtml(HtmlFormatter.formatBottomHistorySeparator());
	}

	private void appendHtml(String htmlText) {
		try {
			doc.insertBeforeEnd(table, htmlText);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean isHistoryKeyPressed(KeyEvent e) {
		return e.getModifiers() == 2 && e.getKeyCode() == 71;
	}

	private String getHistorySeparatorHTML(Date date) {
		return HtmlFormatter
				.formatTopHistoryHeaderSeparator(gHistoryHeaderFormat
						.format(date));
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
