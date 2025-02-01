package com.lcwd.electronics.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronics.store.exception.BadApiRequestException;
import com.lcwd.electronics.store.exception.NoSuchFileException;
import com.lcwd.electronics.store.services.FileService;
@Service
public class FileServiceImpl implements FileService {

	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadfile(MultipartFile file, String path) throws IOException {
		String originalFilename = file.getOriginalFilename();
		logger.info("File Name :{}", originalFilename);
		
		// Validate file extension
		if (originalFilename == null || originalFilename.isEmpty()) {
	        throw new BadApiRequestException("File name is missing");
	    }
		
		// Create unique filename
		String randomId = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		String filenameWithExtention = randomId + extension;
		String fullpath = path+filenameWithExtention;

		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".jpeg")) {

			// Ensure the directory exists

			File folder = new File(path);
			// create folder if not exist
			if (!folder.exists()) {

				 boolean dirCreated=folder.mkdirs();
				 if(!dirCreated) {
					 throw new IOException("Failed to create directory: " + path);
				 }
			}

			// upload file or image

			Files.copy(file.getInputStream(), Paths.get(fullpath));
			return filenameWithExtention;
		} else {

			throw new BadApiRequestException("Unsupported file type. Allowed types are: .png, .jpg, .jpeg");
		}

	}
//  Duregesh course code to serve image
//	@Override
//	public InputStream getResource(String path, String name) throws FileNotFoundException {
//		String fullpath = path + File.separator + name;
//		InputStream inputStream=new FileInputStream(fullpath);
//		return inputStream;
//	}

	@Override
	public InputStream getResource(String path, String name) throws IOException, NoSuchFileException {
		File file =new File(path,name);
		if(!file.exists()) {
			throw new NoSuchFileException("Image not found at: " + file.getAbsolutePath());
		}
		InputStream inputStream= new FileInputStream(file);
		return inputStream;
	}
	
	

}
