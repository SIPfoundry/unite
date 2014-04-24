package org.ezuce.archive.model;

public enum Use {

	concede, // -- this method MAY be used if no other methods are available.
	forbid, // -- this method MUST NOT be used.
	prefer, // -- this method SHOULD be used if available.
	
	;
	
	public static Use parseUse(String val) {
		if (val != null) {
			for (Use s : values()) {
				if (s.toString().equals(val))
					return s;
			}
		}
		return null;
	}
}
