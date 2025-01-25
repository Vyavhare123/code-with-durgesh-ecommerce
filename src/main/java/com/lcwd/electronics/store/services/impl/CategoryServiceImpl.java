package com.lcwd.electronics.store.services.impl;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronics.store.dtos.CategoryDto;
import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.dtos.UserDto;
import com.lcwd.electronics.store.entities.Category;
import com.lcwd.electronics.store.entities.User;
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
		category.setCoverImage(categoryDto.getCategoryId());
	     Category updateCategory = categoryRepository.save(category);
		CategoryDto mappEntityToDto = mapper.map(updateCategory, CategoryDto.class);
		return mappEntityToDto;
	}

	@Override
	public void deletCatogory(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoFoundException("No category found with give id :"+categoryId));	
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
	public CategoryDto getSingleCategory(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoFoundException("No category found with give id :"+categoryId));
		CategoryDto mappEntityIntoDto = mapper.map(category, CategoryDto.class);
		return mappEntityIntoDto;
	}

}
