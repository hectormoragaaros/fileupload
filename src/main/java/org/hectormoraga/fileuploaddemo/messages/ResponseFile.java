package org.hectormoraga.fileuploaddemo.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseFile {
	private String name;
	private String url;
	private String type;
	private long size;
	
}
