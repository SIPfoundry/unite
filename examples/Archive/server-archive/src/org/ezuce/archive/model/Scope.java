package org.ezuce.archive.model;

public enum Scope {

	global, // the setting will remain for next streams.

	stream // By Default. The setting is true only until the end of the stream.
			// For next stream, server default value will be used.
	;

	public static Scope parseScope(String scope) {
		if (scope != null) {
			for (Scope s : values()) {
				if (s.toString().equals(scope))
					return s;
			}
		}
		return null;
	}
}
