package org.ezuce.archive.model;


public enum Type {

	auto, // -- preferences for use of automatic archiving on the user's server.
	local, // -- preferences for use of local archiving to a file or database on the user's machine or device.
	manual, // -- preferences for use of manual archiving by the user's client to the user's server
	;
	
	public static Type parseType(String val) {
		if (val != null) {
			for (Type s : values()) {
				if (s.toString().equals(val))
					return s;
			}
		}
		return null;
	}
}
