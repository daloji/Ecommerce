package com.ecommerce.cozashop.service;
import static java.util.Objects.nonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileStorageService {

	private static Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

	@Value("${resources.directory}")
	private String directory;

	private  Path  root;

	public FileStorageService() {
			
	}

	public List<String> saveFiles(List<MultipartFile> listMultipart) {
		List<String> listFile = new ArrayList<String>();
		try {
			if(nonNull(listMultipart)) {
				for(MultipartFile multipart:listMultipart) {
					String filename = saveFile(multipart);
					if(nonNull(filename)) {
						listFile.add(filename);
					}
					LOGGER.info("FileStorageService add file : "+ multipart.getName());
				}
			}	
		}catch (Exception e) {
			LOGGER.error("FileStorageService saveFiles failed "+ e.getMessage());
		}
		return listFile;
	}


	public String saveFile(MultipartFile multipart) {
		String filename = null;
		try {
			root = Paths.get(directory);	
			if(!Files.exists(root)) {
				Files.createDirectories(root);
			}
			filename = UUID.randomUUID().toString();
			Path filePath = Paths.get(root.toString(),filename);
			Files.copy(multipart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			LOGGER.info("FileStorageService add file : "+ filePath.toString());
			filename = "/images/" +filename;

		} catch (IOException e) {
			LOGGER.error("FileStorageService saveFile failed "+ e.getMessage());
		}
		return filename;
	}


	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

}
