package com.lcwd.electronics.store.repositories;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronics.store.entities.Product;
@Repository
public interface ProductRespository extends JpaRepository<Product, String>{
	Page<Product>  findByLiveTrue(Pageable pageable);
	Page<Product> findByTitleContaining(String title, Pageable pageable);
}
