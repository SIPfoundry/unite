package org.ezuce.commons.ui.location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jivesoftware.MainWindow;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;

public class LocationWindow extends JFrame {

	private final String gLocationsTitle;
	private final String gSaveText;
	private final String gCancelText;
	private final String gDeleteText;
	private final String gLocationText;
	private static final String EMPTY = "";
	private final String gDefaultAddNewText;

	private static final long serialVersionUID = 5141084416634387583L;
	private JTable mLocationsTable;
	private JTextField mNewLocationField;
	private JScrollPane mLocationScrollTable;
	private JButton mBtnDelete;
	private JButton mBtnCancel;
	private JButton mBtnSave;
	private LocationTableModel mTableModel;
	private static Font gFont = new Font("Droid", Font.PLAIN, 12);
	private LocationWindowObservable locationWindowObservable = new LocationWindowObservable();

	// ##### constructors #####

	public LocationWindow() {
		gLocationsTitle = Res.getString("locations.window.title");
		gSaveText = Res.getString("locations.window.btn.save");
		gCancelText = Res.getString("locations.window.btn.cancel");
		gDeleteText = Res.getString("locations.window.btn.delete");
		gLocationText = Res.getString("locations.window.lbl.location");
		gDefaultAddNewText = Res.getString("locations.window.lbl.add.new");
		setIconImage(MainWindow.getInstance().getIconImage());
		initComponents();
		initLayout();
	}

	// ##### initialization #####

	private void initComponents() {
		setTitle(gLocationsTitle);
		setSize(new Dimension(300, 400));
		setResizable(false);
		setLocationRelativeTo(SparkManager.getMainWindow());

		mNewLocationField = new JTextField();
		mNewLocationField.setFont(gFont);
		mNewLocationField.addFocusListener(focusLocationFieldAction());

		// buttons
		mBtnDelete = new JButton(gDeleteText);
		mBtnDelete.addActionListener(deleteAction());
		mBtnCancel = new JButton(gCancelText);
		mBtnCancel.addActionListener(cancelAction());
		mBtnSave = new JButton(gSaveText);
		mBtnSave.addActionListener(saveAction());

		// table
		mLocationsTable = new JTable(new LocationTableModel(gDefaultAddNewText));
		mLocationsTable.setFont(gFont);
		mLocationsTable.setShowGrid(false);
		mLocationsTable.setShowHorizontalLines(false);
		mLocationsTable.setShowVerticalLines(false);
		mLocationsTable.setTableHeader(null);
		mLocationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mLocationsTable.addMouseListener(onTableClickAction());
		mLocationsTable.getColumnModel().getColumn(0)
				.setCellRenderer(new LocationTableRenderer(gDefaultAddNewText));
		mLocationsTable.setRowHeight(25);
		mLocationsTable.setGridColor(Color.white);
		mLocationsTable.setBackground(Color.white);
		mLocationsTable.setBorder(BorderFactory.createLineBorder(Color.white));
		mLocationsTable.getSelectionModel().setSelectionInterval(0, 0);

		mTableModel = (LocationTableModel) mLocationsTable.getModel();
		mTableModel.addTableModelListener(tableChangedAction());

		// scroll
		mLocationScrollTable = new JScrollPane(mLocationsTable);
		mLocationScrollTable.setMinimumSize(new Dimension(70, 250));

	}

	private void initLayout() {
		setLayout(new GridBagLayout());

		Insets insets = new Insets(2, 2, 2, 2);
		int row = 0;

		// 0
		add(mLocationScrollTable, new GridBagConstraints(0, row, 3, 1, 1, 1,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						15, 15, 5, 15), 0, 0));

