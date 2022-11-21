package org.hectormoraga.fileuploaddemo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hectormoraga.fileuploaddemo.messages.ResponseFile;
import org.hectormoraga.fileuploaddemo.messages.ResponseMessage;
import org.hectormoraga.fileuploaddemo.model.FileDB;
import org.hectormoraga.fileuploaddemo.service.FilesStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@CrossOrigin("http://localhost:8081")
public class FileController {
	private static Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	FilesStorageService storageService;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {		
		String message = "";
		
		try {
			FileDB fileDB = storageService.store(file);

			message = "Uploaded the file successfully: " + fileDB.getId().toString();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			logger.warn("error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<ResponseFile>> getListFiles() {
		List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/uploads/")
					.path(dbFile.getId().toString())
					.toUriString();
			
			return new ResponseFile(
					dbFile.getFileName(),
					fileDownloadUri,
					dbFile.getMimeType(),
					dbFile.getSize());
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/files/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable String id) throws IOException {
	    FileDB fileDB = storageService.getFile(id);
		Resource file = storageService.load(id);
		Path filePath = file.getFile().toPath();
		byte[] bytes = Files.readAllBytes(filePath);
		String fileName = fileDB.getFileName();
		String mimeType = fileDB.getMimeType();
		
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
	        .header(HttpHeaders.CONTENT_TYPE, mimeType)
	        .body(bytes);
	  }
}