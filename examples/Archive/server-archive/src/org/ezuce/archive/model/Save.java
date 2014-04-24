package org.ezuce.archive.model;

public enum Save {

	body("body"), // -- the saving entity SHOULD save only <body/> elements. *
	False("false"), // -- the saving entity MUST save nothing.
	message("message"), // -- the saving entity SHOULD save the full XML content
						// of each <message/> element. **
	stream("stream"), // -- the saving entity SHOULD save every byte that passes
						// over the stream in either direction. ***

	;

	private String str;

	private Save(String str) {
		this.str = str;
	}

	public static Save parseSave(String val) {
		if (val != null) {
			for (Save v : values()) {
				if (v.str.equals(val))
					return v;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return str;
	}
}
