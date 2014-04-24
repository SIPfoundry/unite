package org.ezuce.commons.ui.location;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jivesoftware.smack.packet.Presence;

public class Location {

	private String mName;
	private boolean editable = true;
	private boolean isDefault;
	private Icon icon;
	private List<Presence> presences = new ArrayList<Presence>();

	public Location(String name, boolean editable) {
		this.mName = name;
		this.editable = editable;
	}

	public Location(String location) {
		this.mName = location;
	}

	public Location(String name, ImageIcon icon) {
		this.mName = name;
		this.icon = icon;
		this.isDefault = true;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return mName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (!(obj instanceof Location))
			return false;

		Location l = (Location) obj;

		return mName.equals(l.getName());
	}

	@Override
	public int hashCode() {
		int hash = 31;
		hash = 7 + hash * mName.hashCode();
		return hash;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public void addPresence(Presence presence) {
		if (presence == null)
			return;

		presences.add(presence);
	}

	public Presence getPresence(String defaultPresence) {
		for (Presence pres : presences) {
			if (pres.getStatus().equals(defaultPresence)) {
				return pres;
			}
		}
		return null;
	}

	public List<Presence> getPresences() {
		return presences;
	}

}
