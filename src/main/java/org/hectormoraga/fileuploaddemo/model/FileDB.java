package org.hectormoraga.fileuploaddemo.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="file_storage")
public class FileDB {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
    		name = "UUID",
    		strategy = "org.hibernate.id.UUIDGenerator",
    		parameters = {
    				@Parameter(
    						name="uuid_gen_strategy_class",
    						value="org.hibernate.id.uuid.CustomVersionOneStrategy"
    						)
    		}
    )
    @Column(name="id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
	@Column(name = "file_name", nullable = false)
	private String fileName;
	@Column(name = "mime_type", nullable = false)
	private String mimeType;
	@Column(name = "size", nullable = false)
	private int size;
	
	public FileDB() {this.id = UUID.randomUUID();}
	
	public FileDB(String fileName, String mimeType, int size) {
		this.id = UUID.randomUUID();
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.size = size;
	}

	public FileDB(UUID id, String fileName, String mimeType, int size) {
		this.id = id;
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.size = size;
	}	
	
	public UUID getId() {return this.id;}
	public String getFileName() {return this.fileName;}
	public String getMimeType() {return this.mimeType;}
	public int getSize() {return this.size;}
	
	public void setFileName(String fileName) {this.fileName=fileName;}
	public void setMimeType(String mimeType) {this.mimeType=mimeType;}
	public void setSize(int size) {this.size=size;}
	
}
