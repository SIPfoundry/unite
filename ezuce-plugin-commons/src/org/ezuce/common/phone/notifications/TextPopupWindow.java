/**
 * 
 */
package org.ezuce.common.phone.notifications;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.ezuce.media.ui.GraphicUtils;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class TextPopupWindow extends PopupWindow {

	private static final long serialVersionUID = -6548265589491282205L;

	private static final Color TEXT_POPUP_BACKGROUND = new Color(252, 250, 189);
	private static final Insets INSETS_TEXT = new Insets(0, 20, 10, 10);
	private static final Insets INSETS_FROM = new Insets(10, 20, 5, 10);
	private static final String TEXT_POPUP_HEADER_TXT = "New Chat Message";
	private static final Icon ICON_MSG = GraphicUtils
			.createImageIcon("/resources/images/icon_msg.png");
	private static final String EMPTY = "";
	private static final String MORE_TEXT = "...";
	private static final int MAX_TEXT_LENGTH = 150;
	private static final int LINE_WIDTH = 160;
	private static final int NUM_COLUMNS = 16;

	private String text = EMPTY;
	private MouseListener onClickAction;

	public TextPopupWindow() {
		setHeaderIcon(ICON_MSG);
		setHeaderText(TEXT_POPUP_HEADER_TXT);
	}

	public void setText(String text) {
		if (null == text)
			return;
		text = text.replaceAll("(\n|\r|\t)", EMPTY);
		this.text = text.length() > MAX_TEXT_LENGTH ? text.substring(0,
				MAX_TEXT_LENGTH) + MORE_TEXT : text;
	}

	@Override
	protected Color getContentBackgroundColor() {
		return TEXT_POPUP_BACKGROUND;
	}

	@Override
	protected Component getContentPanelContent() {
		JPanel panel = new JPanel();
		panel.addMouseListener(onClickAction);

		panel.setBackground(getContentBackgroundColor());
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.insets = INSETS_FROM;
		JLabel fromLabel = new JLabel(getFrom());
		fromLabel.setFont(FONT_FROM);
		fromLabel.setForeground(COLOR_FROM);
		fromLabel.addMouseListener(onClickAction);
		panel.add(fromLabel, c);

		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = INSETS_TEXT;

		JTextArea textLabel = new JTextArea(text);
		// this is workaround for IceadTea to set up correct size after
		// JTextArea is populated with the text.
		textLabel.setPreferredSize(new Dimension(10, 10));
		textLabel.setFont(FONT_TEXT);
		textLabel.setForeground(COLOR_TEXT);
		textLabel.setBackground(TEXT_POPUP_BACKGROUND);
		textLabel.addMouseListener(onClickAction);
		textLabel.setWrapStyleWord(true);
		textLabel.setColumns(NUM_COLUMNS);
		textLabel.setLineWrap(true);
		textLabel.setRows(countLines(textLabel));

		panel.add(textLabel, c);
		return panel;
	}

	public void addOnClickAction(MouseListener onClickAction) {
		this.onClickAction = onClickAction;
	}

	private static int countLines(JTextArea textArea) {
		AttributedString text = new AttributedString(textArea.getText());
		FontRenderContext frc = textArea.getFontMetrics(textArea.getFont())
				.getFontRenderContext();
		AttributedCharacterIterator it = text.getIterator();
		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(it, frc);
		lineMeasurer.setPosition(it.getBeginIndex());
		int noLines = 0;
		while (lineMeasurer.getPosition() < it.getEndIndex()) {
			lineMeasurer.nextLayout(LINE_WIDTH);
			noLines++;
		}
		return noLines;
	}
}
