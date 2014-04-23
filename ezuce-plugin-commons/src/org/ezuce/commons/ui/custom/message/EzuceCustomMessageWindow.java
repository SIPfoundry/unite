package org.ezuce.commons.ui.custom.message;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.io.CustomMessageIO;
import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.media.ui.WidgetBuilder;
import org.jivesoftware.MainWindow;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.status.CustomStatusItem;

public class EzuceCustomMessageWindow extends JFrame {

	private static final String gEmpty = "";
	private static final Dimension gTreeSize = new Dimension(200, 220);

	private static final long serialVersionUID = 8915001165053804050L;

	private final String gTitle;
	private final String gSaveText;
	private final String gCancelText;
	private final String gDeleteText;
	private final String gMessageText;
	private final String gPresenceText;

	private static Dimension gSize = new Dimension(300, 450);
	private static File gCustomMessagesFile = new File(
			SparkManager.getUserDirectory(), "custom_messages.xml");

	private JTree mPresenceTree;
	private CustomMessageIO mCustomMessageXml;
	private DefaultTreeModel mPresenceTreeModel;
	private JScrollPane mPresenceTreeView;

	private JButton mBtnDelete;
	private JButton mBtnCancel;
	private JButton mBtnSave;

	private DefaultComboBoxModel mPresenceComboboxModel;
	private JTextField mCustomMessageField;

	private List<CustomStatusItem> mCustomStatuses = new ArrayList<CustomStatusItem>();;

	public EzuceCustomMessageWindow() {

		gTitle = Res.getString("status.custom.messages");
		gSaveText = Res.getString("locations.window.btn.save");
		gCancelText = Res.getString("locations.window.btn.cancel");
		gDeleteText = Res.getString("locations.window.btn.delete");
		gMessageText = Res.getString("custom.message.window.message");
		gPresenceText = Res.getString("custom.message.window.presence");

		setIconImage(MainWindow.getInstance().getIconImage());
		initComponents();
		initLayout();
	}

	private void initComponents() {
		setTitle(gTitle);
		setResizable(false);
		setSize(gSize);
		setLocationRelativeTo(SparkManager.getMainWindow());

		// io
		mCustomMessageXml = new CustomMessageIO();
		mPresenceComboboxModel = new DefaultComboBoxModel();

		// tree
		DefaultMutableTreeNode presenceRootNode = new DefaultMutableTreeNode(
				gTitle);
		buildTree(presenceRootNode);
		mPresenceTree = new JTree(presenceRootNode);
		mPresenceTreeModel = (DefaultTreeModel) mPresenceTree.getModel();

		mPresenceTree.setFont(WidgetBuilder.gFont);
		mPresenceTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		mPresenceTree.setCellRenderer(new CustomMessageTreeCellRenderer());
		mPresenceTree.addTreeSelectionListener(onTreeSelected());
		expandAll();

		// tree view
		mPresenceTreeView = new JScrollPane(mPresenceTree);
		mPresenceTreeView.setMinimumSize(gTreeSize);

		// buttons
		mBtnDelete = new JButton(gDeleteText);
		mBtnDelete.addActionListener(deleteAction());
		mBtnCancel = new JButton(gCancelText);
		mBtnCancel.addActionListener(cancelAction());
		mBtnSave = new JButton(gSaveText);
		mBtnSave.addActionListener(saveAction());

		selectVeryFirstLeaf();

	}

