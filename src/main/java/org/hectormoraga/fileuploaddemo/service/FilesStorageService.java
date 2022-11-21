package org.hectormoraga.fileuploaddemo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.hectormoraga.fileuploaddemo.model.FileDB;
import org.hectormoraga.fileuploaddemo.repository.FileDBRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageService {

	@Autowired
	private FileDBRepository fileDBRepository;
	private static Logger logger = LoggerFactory.getLogger(FilesStorageService.class);
	private final Path root = Paths.get("uploads");

	public void init() {
		try {
			Files.createDirectory(root);
		} catch (IOException e) {
			logger.warn(e.getMessage());
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	public FileDB store(MultipartFile file) throws IOException {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB = new FileDB(filename, file.getContentType(), (int)file.getSize());		
		FileDB fileDB2 = fileDBRepository.save(fileDB);
		Files.copy(file.getInputStream(), this.root.resolve(fileDB2.getId().toString()));
		
		return fileDB2;
	}
	
	// version que funciona para cargar archivos dado su filename
	public Resource load(String uuid) {
		try {
			Path file = root.resolve(uuid);
			Resource resource = new UrlResource(file.toUri());
			logger.info("filepath:{}", file);
			logger.info("resource:{}", resource);
			
			if (resource.exists() || resource.isReadable()) {
				
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	public FileDB getFile(String id) {
		Optional<FileDB> theFile = fileDBRepository.findById(UUID.fromString(id));
		
		if (theFile.isPresent()) {
			return theFile.get();
		} else throw new NoSuchElementException("No file exists with such id:" + id);
	}

	// sirve para borrar todos los archivos del directorio del servidor
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	public Stream<FileDB> getAllFiles(){
		return fileDBRepository.findAll().stream();
	}
}