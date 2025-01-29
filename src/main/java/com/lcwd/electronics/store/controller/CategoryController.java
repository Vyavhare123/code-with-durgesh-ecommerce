package com.lcwd.electronics.store.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronics.store.dtos.ApiResponseMassage;
import com.lcwd.electronics.store.dtos.CategoryDto;
import com.lcwd.electronics.store.dtos.ImageResponse;
import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.dtos.UserDto;
import com.lcwd.electronics.store.services.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@Value("${category.profile.image.path}")
	private String imageUploadPath;
	
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		CategoryDto createCategory = categoryService.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(createCategory,HttpStatus.CREATED);
		
	}
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory (@Valid @RequestBody CategoryDto categoryDto, @PathVariable(value = "categoryId") String categoryId ){
		CategoryDto updateCategory = categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updateCategory,HttpStatus.OK);
		
	}
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMassage> deleteCategory(@PathVariable(value = "categoryId") String categoryId){
		categoryService.deletCatogory(categoryId);
		ApiResponseMassage apiResponseMassage=new ApiResponseMassage();
		apiResponseMassage.setMassage("category deleted succesfully having id : "+ categoryId);
		apiResponseMassage.setStatus(HttpStatus.OK);
		apiResponseMassage.setSuccess(true);
		apiResponseMassage.setPath("/category/"+categoryId);
		return new ResponseEntity<ApiResponseMassage>(apiResponseMassage,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity <PageableResponse<CategoryDto>> getAllCategory(
			@RequestParam (value = "pagenumber",defaultValue = "0" , required = false) int pagenumber,
			@RequestParam (value = "pagesize",defaultValue = "10" , required = false) int pagesize,
			@RequestParam (value = "sortBy",defaultValue = "title" , required = false) String sortBy,
			@RequestParam (value = "sortDir",defaultValue = "asc" , required = false) String sortDir ){
		PageableResponse<CategoryDto> allCategory = categoryService.getAllCategory(pagenumber, pagesize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<CategoryDto>>(allCategory,HttpStatus.OK);
		
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategorById(@PathVariable (value = "categoryId") String categoryId){
		CategoryDto singleCategory = categoryService.getcategoryById(categoryId);
		return new ResponseEntity<CategoryDto>(singleCategory,HttpStatus.OK);
		
		
	}
	
	@PostMapping("/categoryImage/{CategoryId}")
	public ResponseEntity<ImageResponse>uploadCategoryImage(@RequestParam("CategoryImage") MultipartFile file,@PathVariable("CategoryId") String categoryId) throws IOException{
		//copy image to folder and get image name
		String imageName = categoryService.uploadCategoryImage(file, imageUploadPath);
		
		//get category to update image name 
		CategoryDto category = categoryService.getcategoryById(categoryId);
		category.setCoverImage(imageName);
		
		//update image name for respective category in database
		categoryService.updateCategory(category, categoryId);
		
		//return details information to user
		ImageResponse imageResponse=new ImageResponse();
		imageResponse.setImageName(imageName);
		imageResponse.setMassage(imageName+" uploaded successfully");
		imageResponse.setPath(imageUploadPath);
		imageResponse.setStatus(HttpStatus.CREATED);
		imageResponse.setSuccess(true);
		return new ResponseEntity<ImageResponse>(imageResponse,HttpStatus.CREATED);	
	}
	
	//upload user controller pending to write

}
