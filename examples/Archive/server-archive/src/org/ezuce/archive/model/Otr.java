package org.ezuce.archive.model;

public enum Otr {

	approve, // -- the user MUST explicitly approve off-the-record communication.
	concede, // -- communications MAY be off the record if requested by another user.
	forbid, // -- communications MUST NOT be off the record.
	oppose, // -- communications SHOULD NOT be off the record even if requested.
	prefer, // -- communications SHOULD be off the record if possible.
	require, ; // -- communications MUST be off the record. *

	public static Otr parseOtr(String val) {
		if (val != null) {
			for (Otr v : values()) {
				if (v.toString().equals(val))
					return v;
			}
		}
		return null;
	}

}
