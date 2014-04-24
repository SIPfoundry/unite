package org.ezuce.archive.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.ezuce.archive.ui.util.ArchiveUtil;

class SearchField extends JTextField implements FocusListener {

	private static final Dimension gFieldSize = new Dimension(200, 28);
	private final String gDefaultText;
	private static final long serialVersionUID = -1782422689958519563L;
	private Insets mInsets;
	private Image mIconSearch;

	// private Image mIconClear;

	public SearchField() {
		gDefaultText = "Search in Chat History"; // Res

		initComponents();
	}

	private void initComponents() {
		this.mIconSearch = ArchiveUtil.gSearchImage.getImage();
		// this.mIconClear = ArchiveUtil.gCloseSmallImage.getImage();
		Border border = UIManager.getBorder("TextField.border");
		this.mInsets = border.getBorderInsets(new JTextField());

		setText(gDefaultText);
		setFont(ArchiveUtil.gFont);
		setMinimumSize(gFieldSize);
		setPreferredSize(gFieldSize);
		setMaximumSize(gFieldSize);

		addFocusListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int textX = 2;

		if (mIconSearch != null) {
			int iconWidth = mIconSearch.getWidth(null);
			int iconHeight = mIconSearch.getHeight(null);
			int x = mInsets.left + 2;
			textX = x + iconWidth + 2;
			int y = (this.getHeight() - iconHeight) / 2;
			g.drawImage(mIconSearch, x, y, null);
		}

		/*
		 * if (mIconClear != null) { int iconWidth = mIconClear.getWidth(null);
		 * int iconHeight = mIconClear.getHeight(null); int x = getSize().width
		 * - iconWidth - mInsets.right - 5; int y = (this.getHeight() -
		 * iconHeight) / 2; g.drawImage(mIconClear, x, y, null); }
		 */

		setMargin(new Insets(1, textX, 1, 2));
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (gDefaultText.equals(getText()))
			setText(ArchiveUtil.gEmpty);
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (!ArchiveUtil.hasText(getText()))
			setText(gDefaultText);
	}

	public void reset() {
		setText(gDefaultText);
	}

}
