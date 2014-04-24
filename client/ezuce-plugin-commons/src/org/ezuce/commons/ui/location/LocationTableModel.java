package org.ezuce.commons.ui.location;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class LocationTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -4884167026043996726L;
	private List<Object> mLocations = new ArrayList<Object>();
	private final String mDefaultText;

	public LocationTableModel(String defaultAddNewText) {
		this.mDefaultText = defaultAddNewText;
	}

	@Override
	public int getRowCount() {
		return mLocations.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return mLocations.get(rowIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (mDefaultText.equals(aValue.toString()))
			return;

		Location location = new Location(aValue.toString());
		mLocations.add(location);

		fireTableDataChanged();
		fireTableCellUpdated(mLocations.size() - 1, columnIndex);

		save();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 0 && rowIndex == 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public void delete(Location location) {
		int idx = mLocations.indexOf(location);
		if (idx < 1)
			return;

		mLocations.remove(idx);
		save();
	}

	public void save(Location location, String newName) {
		if (location != null) {
			int idx = mLocations.indexOf(location);
			if (idx > 0)
				((Location) mLocations.get(idx)).setName(newName);
		} else {
			mLocations.add(new Location(newName));
		}
		save();
	}

	public void save() {
		LocationManager.save(toXmlListForSaving(mLocations));
		fireTableDataChanged();
	}

	private List<Location> toXmlListForSaving(List<Object> list) {
		List<Location> locations = new ArrayList<Location>();
		for (Object obj : list) {
			if (obj instanceof Location) {
				Location n = (Location) obj;

				if (!n.isDefault() && !locations.contains(n))
					locations.add(n);
			}
		}
		return locations;
	}

	public void setLocations(List<Location> locations) {
		mLocations.clear();
		mLocations.add(mDefaultText);
		mLocations.addAll(locations);
	}

}
