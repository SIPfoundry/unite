package org.ezuce.common.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jivesoftware.spark.component.tabbedPane.SparkTab;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;

public class EzuceTabPanel extends SparkTabbedPane {
	private static final long serialVersionUID = -121342557353559235L;

	private static final int HEADER_WIDTH = 85;
	private static final int HEADER_HEIGHT = 56;

	/**
	 *
	 * @param type
	 *            Used just to satisfy reflection instantiation
	 */
	@SuppressWarnings("unused")
	public EzuceTabPanel(final Integer type) {
		setBackground(new Color(250, 250, 251));
	}

	@Override
	protected JTabbedPane buildTabbedPane(final int type) {
		final JTabbedPane tPane = new JTabbedPane(type);

		tPane.setUI(new EzuceTabbedPaneUI());

		return tPane;
	}

	@Override
	public SparkTab addTab(String title, Icon icon, Component component, String tip) {                
		final SparkTab tab = super.addTab(null, icon, component, tip);
		final int tabPosition = getTabPosition(tab);
		if (tabPosition == 0) {
			decorateSparkTab(tab);
		}

		final URL urlOn = getClass().getClassLoader().getResource("resources/images/tab_" + title + "_on.png");
		final URL urlOff = getClass().getClassLoader().getResource("resources/images/tab_" + title + "_off.png");

		if (urlOn != null && urlOff != null) {
			final ImageIcon imgOn = new ImageIcon(urlOn);
			final ImageIcon imgOff = new ImageIcon(urlOff);

			replaceComponent(tab, imgOn, imgOff, tabPosition == 0);
		} else {
			replaceComponent(tab, icon, icon, tabPosition == 0);
		}

		return tab;
	}

	private void replaceComponent(SparkTab tab, Icon on, Icon off, boolean defaultSelected) {
		for (int i = 0; i < getTabbedPane().getTabCount(); i++) {
			Component comp = getTabbedPane().getComponentAt(i);

			if (tab.equals(comp)) {
				TabHeader header = new TabHeader(on, off);
				getTabbedPane().setTabComponentAt(i, header);
				header.setSelected(defaultSelected);
				break;
			}
		}
	}

	private void decorateSparkTab(SparkTab tab) {
		final ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/images/chat_on.png"));
		tab.setIcon(img);

		final EzuceTabHeader header = new EzuceTabHeader();
		final BorderLayout lm = (BorderLayout) tab.getLayout();

		lm.setHgap(0);
		lm.setVgap(0);
		tab.add(header, BorderLayout.PAGE_START);
	}

	@Override
	protected void fireTabSelected(SparkTab tab, Component component, int index) {
		for (int i = 0; i < getTabbedPane().getTabCount(); i++) {
			Component comp = getTabbedPane().getTabComponentAt(i);
			if (comp instanceof TabHeader) {
				((TabHeader) comp).setSelected(i == index);
			}
		}

		super.fireTabSelected(tab, component, index);
	}

	private static class TabHeader extends JPanel {
		private static final long serialVersionUID = -7593820364717294371L;

		private final JLabel selected;
		private final JLabel deselected;

		public TabHeader(Icon selected, Icon deselected) {
			this.selected = new JLabel(selected);
			this.deselected = new JLabel(deselected);
			setSize(new Dimension(HEADER_WIDTH, HEADER_HEIGHT));
			setPreferredSize(new Dimension(HEADER_WIDTH, HEADER_HEIGHT));
			setMinimumSize(new Dimension(HEADER_WIDTH, HEADER_HEIGHT));
			setOpaque(false);
			setLayout(new BorderLayout());
			add(this.selected, BorderLayout.CENTER);
		}

		public void setSelected(boolean selected) {
			removeAll();
			if (selected) {
				add(this.selected, BorderLayout.CENTER);
			} else {
				add(this.deselected, BorderLayout.CENTER);
			}
		}
	}
}
