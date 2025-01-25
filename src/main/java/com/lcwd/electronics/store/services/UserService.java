package com.lcwd.electronics.store.services;


import java.util.List;
import com.lcwd.electronics.store.exception.NoSuchFileException;
import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.dtos.UserDto;


public interface UserService {
	
	//create user
	
	UserDto createUser(UserDto userDto);
	
	//update user
	
	UserDto updateUser(UserDto userdto ,String userId);
	
	
  // delete user
	
	void deleteuser(String userId) throws NoSuchFileException  ;
	
	// get All user 
	PageableResponse<UserDto> getAllUser(int pagenumber, int pageSize,String sortBy ,String sortDir);
	
	//get user by id 
	
	UserDto getUserById(String userId);
	
	// get user by email
	
	UserDto getUserByEmail(String email);
	
	//search user
	List<UserDto>searchUser(String Name);

}
