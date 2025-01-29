package com.lcwd.electronics.store.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronics.store.dtos.CategoryDto;
import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.dtos.UserDto;
import com.lcwd.electronics.store.entities.Category;
import com.lcwd.electronics.store.entities.User;
import com.lcwd.electronics.store.exception.BadApiRequestException;
import com.lcwd.electronics.store.exception.ResourceNoFoundException;
import com.lcwd.electronics.store.helper.Helper;
import com.lcwd.electronics.store.repositories.CategoryRepository;
import com.lcwd.electronics.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper mapper;
	
	@Value("${category.profile.image.path}")
	private String imageUploadPath;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		 Category categortyEntity = mapper.map(categoryDto, Category.class);
		String categoryId = UUID.randomUUID().toString();
		categortyEntity.setCategoryId(categoryId);
		// TODO Auto-generated method stub
		Category saveCategory = categoryRepository.save(categortyEntity);
		CategoryDto categorydto = mapper.map(saveCategory, CategoryDto.class);
		return categorydto;
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
		 Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoFoundException("No category found with give id :"+categoryId));
		category.setDiscription(categoryDto.getDiscription());
		category.setTitle(categoryDto.getTitle());
		category.setCoverImage(categoryDto.getCoverImage());
	     Category updateCategory = categoryRepository.save(category);
		CategoryDto mappEntityToDto = mapper.map(updateCategory, CategoryDto.class);
		return mappEntityToDto;
	}

	@Override
	public void deletCatogory(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoFoundException("No category found with give id :"+categoryId));
		String categotyImage = category.getCoverImage();
		String fullPath =imageUploadPath+categotyImage;
		File file=new File(fullPath);
		if(file.exists() ||file.isFile()) {
		 file.delete();
		}else {
			throw new ResourceNoFoundException("File does not exist at path: " + fullPath);
		}
		categoryRepository.delete(category);
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategory(int pagenumber, int pagesize, String sortBy,
			String sortDir) {
		// create sort object for sorting
				Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

				// create pagable object

				Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);

				Page<Category> page = categoryRepository.findAll(pageable);
				PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
				return pageableResponse;
		
	}

	@Override
	public CategoryDto getcategoryById(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoFoundException("No category found with give id :"+categoryId));
		CategoryDto mappEntityIntoDto = mapper.map(category, CategoryDto.class);
		return mappEntityIntoDto;
	}

	@Override
	public String uploadCategoryImage(MultipartFile file, String path) throws IOException {
		//get file name
		String originalFilename = file.getOriginalFilename();
		if(originalFilename==null || originalFilename.isEmpty()) {
			throw new BadApiRequestException("image name is missing");
		}
		// get .png .jpg extension from image
		String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toString();
		//// Create unique filename
		String uniqueImageName=UUID.randomUUID().toString()+extension;
		//create full image path
		String fullPath=path+uniqueImageName;
		
		if(extension.equalsIgnoreCase(".png")|| extension.equalsIgnoreCase(".jpg")|| extension.equalsIgnoreCase(".jpeg")) 
		{
			File folder=new  File(path);
			if(!folder.exists()) {
				boolean createFolder = folder.mkdirs();
				if(!createFolder) {
					throw new IOException("Failed to create directory: " + path);
				}
				
				
			}
			
			Files.copy(file.getInputStream(), Paths.get(fullPath),StandardCopyOption.REPLACE_EXISTING);
			//return imagename
			return uniqueImageName;
		}else {
			throw new BadApiRequestException("Unsupported file type. Allowed types are: .png, .jpg, .jpeg");
		}
		
		
	}

}
