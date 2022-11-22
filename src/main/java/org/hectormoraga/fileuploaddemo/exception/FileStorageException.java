package org.hectormoraga.fileuploaddemo.exception;

public class FileStorageException extends RuntimeException {
	private static final long serialVersionUID = 1912739485647970132L;

	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileStorageException(String message) {
		super(message);
	}

	public FileStorageException(Throwable cause) {
		super(cause);
	}
}
