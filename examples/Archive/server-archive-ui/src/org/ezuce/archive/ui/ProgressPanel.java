package org.ezuce.archive.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ezuce.archive.ui.util.ArchiveUtil;

 class ProgressPanel extends JPanel {

	private static final long serialVersionUID = 1094284470206823687L;
	private static final String gProgressText = "Loading chat history";
	private static final Font gFont = ArchiveUtil.gFont.deriveFont(Font.BOLD, 10);
	private static final Color gTextColor = Color.gray;
	private static final Color gBgColor = Color.white;

	private JLabel mLoadingIcon;
	private JLabel mLoadingText;

	public ProgressPanel() {
		initComponents();
		initLayout();
	}

	private void initComponents() {
		ArchiveUtil.debugBorder(this);
		mLoadingIcon = new JLabel("...");
		mLoadingIcon.setFont(gFont);
		mLoadingIcon.setForeground(gTextColor);

		mLoadingText = new JLabel(gProgressText);
		mLoadingText.setFont(gFont);
		mLoadingText.setForeground(gTextColor);

		setBackground(gBgColor);
	}

	private void initLayout() {
		setLayout(new GridBagLayout());

		add(mLoadingIcon, new GridBagConstraints(0, 0, 1, 1, 0.2, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		add(mLoadingText, new GridBagConstraints(1, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
	}

}
