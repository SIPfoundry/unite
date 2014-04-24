package com.ezuce.widgets.builder;

import java.awt.Graphics;

import javax.swing.JButton;

public class ButtonBuilder extends AbstractWidgetBuilder {

	private JButton mButton;

	public ButtonBuilder() {
	}

	@Override
	protected JButton buildComponent() {

		mButton = new JButton();
		if (mCurrentBackground != null) {
			mButton = createCustomButton();
			mButton.setContentAreaFilled(false);
		}
		mButton.setText(mText);
		mButton.setOpaque(false);
		mButton.setFocusPainted(false);
		mButton.addMouseListener(this);
		return mButton;
	}

	private JButton createCustomButton() {
		JButton button = new JButton() {

			private static final long serialVersionUID = -2119354682958465243L;

			@Override
			protected void paintComponent(Graphics g) {
				paintBackground(g, mCurrentBackground, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		return button;
	}

	@Override
	protected void repaintComponent() {
		mButton.repaint();
	}
}
