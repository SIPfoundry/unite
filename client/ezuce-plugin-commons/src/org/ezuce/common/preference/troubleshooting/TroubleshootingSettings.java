package org.ezuce.common.preference.troubleshooting;

import static org.ezuce.media.ui.GraphicUtils.createImageIcon;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

import org.ezuce.media.ui.WidgetBuilder;
import org.jivesoftware.Spark;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.preference.Preference;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class TroubleshootingSettings implements Preference {

	private static final MessageFormat gMailToStringFormat = new MessageFormat(
			"mailto:{0}?subject={1}&cc={2}&body={3}");// &attachment=file:///{4}");
	private static final String MB = "Mb";
	public static final String NAMESPACE = "http://www.jivesoftware.org/spark/troubleshooting";
	private static final String iconName = "/resources/images/prefs/troubleshooting";
	private ImageIcon activeImage = createImageIcon(iconName + "_on.png");
	private ImageIcon inactiveImage = createImageIcon(iconName + "_off.png");
	private ImageIcon titleImage = createImageIcon(iconName + ".png");

	private ImageIcon clearImage = createImageIcon(iconName + "/clear.png");
	private ImageIcon refreshImage = createImageIcon(iconName + "/refresh.png");
	private ImageIcon sendImage = createImageIcon(iconName + "/send.png");
	private ImageIcon copyImage = createImageIcon(iconName + "/copy.png");

	private static final int gKB = 1024;
	private static final MessageFormat gCurrentLogSizeFormat = new MessageFormat(
			"{0,number,#.##}");
	private static final Font gTextFont = new Font("Droid Sans", Font.PLAIN, 13);
	private static final File gErrorLogFile = getLogFile("errors.log");
	private static final File gWarnLogFile = getLogFile("warn.log");
	private static final File gInfoLogFile = getLogFile("info.log");
	private static final File gConsoleLogFile = getLogFile("console.log");
	protected static final String gSupportEmail = Res
			.getString("support.email");
	private static final SimpleDateFormat gLogDateFormat = new SimpleDateFormat(
			"ddMMMyyyyHHmm");
	private static final MessageFormat gLogZippedMessageFormat = new MessageFormat(
			Res.getString("troubleshooting.log.zipped.msg"));
	private static final MessageFormat gRemindedToAttachFile = new MessageFormat(
			Res.getString("troubleshooting.attach.file"));

	private String title;

	private JComponent mPanel;

	public TroubleshootingSettings() {
		title = Res.getString("preferences.troubleshooting.title");

		try {
			mPanel = new JPanel(new GridLayout(0, 1));
			addErrorGUI(mPanel);
			addWarnGUI(mPanel);
			addConsoleGUI(mPanel);
			addInfoGUI(mPanel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Icon getIcon() {
		return inactiveImage;
	}

	@Override
	public String getTooltip() {
		return title;
	}

	@Override
	public String getListName() {
		return title;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public JComponent getGUI() {
		return mPanel;
	}

	@Override
	public void load() {
	}

	@Override
	public void commit() {

	}

	@Override
	public boolean isDataValid() {
		return true;
	}

	@Override
	public String getErrorMessage() {
		return "";
	}

	@Override
	public Object getData() {
		return SettingsManager.getLocalPreferences();
	}

	@Override
	public void shutdown() {
	}

	@Override
	public Icon getActiveIcon() {
		return activeImage;
	}

	@Override
	public Icon getTitleIcon() {
		return titleImage;
	}

	private void addErrorGUI(JComponent panel) {
		addLogPanelToGUI(gErrorLogFile, panel);
	}

	private void addConsoleGUI(JComponent panel) {
		addLogPanelToGUI(gConsoleLogFile, panel);
	}

	private void addWarnGUI(JComponent panel) {
		addLogPanelToGUI(gWarnLogFile, panel);
	}

	private void addInfoGUI(JComponent panel) {
		addLogPanelToGUI(gInfoLogFile, panel);
	}

	private void addLogPanelToGUI(File logFile, JComponent mainPanel) {

		JTextArea logTextArea = new JTextArea();
		logTextArea.setEditable(false);
		logTextArea.setBackground(Color.WHITE);
		logTextArea.setFont(gTextFont);
		JScrollPane consoleScrollPane = new JScrollPane(logTextArea);

		JLabel lblLimitSize = WidgetBuilder.createLabel(Res
				.getString("preferences.troubleshooting.limit.size"));
		JSpinner spinerLogSize = WidgetBuilder.createIntegerSpinner(1, 100, 2,
				1);
		Dimension size = new Dimension(40, 24);
		spinerLogSize.setMinimumSize(size);
		spinerLogSize.setPreferredSize(size);
		spinerLogSize.setMaximumSize(size);

		JLabel lblFileSize = WidgetBuilder.createLabel("0");

		String path = logFile.getPath();
		String name = path.substring(path.lastIndexOf(File.separator) + 1);
		JLabel title = new JLabel(name);

		Insets insets = new Insets(1, 0, 0, 0);
		JPanel col2 = new JPanel(new GridBagLayout());
		Dimension colSize = new Dimension(130, 130);
		col2.setMinimumSize(colSize);
		col2.setMaximumSize(colSize);
		col2.setPreferredSize(colSize);

		// row 1
		JPanel panel1 = new JPanel();
		panel1.add(title);
		panel1.add(lblFileSize);
		panel1.add(new JLabel(MB));
		col2.add(panel1, new GridBagConstraints(0, 0, 3, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));

		// row 2
		JPanel panel2 = new JPanel();
		panel2.add(lblLimitSize);
		panel2.add(spinerLogSize);
		panel2.add(new JLabel(MB));

		col2.add(panel2, new GridBagConstraints(0, 1, 3, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));

		// / buttons
		Dimension btnSize = new Dimension(24, 24);

		JButton btnClearLog = new JButton(clearImage);
		btnClearLog.setMinimumSize(btnSize);
		btnClearLog.setPreferredSize(btnSize);
		btnClearLog.setMaximumSize(btnSize);
		btnClearLog.setToolTipText("Clear");
		btnClearLog.addActionListener(clearLogActionListener(logFile,
				lblFileSize, logTextArea));

		JButton btnCopyLog = new JButton(copyImage);
		btnCopyLog.setMinimumSize(btnSize);
		btnCopyLog.setPreferredSize(btnSize);
		btnCopyLog.setMaximumSize(btnSize);
		btnCopyLog.setToolTipText("Copy");
		btnCopyLog
				.addActionListener(copyLogActionListener(logFile, logTextArea));

		JButton btnRefresh = new JButton(refreshImage);
		btnRefresh.setMinimumSize(btnSize);
		btnRefresh.setPreferredSize(btnSize);
		btnRefresh.setMaximumSize(btnSize);
		btnRefresh.setToolTipText("Refresh");
		btnRefresh.addActionListener(refreshLogActionListener(logFile,
				lblFileSize, logTextArea));

		JButton btnSend = new JButton(sendImage);
		btnSend.setMinimumSize(btnSize);
		btnSend.setPreferredSize(btnSize);
		btnSend.setMaximumSize(btnSize);
		btnSend.setToolTipText("Email");
		btnSend.addActionListener(sendLogActionListener(logFile, logTextArea));

		// row 3
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(btnCopyLog);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnSend);
		buttonPanel.add(btnClearLog);

		col2.add(buttonPanel, new GridBagConstraints(0, 3, 3, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0,
				0));

		// main
		JPanel panel = new JPanel(new GridBagLayout());
		Insets mainInsets = new Insets(2, 1, 2, 1);
		// col 1
		panel.add(consoleScrollPane, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, mainInsets,
				0, 0));

		// col 2
		panel.add(col2, new GridBagConstraints(1, 1, 0, 1, 0, 1,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, mainInsets,
				0, 0));

		mainPanel.add(panel);

		checkLogFileSize((Integer) spinerLogSize.getValue(), logFile,
				lblFileSize, logTextArea);
		readFileContent(logFile, lblFileSize, logTextArea);

	}

	private ActionListener sendLogActionListener(final File logFile,
			final JTextArea logTextArea) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser saveFileDialog = new JFileChooser();
				saveFileDialog
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnVal = saveFileDialog.showSaveDialog(mPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = saveFileDialog.getSelectedFile();
					File logZip = createZippedLog(selectedFile);

					try {
						openEmailClient(logFile, logTextArea, logZip);
					} catch (Exception exx) {
						exx.printStackTrace();

						JOptionPane.showMessageDialog(
								new JFrame(),
								gLogZippedMessageFormat.format(new String[] {
										logZip.getPath(), gSupportEmail }));
					}
				}
			}
		};
	}

	private ActionListener refreshLogActionListener(final File file,
			final JLabel size, final JTextArea logTextArea) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				readFileContent(file, size, logTextArea);
			}
		};
	}

	private ActionListener copyLogActionListener(File logFile,
			final JTextArea logTextArea) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				logTextArea.selectAll();
				logTextArea.copy();
			}
		};
	}

	private ActionListener clearLogActionListener(final File logFile,
			final JLabel label, final JTextArea logTextArea) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearLogFile(logFile);
				readFileContent(logFile, label, logTextArea);
			}
		};
	}

	private void checkLogFileSize(int maxSize, File logFile, JLabel lbl,
			JTextArea logTextArea) {
		double fileSize = getLogFileSize(logFile);
		if (fileSize >= maxSize) {
			clearLogFile(logFile);
			readLogFileContent(logFile, logTextArea);
			updateLogFileSize(fileSize, lbl);
		}
	}

	private void updateLogFileSize(double size, JLabel lblSize) {
		lblSize.setText(gCurrentLogSizeFormat.format(new Object[] { size }));
	}

	protected double getLogFileSize(File file) {
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(file);
			double size = (double) fis.available() / gKB / gKB;
			return size;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	private void readFileContent(File file, JLabel lblSize,
			JTextArea logTextArea) {
		readLogFileContent(file, logTextArea);
		updateLogFileSize(getLogFileSize(file), lblSize);
	}

	protected void readLogFileContent(File file, JTextArea logTextArea) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			logTextArea.read(reader, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void clearLogFile(File logFile) {
		FileOutputStream fos = null;
		Scanner scanner = null;
		try {
			fos = new FileOutputStream(logFile);
			fos.write(new byte[0]);
			fos.flush();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static File getLogFile(String log) {
		File logDir = new File(Spark.getLogDirectory(), log);
		if (!logDir.exists()) {
			try {
				logDir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logDir;
	}

	protected void openEmailClient(final File logFile,
			final JTextArea logTextArea, File logZip) throws IOException,
			URISyntaxException {
		if (!Desktop.isDesktopSupported()) {
			System.out.println("ERROR: Destop API is not supported.");
			return;
		}

		if (!Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
			System.out.println("ERROR: Desktop Mail API is not supported.");
			return;
		}

		logTextArea.selectAll();
		logTextArea.copy();
		String body = "";// logTextArea.getText();
		int origBodyLength = body.length();
		int maxBodyLength = 1000;
		String startText = gRemindedToAttachFile.format(new String[] { logZip
				.getPath() });
		body = origBodyLength > maxBodyLength ? startText
				+ body.substring(origBodyLength - maxBodyLength) : startText
				+ body;

		String subject = logFile.getPath().substring(
				logFile.getPath().lastIndexOf(File.separator) + 1);

		final String mailURIStr = gMailToStringFormat.format(new String[] {
				gSupportEmail, subject, "", encodeMailBody(body),
				logZip.getPath() });
		final URI mailURI = new URI(mailURIStr);
		Desktop.getDesktop().mail(mailURI);
	}

	private File createZippedLog(File parentDirectory) {

		File zippedLogFile = new File(parentDirectory, "eZuce_log_"
				+ gLogDateFormat.format(Calendar.getInstance().getTime())
				+ ".zip");
		if (zippedLogFile.exists())
			zippedLogFile.delete();

		String[] files = { gErrorLogFile.getPath(), gWarnLogFile.getPath(),
				gConsoleLogFile.getPath(), gInfoLogFile.getPath() };

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zippedLogFile));

			byte[] buf = new byte[1024];
			for (String filename : files) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(filename);
					zos.putNextEntry(new ZipEntry(getShortFileName(filename)));
					int len;
					while ((len = fis.read(buf)) > 0)
						zos.write(buf, 0, len);
					zos.closeEntry();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fis != null)
						fis.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (zos != null)
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return zippedLogFile;
	}

	private static String getShortFileName(String f) {
		return f.substring(f.lastIndexOf(File.separator) + 1);
	}

	private static String encodeMailBody(final String s) {
		try {
			return java.net.URLEncoder.encode(s, "utf-8")
					.replaceAll("\\+", "%20").replaceAll("\\%0A", "%0D%0A");
		} catch (Throwable x) {
			return s;
		}
	}
}
