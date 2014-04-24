package org.ezuce.call;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class VoicemailPanel extends JPanel {
    JList list;
    DefaultListModel model;
    public VoicemailPanel() {
        setLayout(new BorderLayout());
        model = new DefaultListModel();
        list = new JList(model);
        JScrollPane pane = new JScrollPane(list);
        add(pane, BorderLayout.NORTH);
    }
}
