package org.ezuce.media.manager;

public class UnsupportedOperationException extends RuntimeException {

	public UnsupportedOperationException() {
		super("Operation not supported. Unite is not registered as a phone");
	}
}
