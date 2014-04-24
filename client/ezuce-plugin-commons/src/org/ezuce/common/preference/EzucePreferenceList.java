package org.ezuce.common.preference;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;

import org.jivesoftware.spark.preference.Preference;

public abstract class EzucePreferenceList extends JPanel implements
		ListSelectionListener {

	private static final Dimension MINIMUM_SIZE = new Dimension(200, 540);
	private static final long serialVersionUID = -9071728156412781287L;
	private static final Border BORDER_MAIN_LIST = BorderFactory
			.createEmptyBorder(10, 10, 10, 10);
	private static final Color BG_LEFT_PANEL = Color.decode("#58585A");

	private JList list;

	public EzucePreferenceList(DefaultListModel data) {
		setLayout(new BorderLayout());

		// list
		list = new JList();
		list.setLayoutOrientation(JList.VERTICAL);

		list.setCellRenderer(new EzucePreferenceListCellRenderer());

		list.setBorder(BORDER_MAIN_LIST);
		list.getSelectionModel().addListSelectionListener(this);

		// set data
		list.setModel(data);

		// add list
		JScrollPane listPane = new JScrollPane(list);
		list.setBackground(BG_LEFT_PANEL);

		setMinimumSize(MINIMUM_SIZE);
		setPreferredSize(MINIMUM_SIZE);
		setMaximumSize(MINIMUM_SIZE);
		add(listPane, BorderLayout.CENTER);
	}

	public Preference getSelectedValue() {
		return (Preference) list.getSelectedValue();
	}

	public void setSelectedValue(Preference selectedPref) {
		if (selectedPref == null || selectedPref.getTitle() == null
				|| "".equals(selectedPref.getTitle())) {
			list.setSelectedIndex(0);
			return;
		}

		DefaultListModel listModel = (DefaultListModel) list.getModel();
		int size = listModel.getSize();
		for (int i = 0; i < size; i++) {
			Preference pref = (Preference) listModel.get(i);
			if (pref.getTitle().equals(selectedPref.getTitle())) {
				list.setSelectedIndex(i);
				return;
			}
		}
	}
}
