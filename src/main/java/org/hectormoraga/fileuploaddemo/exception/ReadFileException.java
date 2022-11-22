package org.hectormoraga.fileuploaddemo.exception;

public class ReadFileException extends Exception {
	private static final long serialVersionUID = 3114334379932871759L;

	public ReadFileException(String message) {
		super(message);
	}

	public ReadFileException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
