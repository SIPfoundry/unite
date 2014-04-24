package com.ezuce.preferences.category;

import static com.ezuce.util.GraphicUtils.createImageIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

import com.ezuce.util.Res;
import com.ezuce.util.WidgetBuilder;

public class TroubleshootingSettings extends Preference {

	private static final long serialVersionUID = 1803617032322406575L;

	private static final int gKB = 1024;
	private static final String mIconName = "/resources/images/prefs/login";
	private static final String gTitle = "Troubleshooting";
	protected static final MessageFormat gCurrentLogSizeFormat = new MessageFormat(
			"{0,number,#.##} Mb");
	private static final Font gTextFont = new Font("Droid Sans", Font.PLAIN, 13);
	private static final File gErrorLogFile = getErrorLogFileDirectory();

	private JPanel mPanel;
	private JTextArea mErrorArea;
	private JLabel mLblSize;
	private JSpinner mSpinerLogSize;

	public TroubleshootingSettings() {
		super(gTitle, createImageIcon(mIconName + "_on.png"),
				createImageIcon(mIconName + "_off.png"),
				createImageIcon(mIconName + ".png"));

		mPanel = createGUI();
		checkErrorLogSize();
	}

	@Override
	public JComponent getGUI() {
		readErrorFileContent();
		return mPanel;

	}

	private JPanel createGUI() {

		mErrorArea = new JTextArea();
		mErrorArea.setEditable(false);
		mErrorArea.setBackground(Color.WHITE);
		mErrorArea.setFont(gTextFont);
		JScrollPane consoleScrollPane = new JScrollPane(mErrorArea);

		JLabel lblLimitSize = WidgetBuilder.createLabel(Res
				.getString("preferences.troubleshooting.limit.size"));
		mSpinerLogSize = WidgetBuilder.createIntegerSpinner(1, 100, 2, 1);

		JLabel lblCurrentSize = WidgetBuilder.createLabel(Res
				.getString("preferences.troubleshooting.current.size"));

		mLblSize = WidgetBuilder.createLabel(gCurrentLogSizeFormat
				.format(new Object[] { 0 }));
		JButton btnClearLog = WidgetBuilder.createJButton(Res
				.getString("preferences.troubleshooting.clear.log"));
		btnClearLog.addActionListener(clearLogActionListener());

		JButton btnRefresh = WidgetBuilder.createJButton(Res
				.getString("preferences.troubleshooting.log.refresh"));
		btnRefresh.addActionListener(refreshLogActionListener());

		Insets insets = new Insets(5, 5, 5, 5);
		JPanel panel = new JPanel(new GridBagLayout());

		// row 1
		panel.add(lblLimitSize, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(mSpinerLogSize, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));

		panel.add(lblCurrentSize, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(mLblSize, new GridBagConstraints(3, 0, 1, 1, 1, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(btnClearLog, new GridBagConstraints(4, 0, 1, 1, 1, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		panel.add(btnRefresh, new GridBagConstraints(5, 0, 1, 1, 1, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));

		// row 2
		panel.add(consoleScrollPane, new GridBagConstraints(0, 1, 6, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0));

		return panel;
	}

	private ActionListener refreshLogActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				readErrorFileContent();
			}
		};
	}

	private ActionListener clearLogActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearErrorFile();
				readErrorFileContent();
			}
		};
	}

	private void checkErrorLogSize() {
		double fileSize = getFileSize();
		Integer maxSize = (Integer) mSpinerLogSize.getValue();
		if (fileSize >= maxSize) {
			clearErrorFile();
			readErrorFileContent();
		}
	}

	private void updateFileSize() {
		double fileSize = getFileSize();
		mLblSize.setText(gCurrentLogSizeFormat
				.format(new Object[] { fileSize }));
	}

	private double getFileSize() {
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(gErrorLogFile);
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

	private void readErrorFileContent() {
		FileReader reader = null;
		try {
			reader = new FileReader(gErrorLogFile);
			mErrorArea.read(reader, gErrorLogFile);
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

		updateFileSize();
	}

	private void clearErrorFile() {
		FileOutputStream fos = null;
		Scanner scanner = null;
		try {
			fos = new FileOutputStream(gErrorLogFile);
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

	private static File getErrorLogFileDirectory() {
		/*
		 * final String bareJID = sessionManager.getBareAddress(); File
		 * userDirectory = new File(Spark.getSparkUserHome(),
		 * "/logs/errors.log"); if (!userDirectory.exists()) {
		 * userDirectory.mkdirs(); } return userDirectory;
		 */
		return new File("/home/slava/.eZuce/Unite/logs/errors.log");
	}

}
