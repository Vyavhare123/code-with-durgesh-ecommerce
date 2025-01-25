package com.lcwd.electronics.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronics.store.dtos.CategoryDto;
import com.lcwd.electronics.store.entities.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
	
	

}
