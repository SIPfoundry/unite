package com.ezuce.widgets.builder;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TextFieldBuilder extends AbstractWidgetBuilder implements
		MouseListener, FocusListener {

	private boolean mIsPassword;
	private JTextField field;

	public TextFieldBuilder() {
	}

	public void setPassword(boolean isPassword) {
		this.mIsPassword = isPassword;
	}

	@Override
	protected JTextField buildComponent() {
		field = mIsPassword ? createPasswordField() : createTextField();
		field.setOpaque(false);
		field.addMouseListener(this);

		if (mHeight > 0)
			field.setPreferredSize(new Dimension(1, mHeight));

		if (mFont != null)
			field.setFont(mFont);

		if (mForegroundColor != null)
			field.setForeground(mForegroundColor);

		if (mText != null) {
			field.setText(mText);
			field.addFocusListener(this);
		}

		return field;
	}

	private JPasswordField createPasswordField() {
		JPasswordField field = new JPasswordField() {

			private static final long serialVersionUID = -1381028520035888434L;

			@Override
			protected void paintComponent(Graphics g) {
				if (mCurrentBackground != null) {
					paintBackground(g, mCurrentBackground, getWidth(),
							getHeight());
				}
				super.paintComponent(g);

			}
		};
		return field;
	}

	private JTextField createTextField() {
		JTextField field = new JTextField() {

			private static final long serialVersionUID = -7837455474859538576L;

			@Override
			protected void paintComponent(Graphics g) {
				if (mCurrentBackground != null) {
					paintBackground(g, mCurrentBackground, getWidth(),
							getHeight());
				}
				super.paintComponent(g);
			}
		};
		return field;
	}

	@Override
	public void focusLost(FocusEvent e) {
		JTextField field = (JTextField) e.getSource();
		String fieldText = field.getText();
		if (EMPTY.equals(fieldText))
			field.setText(mText);
	}

	@Override
	public void focusGained(FocusEvent e) {
		JTextField field = (JTextField) e.getSource();
		String fieldText = field.getText();
		if (mText.equals(fieldText))
			field.setText(EMPTY);
	}

	@Override
	protected void repaintComponent() {
		field.repaint();
	}
}
