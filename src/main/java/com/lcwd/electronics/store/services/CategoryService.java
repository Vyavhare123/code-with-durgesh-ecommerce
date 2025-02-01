package com.lcwd.electronics.store.services;

import com.lcwd.electronics.store.dtos.CategoryDto;
import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.exception.NoSuchFileException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {
	
	//create service
	
		CategoryDto createCategory( CategoryDto categoryDto);
		
		//update 
		CategoryDto updateCategory (CategoryDto categoryDto , String categoryId );
		
		//delete
		
		void deletCatogory(String categoryId);
		
		//get all
		PageableResponse<CategoryDto> getAllCategory(int pagenumber, int pagesize,String sortBy ,String sortDir);
		
		//get single category
		
		CategoryDto getcategoryById(String categoryId);
		
		String uploadCategoryImage(MultipartFile file,String path) throws IOException;
		
		InputStream getCategoryImage(String path,String name) throws NoSuchFileException ,IOException;

}
