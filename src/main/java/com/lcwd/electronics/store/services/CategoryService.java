package com.lcwd.electronics.store.services;

import com.lcwd.electronics.store.dtos.CategoryDto;
import com.lcwd.electronics.store.dtos.PageableResponse;

import java.util.*;

import org.springframework.data.domain.Page;

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
		
		CategoryDto getSingleCategory(String categoryId);

}
