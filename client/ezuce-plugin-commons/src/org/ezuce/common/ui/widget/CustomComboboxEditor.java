package org.ezuce.common.ui.widget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CustomComboboxEditor extends JPanel implements ComboBoxEditor {

	private static final long serialVersionUID = 1262901784621351288L;
	private static final Dimension gIconSize = new Dimension(20, 20);
	private ImageIcon mSeparatorIcon;
	private JLabel mIcon;
	private JLabel mSeparator;
	private JLabel mText;
	private Image mBackgroundNormal;
	private Font mFont;
	private Object mValue;

	public CustomComboboxEditor(ImageIcon backgroundNormal,
			ImageIcon separatorImage, Font font) {
		this.mBackgroundNormal = backgroundNormal.getImage();
		this.mSeparatorIcon = separatorImage;
		this.mFont = font;
		initComponents();
		setBorder(null);
	}

	private void initLayout(Icon icon, String text) {

		if (icon != null)
			add(mIcon, new GridBagConstraints(0, 0, 1, 1, 0, 1,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(2, 4, 2, 0), 0, 0));

		if (mSeparatorIcon != null)
			add(mSeparator, new GridBagConstraints(1, 0, 1, 1, 0, 1,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(0, 4, 0, 0), 0, 0));

		add(mText, new GridBagConstraints(2, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 4, 2, 0), 0, 0));

		setPreferredSize(new Dimension(1, 28));
		setMinimumSize(new Dimension(1, 28));
	}

	private void initComponents() {
		setLayout(new GridBagLayout());

		mIcon = new JLabel();
		mIcon.setPreferredSize(gIconSize);
		mIcon.setMinimumSize(gIconSize);
		mIcon.setBorder(null);
		mIcon.setHorizontalAlignment(JLabel.CENTER);

		mSeparator = new JLabel();
		mSeparator.setBorder(null);
		mSeparator.setIcon(mSeparatorIcon);
		mSeparator.setMinimumSize(new Dimension(1, 26));
		mSeparator.setPreferredSize(new Dimension(1, 26));
		mSeparator.setHorizontalAlignment(JLabel.CENTER);

		mText = new JLabel();
		mText.setFont(mFont);
		mText.setBorder(null);
		mText.setHorizontalAlignment(JLabel.LEFT);
	}

	@Override
	public Component getEditorComponent() {
		return this;
	}

	@Override
	public void setItem(Object value) {
		if (value == null)
			return;

		mValue = value;
		Icon icon = null;
		String text = value.toString();

		if (value instanceof CustomComboboxItem) {
			CustomComboboxItem itemValue = (CustomComboboxItem) mValue;
			icon = itemValue.getIcon();
			text = itemValue.getText();
		}

		initLayout(icon, text);

		updateItem(icon, text);
	}

	private void updateItem(Icon icon, String text) {
		if (icon != null)
			mIcon.setIcon(icon);
		mText.setText(text);
	}

	@Override
	public Object getItem() {
		return mValue;
	}

	@Override
	public void selectAll() {
	}

	@Override
	public void addActionListener(ActionListener l) {
	}

	@Override
	public void removeActionListener(ActionListener l) {
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (mBackgroundNormal != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(mBackgroundNormal, 0, 0, getWidth(), getHeight(), 0,
					0, mBackgroundNormal.getWidth(null),
					mBackgroundNormal.getHeight(null), null);
		} else {
			super.paintComponent(g);
		}
	}
}