		// 1
		row++;
		add(new JSeparator(SwingConstants.HORIZONTAL), new GridBagConstraints(
				0, row, 3, 1, 1, 1, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, insets, 0, 0));
		// 2
		row++;
		JLabel locationLabel = new JLabel(gLocationText);
		add(locationLabel, new GridBagConstraints(0, row, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						15, 5, 15), 0, 0));

		add(mNewLocationField, new GridBagConstraints(1, row, 2, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 15), 0, 0));

		// 3
		row++;
		add(mBtnDelete, new GridBagConstraints(0, row, 1, 1, 1, 1,
				GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE,
				new Insets(5, 15, 15, 15), 0, 0));

		add(mBtnCancel, new GridBagConstraints(1, row, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						0, 15, 0), 0, 0));

		add(mBtnSave, new GridBagConstraints(2, row, 1, 1, 1, 1,
				GridBagConstraints.LAST_LINE_END, GridBagConstraints.NONE,
				new Insets(5, 0, 15, 15), 0, 0));

		row++;

		// pack();

		mBtnDelete.setVisible(false);
	}

	// ##### actions #####

	private MouseAdapter onTableClickAction() {
		return new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {

				JTable target = (JTable) evt.getSource();
				int row = target.getSelectedRow();
				int col = target.getSelectedColumn();

				if (row >= 0 && col >= 0) {
					Object data = mLocationsTable.getModel().getValueAt(row,
							col);
					if (data != null) {
						updateComponents(data);

						if (evt.getClickCount() == 2) {
							locationWindowObservable
									.sendChangeLocationEvent((Location) data);
							setVisible(false);
						}
					}
				}
			}

		};
	}

	private TableModelListener tableChangedAction() {
		return new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				TableModel model = (TableModel) e.getSource();
				Object data = model.getValueAt(row, column);
				int selected = model.getRowCount() - 1;

				mLocationsTable.getSelectionModel().setSelectionInterval(
						selected, selected);
				updateComponents(data);
			}
		};
	}

	private ActionListener saveAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String newName = mNewLocationField.getText();
				if (gDefaultAddNewText.equals(newName) || EMPTY.equals(newName))
					return;

				Location location = getSelectedRow();
				mTableModel.save(location, newName);
			}
		};
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
				Location location = getSelectedRow();
				if (location == null || location.isDefault()
						|| !location.isEditable())
					return;

				mTableModel.delete(location);
			}

		};
	}

	private FocusListener focusLocationFieldAction() {
		return new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				JTextField field = (JTextField) e.getSource();
				String fieldText = field.getText();
				if (EMPTY.equals(fieldText))
					field.setText(gDefaultAddNewText);
			}

			@Override
			public void focusGained(FocusEvent e) {
				JTextField field = (JTextField) e.getSource();
				String fieldText = field.getText();
				if (gDefaultAddNewText.equals(fieldText))
					field.setText(EMPTY);
			}
		};
	}

	// ##### helpers #####

	public void setLocations(List<Location> locations) {
		mTableModel.setLocations(locations);
		updateComponents(mTableModel.getValueAt(0, 0));
	}

	private Location getSelectedRow() {
		int selectedRow = mLocationsTable.getSelectedRow();
		if (selectedRow < 1)
			return null;

		return (Location) mTableModel.getValueAt(selectedRow, 0);
	}

	private void updateComponents(Object data) {
		mNewLocationField.setText(data.toString());
		boolean isEditable = !gDefaultAddNewText.equals(data.toString());

		if (data instanceof Location) {
			Location location = (Location) data;
			isEditable = !location.isDefault() && location.isEditable();
		}
		mNewLocationField.setEditable(isEditable);
		mBtnDelete.setVisible(isEditable);
	}

	// ##### observable class #####

	private static class LocationWindowObservable extends Observable {

		public void sendChangeLocationEvent(Location location) {
			setChanged();
			super.notifyObservers(location);
		}
	}

	public void addObserver(Observer object) {
		locationWindowObservable.addObserver(object);
	}

	public void deleteObserver(Observer object) {
		locationWindowObservable.deleteObserver(object);
	}
}
