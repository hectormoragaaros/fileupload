package org.hectormoraga.fileuploaddemo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.hectormoraga.fileuploaddemo.exception.ResourceNotFoundException;
import org.hectormoraga.fileuploaddemo.messages.ErrorResponse;
import org.hectormoraga.fileuploaddemo.messages.ResponseFile;
import org.hectormoraga.fileuploaddemo.model.FileDB;
import org.hectormoraga.fileuploaddemo.service.FilesStorageService;
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
	@Autowired
	FilesStorageService storageService;

	@PostMapping("/upload")
	public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		try {
			FileDB fileDB = storageService.store(file);
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath().path("/uploads/")
					.path(fileDB.getId().toString())
					.toUriString();
			message = "File Uploaded successfully!";
			
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseFile(fileDB.getFileName(), fileDownloadUri, fileDB.getMimeType(), fileDB.getSize(), message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity
					.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ErrorResponse(message));
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<ResponseFile>> getListFiles() {
		List<ResponseFile> responseList = storageService.getAllFiles()
				.stream()
				.map(fileDb -> new ResponseFile(fileDb.getFileName(), 
							ServletUriComponentsBuilder
								.fromCurrentContextPath().path("/files/")
								.path(fileDb.getId().toString())
								.toUriString(),
							fileDb.getMimeType(),
							fileDb.getSize(),
							(fileDb!=null)?"operation successfull":"file not exist"))
				.toList();

		if (!responseList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(responseList);			
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		}
	}

	/**
	 * Endpoint REST que me permite obtener un archivo ya almacenado en el servidor,
	 * dado su uuid (como texto)
	 */
	@GetMapping("/files/{uuid}")
	public ResponseEntity<byte[]> getFile(@PathVariable String uuid) throws ResourceNotFoundException {
		FileDB fileDB = storageService.getFile(uuid)
				.orElseThrow( () ->  new ResourceNotFoundException("File not found in DB for this UUID ::" + uuid));

		try {
			Resource file = storageService.load(uuid);
			Path filePath = file.getFile().toPath();

			byte[] bytes = Files.readAllBytes(filePath);

			String fileName = fileDB.getFileName();
			String mimeType = fileDB.getMimeType();

			return ResponseEntity
					.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.header(HttpHeaders.CONTENT_TYPE, mimeType)
					.body(bytes);
		} catch (Exception ex) {
			throw new ResourceNotFoundException("File not found physically for this UUID ::" + uuid);
		}
	}
}