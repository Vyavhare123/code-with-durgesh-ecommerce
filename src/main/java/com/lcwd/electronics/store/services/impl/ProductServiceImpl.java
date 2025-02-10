package com.lcwd.electronics.store.services.impl;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.dtos.ProductDto;
import com.lcwd.electronics.store.entities.Product;
import com.lcwd.electronics.store.exception.NoSuchFileException;
import com.lcwd.electronics.store.exception.ResourceNoFoundException;
import com.lcwd.electronics.store.helper.Helper;
import com.lcwd.electronics.store.repositories.ProductRespository;
import com.lcwd.electronics.store.services.ProductService;
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRespository productRespository;
	@Autowired
	private ModelMapper mapper;
	
	@Value("${product.image.path}")
	private String imageUploadPath;
	
	

	@Override
	public ProductDto saveProdct(ProductDto productDto) {
		String generateRandomId = UUID.randomUUID().toString();
		Product prodct = mapper.map(productDto, Product.class);
		prodct.setProductId(generateRandomId);
		prodct.setAddedDate(new Date());
		Product saveProduct = productRespository.save(prodct);
		ProductDto mapProdctToDto = mapper.map(saveProduct, productDto.getClass());
		return mapProdctToDto;
	}

	@Override
	public ProductDto updateProdct(ProductDto productDto, String prodctId) {
		Product product = productRespository.findById(prodctId).orElseThrow(()-> new ResourceNoFoundException("No product found with give id :"+prodctId));
		product.setTitle(productDto.getTitle());
		product.setDiscription(productDto.getDiscription());
		product.setPrice(productDto.getPrice());
		product.setQuantity(productDto.getQuantity());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setProductImageName(productDto.getProductImageName());
		//product.setAddedDate(productDto.getAddedDate());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		Product updateProduct = productRespository.save(product);
		ProductDto mapProductToDto = mapper.map(updateProduct, ProductDto.class);
		return mapProductToDto;
	}

	@Override
	public void deletProduct(String prodctId) throws NoSuchFileException {
		Product product = productRespository.findById(prodctId).orElseThrow(()-> new ResourceNoFoundException("No product found with give id :"+prodctId));
		// Delete user photo from folder
	    String userImagePath = imageUploadPath + product.getProductImageName();
	    try {
	        File file = new File(userImagePath);
	        if (file.exists() && file.isFile()) {
	            boolean deleted = file.delete();
	            if (!deleted) {
	            	throw new NoSuchFileException("File does not exist at path: " + userImagePath);
	            }
	        } 
	    } catch (Exception e) {
	        // Log error if logging framework is available
	        throw new NoSuchFileException("Error deleting file: " + e.getMessage());
	    }
		productRespository.delete(product);
	}

	@Override
	public ProductDto getProductbyId(String prodctId) {
		Product product = productRespository.findById(prodctId).orElseThrow(()-> new ResourceNoFoundException("No product found with give id :"+prodctId));
		ProductDto mapProductToDto = mapper.map(product, ProductDto.class);
		return mapProductToDto;
	}

	@Override
	public PageableResponse<ProductDto> getAllProduct(int pagenumber, int pagesize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Product> page = productRespository.findAll(pageable);
		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> serachProduct(String title,int pagenumber, int pagesize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
	      Page<Product> sortedproduct = productRespository.findByTitleContaining(title,pageable);
	      PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(sortedproduct, ProductDto.class);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> searchLiveproduct(int pagenumber, int pagesize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
	     Page<Product> liveProduct = productRespository.findByLiveTrue(pageable);
	     PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(liveProduct, ProductDto.class);
		return pageableResponse; 
	}

}
