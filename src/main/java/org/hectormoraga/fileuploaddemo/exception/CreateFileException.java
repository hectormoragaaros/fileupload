package org.hectormoraga.fileuploaddemo.exception;

public class CreateFileException extends RuntimeException {
	private static final long serialVersionUID = 3702052070950608904L;

	public CreateFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreateFileException(String message) {
		super(message);
	}

	public CreateFileException(Throwable cause) {
		super(cause);
	}	
}
