package org.ezuce.common.ui.widget.builder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.border.Border;

public abstract class AbstractWidgetBuilder implements MouseListener,
		FocusListener {

	protected static final String EMPTY = "";
	protected ImageIcon mBackgroundNormal;
	protected ImageIcon mBackgroundHover;
	protected ImageIcon mBackgroundPressed;
	protected Image mCurrentBackground;
	protected int mHeight;
	protected Font mFont;
	protected Border mBorder;
	protected Color mForegroundColor;
	protected String mText;

	protected abstract JComponent buildComponent();

	protected abstract void repaintComponent();

	public JComponent build() {
		if (mBackgroundNormal != null)
			mCurrentBackground = mBackgroundNormal.getImage();
		JComponent comp = buildComponent();
		comp.setBorder(mBorder);
		comp.setFont(mFont);
		comp.setForeground(mForegroundColor);
		comp.setDoubleBuffered(true);
		return comp;
	}

	protected void paintBackground(Graphics g, Image bg, int wWidth, int wHeight) {
		if (bg == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(bg, 0, 0, wWidth, wHeight, 0, 0, bg.getWidth(null),
				bg.getHeight(null), null);
	}

	public void setBackgroundNormal(ImageIcon backgroundNormal) {
		this.mBackgroundNormal = backgroundNormal;
	}

	public void setBackgroundHover(ImageIcon backgroundHover) {
		this.mBackgroundHover = backgroundHover;
	}

	public void setBackgroundPressed(ImageIcon backgroundPressed) {
		this.mBackgroundPressed = backgroundPressed;
	}

	public void setHeight(int height) {
		this.mHeight = height;
	}

	public void setFont(Font font) {
		this.mFont = font;
	}

	public void setBorder(Border border) {
		this.mBorder = border;
	}

	public void setForegroundColor(Color color) {
		this.mForegroundColor = color;
	}

	public void setText(String text) {
		this.mText = text;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (mBackgroundHover != null) {
			mCurrentBackground = mBackgroundHover.getImage();
			repaintComponent();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (mBackgroundNormal != null) {
			mCurrentBackground = mBackgroundNormal.getImage();
			repaintComponent();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (mBackgroundPressed != null)
			mCurrentBackground = mBackgroundPressed.getImage();

		if (mCurrentBackground == null && mBackgroundHover != null)
			mCurrentBackground = mBackgroundHover.getImage();

		if (mCurrentBackground == null && mBackgroundNormal != null)
			mCurrentBackground = mBackgroundNormal.getImage();

		if (mCurrentBackground != null)
			repaintComponent();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseClicked(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

	@Override
	public void focusGained(FocusEvent e) {
	}
}
