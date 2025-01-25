package com.lcwd.electronics.store.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronics.store.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	List<User> findByName(String name);
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPassword(String email ,String password);

}
