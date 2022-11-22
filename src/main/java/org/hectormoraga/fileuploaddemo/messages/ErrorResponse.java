package org.hectormoraga.fileuploaddemo.messages;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
	private Date timestamp;
	private String message;
	private String details;
	
	public ErrorResponse(String message) {
		this.timestamp = new Date();
		this.message = message;
	}
}