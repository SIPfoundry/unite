package org.ezuce.common.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.spark.component.panes.BaseCollapsibleTitlePane;

public class EzuceCollapsibleTitlePane extends BaseCollapsibleTitlePane {
	private static final long serialVersionUID = 784026697460880461L;

	protected boolean collapsed;
	private final JLabel titleLabel;
	private final JLabel lineLabel;
	private final JLabel countLabel;

	public EzuceCollapsibleTitlePane(String title) {
		setBackground(new Color(248, 248, 249));
		setLayout(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 12, 0, 0);
		add(new ArrowLabel(), gbc);
		titleLabel = new JLabel();
		gbc.gridx = 1;
		gbc.insets = new Insets(0, 3, 0, 3);
		add(titleLabel, gbc);
		lineLabel = new LineLabel();
		lineLabel.setText("X");
		gbc.gridx = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		add(lineLabel, gbc);
		countLabel = new JLabel("()");
		countLabel.setForeground(new Color(113, 113, 113));
		gbc.gridx = 3;
		gbc.insets = new Insets(0, 3, 0, 3);
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		add(countLabel, gbc);
		setTitle(title);
	}

	@Override
	public void setIcon(Icon icon) {
		// do nothing
	}

	@Override
	public void setTitle(String title) {
		String trimmedTitle = title;
		String onlineTotalNo = StringUtils.EMPTY;
		if (trimmedTitle == null || trimmedTitle.isEmpty()) {
			// trick it into using vertical space
			trimmedTitle = "X";
			titleLabel.setForeground(getBackground());
		} else {
			titleLabel.setForeground(new Color(113, 113, 113));
			int start = trimmedTitle.lastIndexOf('(');
			int end = trimmedTitle.lastIndexOf(')');
			if (start >= 0) {
				if (end >=0 && start < end) {
					onlineTotalNo = trimmedTitle.substring(start, end + 1);
				}
				trimmedTitle = trimmedTitle.substring(0,
						start);
			}
		}
		titleLabel.setText(trimmedTitle);
		countLabel.setText(onlineTotalNo);
	}

	@Override
	public boolean isCollapsed() {
		return collapsed;
	}

	@Override
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	@Override
	public void setSubPane(boolean subPane) {
		// do nothing
	}

	private class ArrowLabel extends JLabel {
		private static final long serialVersionUID = -2811039056533717474L;

		public ArrowLabel() {
			setPreferredSize(new Dimension(9, 9));
			setSize(new Dimension(9, 9));
		}

		@Override
		protected void paintComponent(Graphics g) {
			final Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2D.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			setForeground(Color.BLACK);
			final Polygon arrow = new Polygon();
			final int mid = getHeight() / 2;
			if (collapsed) {
				arrow.addPoint(mid - 2, mid - 3);
				arrow.addPoint(mid - 2, mid + 3);
				arrow.addPoint(mid + 2, mid);
			} else {
				arrow.addPoint(mid - 3, mid - 2);
				arrow.addPoint(mid + 3, mid - 2);
				arrow.addPoint(mid, mid + 2);
			}
			g.fillPolygon(arrow);
		}

	}

	private class LineLabel extends JLabel {
		private static final long serialVersionUID = -6215697341283982659L;

		public LineLabel() {
			setHorizontalAlignment(CENTER);
		}

		@Override
		protected void paintComponent(Graphics g) {
			final int y = getHeight() / 2;
			g.setColor(new Color(148, 167, 176));
			g.drawLine(0, y, getWidth(), y);
		}

	}
}