	private void initLayout() {
		setLayout(new GridBagLayout());

		add(mPresenceTreeView, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));
		add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, 1,
				1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		add(getCreateMessagePanel(), new GridBagConstraints(0, 2, 1, 1, 1, 1,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));
	}

	@SuppressWarnings("unchecked")
	public List<CustomStatusItem> load() {
		List<CustomStatusItem> list = null;

		if (gCustomMessagesFile.exists()) {
			try {
				list = (List<CustomStatusItem>) mCustomMessageXml
						.parse(FileUtils.readFileToString(gCustomMessagesFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (list == null) {
			list = new ArrayList<CustomStatusItem>();
		}

		mCustomStatuses.clear();
		mCustomStatuses.addAll(list);
		return list;
	}

	public void save(List<CustomStatusItem> list) {
		try {
			String xml = mCustomMessageXml.toXml(list);
			FileUtils.writeStringToFile(gCustomMessagesFile, xml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildTree(DefaultMutableTreeNode root) {

		if (root == null) {
			System.out.println("ERROR: Root element cannot be null");
			return;
		}

		root.removeAllChildren();

		// available
		CustomMessageTreeNode availableNode = new CustomMessageTreeNode(
				EzucePresenceManager.getAvailablePresence());
		availableNode.add(new CustomMessageTreeNode(EzucePresenceManager
				.getFreeToChatPresence()));

		// away
		CustomMessageTreeNode awayNode = new CustomMessageTreeNode(
				EzucePresenceManager.getAwayPresence());
		awayNode.add(new CustomMessageTreeNode(EzucePresenceManager
				.getBrbPresence()));
		awayNode.add(new CustomMessageTreeNode(EzucePresenceManager
				.getBusyPresence()));

		// dnd
		CustomMessageTreeNode dndNode = new CustomMessageTreeNode(
				EzucePresenceManager.getDndPresence());
		dndNode.add(new CustomMessageTreeNode(EzucePresenceManager
				.getOnPhonePresence()));

		// extended away
		CustomMessageTreeNode extendedAwayNode = new CustomMessageTreeNode(
				EzucePresenceManager.getExtendedAwayPresence());
		extendedAwayNode.add(new CustomMessageTreeNode(EzucePresenceManager
				.getInMeetingPresence()));
		extendedAwayNode.add(new CustomMessageTreeNode(EzucePresenceManager
				.getOffWorkPresence()));

		root.add(availableNode);
		root.add(awayNode);
		root.add(dndNode);
		root.add(extendedAwayNode);

		load();
		addCustomMessages(availableNode, awayNode, dndNode, extendedAwayNode);
		buildPresenceCombobox();
	}

	private Component getCreateMessagePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JComboBox presenceComboBox = new JComboBox(mPresenceComboboxModel);
		presenceComboBox.setRenderer(new PrecenceCellRenderer());
		JLabel presenceLabel = new JLabel(gPresenceText);
		presenceLabel.setLabelFor(presenceComboBox);

		mCustomMessageField = new JTextField();
		JLabel customMessageLabel = new JLabel(gMessageText);
		customMessageLabel.setLabelFor(mCustomMessageField);

		panel.add(presenceLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						5, 5, 5), 0, 0));
		panel.add(presenceComboBox, new GridBagConstraints(1, 0, 2, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 5, 5), 0, 0));

		panel.add(customMessageLabel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		panel.add(mCustomMessageField, new GridBagConstraints(1, 1, 2, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		panel.add(mBtnDelete, new GridBagConstraints(0, 2, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 0, 5), 0, 0));

		panel.add(mBtnCancel, new GridBagConstraints(1, 2, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 0, 5), 0, 0));

		panel.add(mBtnSave, new GridBagConstraints(2, 2, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 0, 5), 0, 0));
		return panel;
	}

	private ActionListener saveAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Presence p = (Presence) mPresenceComboboxModel
						.getSelectedItem();
				String newMessage = mCustomMessageField.getText().trim();
				if (null == p || StringUtils.isEmpty(newMessage))
					return;

				Object lastSelected = mPresenceTree
						.getLastSelectedPathComponent();

				CustomMessageTreeNode node = lastSelected instanceof CustomMessageTreeNode ? (CustomMessageTreeNode) lastSelected
						: new CustomMessageTreeNode();

				Presence currentPresence = (Presence) mPresenceComboboxModel
						.getSelectedItem();

				CustomStatusItem item = null;
				if (node.isCustom()) {
					item = findCustomMessage(node.getPriority(),
							node.getStatus(), currentPresence.getStatus());
					if (item != null)
						item.setStatus(newMessage);

				}

				if (item == null) {
					item = createCustomMessage(newMessage, currentPresence);
					mCustomStatuses.add(item);
				}

				save(mCustomStatuses);
				buildTree((DefaultMutableTreeNode) mPresenceTreeModel.getRoot());
				reloadTreeModel();
				selectTreeItem(item.getStatus(), item.getType());
			}

		};
	}

	protected CustomStatusItem createCustomMessage(String newMessage,
			Presence currentPresence) {
		CustomStatusItem item = new CustomStatusItem();
		item.setPriority(currentPresence.getPriority());
		item.setStatus(newMessage);
		item.setType(currentPresence.getStatus());
		return item;
	}

	private ActionListener cancelAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		};
	}

	private ActionListener deleteAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object lastSelected = mPresenceTree
						.getLastSelectedPathComponent();

				if (lastSelected instanceof CustomMessageTreeNode) {
					CustomMessageTreeNode node = (CustomMessageTreeNode) lastSelected;
					removeCustomMessage(node.getPriority(), node.getStatus(),
							node.getType());
				}
			}

		};
	}

	private void expandAll() {
		if (mPresenceTree == null)
			return;

		for (int i = 0; i < mPresenceTree.getRowCount(); i++) {
			mPresenceTree.expandRow(i);
		}
	}

	private TreeSelectionListener onTreeSelected() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath tp = e.getNewLeadSelectionPath();
				if (tp == null)
					return;

				Object node = tp.getLastPathComponent();
				if (!(node instanceof CustomMessageTreeNode))
					return;

				CustomMessageTreeNode entry = (CustomMessageTreeNode) node;
				Presence presence = !entry.isLeaf() ? entry.getPresence()
						: ((CustomMessageTreeNode) entry.getParent())
								.getPresence();

				setCurrentPresence(presence);
				mBtnDelete.setVisible(entry.isCustom());
				if (mCustomMessageField != null)
					mCustomMessageField.setText(entry.isCustom() ? entry
							.getStatus() : gEmpty);

			}

		};
	}

	private void setCurrentPresence(Presence presence) {
		if (null == presence)
			return;

		for (int i = 0; i < mPresenceComboboxModel.getSize(); i++) {
			Presence p = (Presence) mPresenceComboboxModel.getElementAt(i);
			if (null == p)
				continue;

			if (p.getStatus().equals(presence.getStatus())) {
				mPresenceComboboxModel.setSelectedItem(p);
				break;
			}

		}
	}

	protected void buildPresenceCombobox() {
		mPresenceComboboxModel.removeAllElements();
		mPresenceComboboxModel.addElement(EzucePresenceManager
				.getAvailablePresence());
		mPresenceComboboxModel.addElement(EzucePresenceManager
				.getAwayPresence());
		mPresenceComboboxModel
				.addElement(EzucePresenceManager.getDndPresence());
		mPresenceComboboxModel.addElement(EzucePresenceManager
				.getExtendedAwayPresence());
	}

	protected void addCustomMessages(CustomMessageTreeNode availableNode,
			CustomMessageTreeNode awayNode, CustomMessageTreeNode dndNode,
			CustomMessageTreeNode extendedAwayNode) {
		CustomMessageTreeNode node = null;
		for (CustomStatusItem customStatusItem : mCustomStatuses) {
			if (EzucePresenceManager.getAvailablePresence().getStatus()
					.equals(customStatusItem.getType())) {
				node = availableNode;

			} else if (EzucePresenceManager.getAwayPresence().getStatus()
					.equals(customStatusItem.getType())) {
				node = awayNode;

			} else if (EzucePresenceManager.getDndPresence().getStatus()
					.equals(customStatusItem.getType())) {
				node = dndNode;

			} else if (EzucePresenceManager.getExtendedAwayPresence()
					.getStatus().equals(customStatusItem.getType())) {
				node = extendedAwayNode;
			}

			if (node != null)
				node.add(new CustomMessageTreeNode(customStatusItem
						.getPriority(), customStatusItem.getStatus(),
						customStatusItem.getType()));
		}
	}

	private void selectVeryFirstLeaf() {
		if (mPresenceTree == null || mPresenceTree.getRowCount() < 1)
			return;

		DefaultMutableTreeNode firstLeaf = ((DefaultMutableTreeNode) mPresenceTree
				.getModel().getRoot()).getFirstLeaf();
		TreePath path = new TreePath(firstLeaf.getPath());
		mPresenceTree.setSelectionPath(path);
	}

	private void removeCustomMessage(int priority, String status, String type) {
		CustomStatusItem foundItem = findCustomMessage(priority, status, type);
		mCustomStatuses.remove(foundItem);
		save(mCustomStatuses);
		buildTree((DefaultMutableTreeNode) mPresenceTreeModel.getRoot());
		reloadTreeModel();
	}

	protected CustomStatusItem findCustomMessage(int priority, String status,
			String type) {
		CustomStatusItem foundItem = null;
		for (int i = 0; i < mCustomStatuses.size(); i++) {
			CustomStatusItem item = mCustomStatuses.get(i);

			if (item.getPriority() == priority
					&& item.getStatus().equals(status)
					&& item.getType().equals(type)) {
				foundItem = item;
				break;
			}
		}
		return foundItem;
	}

	@SuppressWarnings("rawtypes")
	private void selectTreeItem(String status, String type) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) mPresenceTreeModel
				.getRoot();

		Enumeration e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			Object nextElement = e.nextElement();

			if (nextElement instanceof CustomMessageTreeNode) {
				CustomMessageTreeNode node = (CustomMessageTreeNode) nextElement;

				if (status.equals(node.getStatus())
						&& type.equals(node.getType())) {
					selectTreePath(node);
					break;
				}

			}
		}
	}

	protected void selectTreePath(CustomMessageTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		mPresenceTree.setSelectionPath(path);
		mPresenceTree.scrollPathToVisible(path);
	}

	protected void reloadTreeModel() {
		mPresenceTreeModel.reload();
		expandAll();
	}

}
