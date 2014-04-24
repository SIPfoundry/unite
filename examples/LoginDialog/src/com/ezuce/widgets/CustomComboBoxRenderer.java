package com.ezuce.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.ezuce.util.GraphicUtils;

public class CustomComboBoxRenderer extends JPanel implements ListCellRenderer {

	private static final long serialVersionUID = -5686213417257289599L;
	private static final Dimension gIconSize = new Dimension(20, 20);
	private ImageIcon mSeparatorIcon;
	private JLabel mIcon;
	private JLabel mSeparator;
	private JLabel mText;
	private Image mBgNormal;
	private Font mFont;
	private Color mColorItemBorder;
	private Color mInactiveBgColor;
	private Color mActiveBgColor;

	public CustomComboBoxRenderer(Color bgItemColor, Color bgItemHoverColor,
			Color itemBorderColor, ImageIcon sepImage, Font font) {
		this.mSeparatorIcon = sepImage;
		this.mFont = font;
		this.mColorItemBorder = itemBorderColor;
		this.mInactiveBgColor = bgItemColor;
		this.mActiveBgColor = bgItemHoverColor;
		initComponents();
		setBackground(bgItemColor);
	}

	public CustomComboBoxRenderer(String bg) {
		if (bg != null)
			mBgNormal = GraphicUtils.createImageIcon(bg).getImage();

		initComponents();
	}

	private void initLayout(Icon icon, String text) {

		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, mColorItemBorder));

		if (icon != null)
			add(mIcon, new GridBagConstraints(0, 0, 1, 1, 0, 1,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(2, 3, 2, 2), 0, 0));

		if (mSeparatorIcon != null && icon != null)
			add(mSeparator, new GridBagConstraints(1, 0, 1, 1, 0, 1,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(2, 2, 2, 2), 0, 0));

		Insets textInsets = new Insets(2, 2, 2, 2);
		if (icon == null)
			textInsets = new Insets(2, 4, 2, 2);

		add(mText, new GridBagConstraints(2, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				textInsets, 0, 0));

		setPreferredSize(new Dimension(1, 28));
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
		mSeparator.setHorizontalAlignment(JLabel.CENTER);

		mText = new JLabel();
		mText.setFont(mFont);
		mText.setBorder(null);
		mText.setHorizontalAlignment(JLabel.LEFT);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		setBackground(isSelected ? mActiveBgColor : mInactiveBgColor);

		Icon icon = null;
		String text = value.toString();
		if (value instanceof CustomComboboxItem) {
			CustomComboboxItem itemValue = (CustomComboboxItem) value;
			icon = itemValue.getIcon();
			text = itemValue.getText();
		}

		initLayout(icon, text);

		updateItem(icon, text);

		return this;
	}

	private void updateItem(Icon icon, String text) {
		if (icon != null)
			mIcon.setIcon(icon);
		mText.setText(text);
	}

	@Override
	protected void paintComponent(Graphics g) {

		if (mBgNormal != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(mBgNormal, 0, 0, getWidth(), getHeight(), 0, 0,
					mBgNormal.getWidth(null), mBgNormal.getHeight(null), null);
		}

		else {
			super.paintComponent(g);
		}
	}
}
