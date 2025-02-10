package com.lcwd.electronics.store.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lcwd.electronics.store.dtos.CategoryDto;
import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.dtos.ProductDto;
import com.lcwd.electronics.store.exception.NoSuchFileException;

public interface ProductService {
	
	//save product
	
	ProductDto saveProdct(ProductDto productDto);
	
	//update product
	ProductDto updateProdct(ProductDto productDto,String prodctId);
	
	//delete prodct
	void deletProduct(String prodctId) throws NoSuchFileException;
	
	//get Prodcut by id
	ProductDto getProductbyId(String prodctId);
	
	
	// get All product
	
	PageableResponse<ProductDto> getAllProduct(int pagenumber, int pagesize,String sortBy ,String sortDir);
	
	//serach product by title.
	PageableResponse<ProductDto> serachProduct(String title,int pagenumber, int pagesize, String sortBy, String sortDir);
	
	// seach live product
	
	PageableResponse<ProductDto> searchLiveproduct(int pagenumber, int pagesize, String sortBy, String sortDir);

}
