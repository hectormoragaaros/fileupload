package org.hectormoraga.fileuploaddemo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.tika.Tika;
import org.hectormoraga.fileuploaddemo.exception.FileStorageException;
import org.hectormoraga.fileuploaddemo.exception.ReadFileException;
import org.hectormoraga.fileuploaddemo.model.FileDB;
import org.hectormoraga.fileuploaddemo.repository.FileDBRepository;
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
	private final Path root = Paths.get("uploads");

	public void init() throws FileSystemException {
		try {
			Files.createDirectory(root);
		} catch (IOException exc) {
			throw new FileSystemException(root.toString(), "Could not initialize folder for upload.", exc.getMessage());
		}
	}

	public FileDB store(MultipartFile file) {
		FileDB fileDB = new FileDB();

		if (file != null) {
			try {
				String originalFilename = file.getOriginalFilename();
				String mimeType = new Tika().detect(file.getInputStream());

				if (originalFilename != null) {
					String filename = StringUtils.cleanPath(originalFilename);
					fileDB = fileDBRepository.save(new FileDB(filename, mimeType, (int) file.getSize()));

					Files.copy(file.getInputStream(), this.root.resolve(fileDB.getId().toString()));
				}
			} catch (IOException ex) {
				throw new FileStorageException(ex);
			}
		}

		return fileDB;
	}

	// version que funciona para cargar archivos dado su filename
	/**
	 * Método para cargar un archivo dado su uuid
	 * 
	 * @param uuid uuid correspondiente al nombre del archivo como está almacenado
	 *             en la aplicación.
	 * @return recurso asociado (el archivo)
	 */
	public Resource load(String uuid) throws MalformedURLException, ReadFileException {
		Path file = root.resolve(uuid);
		try {
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new ReadFileException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new ReadFileException("Malformed URL:", e);
		} catch (ReadFileException e) {
			throw new ReadFileException(e.getMessage());
		}
	}

	/**
	 * Me retorna un objeto con todos los datos del archivo a recuperar:
	 * 
	 * @param uuid nombre con el cual fue almacenado el archivo en la aplicación.
	 * @return objeto FileDB con los datos del archivo recuperado (nombre, tipo
	 *         MIME, tamaño en bytes)
	 */
	public Optional<FileDB> getFile(String uuid) {
		return fileDBRepository.findById(UUID.fromString(uuid));
	}

	// sirve para borrar todos los archivos del directorio del servidor
	public void deleteAll() throws FileSystemException {
		if (!FileSystemUtils.deleteRecursively(root.toFile())) {
			String msg = "No fue posible borrar el directorio ::" + root.toString();
			throw new FileSystemException(msg);
		}
	}

	public Stream<FileDB> getAllFiles() {
		return fileDBRepository.findAll().stream();
	}
}