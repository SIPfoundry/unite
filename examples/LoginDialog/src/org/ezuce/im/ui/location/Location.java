package org.ezuce.im.ui.location;

public class Location {

	private String name;

	public Location() {

	}

	public Location(String location) {
		this.name = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
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

		return name.equals(l.getName());
	}

	@Override
	public int hashCode() {
		int hash = 31;
		hash = 7 + hash * name.hashCode();
		return hash;
	}

}
