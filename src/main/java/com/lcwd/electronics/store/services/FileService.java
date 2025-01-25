package com.lcwd.electronics.store.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	// This is for uploading images 
	String uploadfile(MultipartFile file,String path) throws IOException;
	
	InputStream getResource(String path,String name) throws IOException;

}
