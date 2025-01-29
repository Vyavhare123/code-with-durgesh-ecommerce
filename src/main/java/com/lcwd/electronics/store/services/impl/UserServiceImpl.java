package com.lcwd.electronics.store.services.impl;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.UUID;
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
import com.lcwd.electronics.store.dtos.UserDto;
import com.lcwd.electronics.store.entities.User;
import com.lcwd.electronics.store.exception.NoSuchFileException;
import com.lcwd.electronics.store.exception.ResourceNoFoundException;
import com.lcwd.electronics.store.helper.Helper;
import com.lcwd.electronics.store.repositories.UserRepository;
import com.lcwd.electronics.store.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	// used ModelMapper instead of manualy mapping the object
	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	// Create User

	@Override
	public UserDto createUser(UserDto userDto) {

		// Generate unique id in string format

		String userID = UUID.randomUUID().toString();
		userDto.setUserId(userID);
		// dto to entity
		// User user = dtoEntity(userDto);
		User user = mapper.map(userDto, User.class);
		User saveUser = userRepository.save(user);
		// Entity to dto
		// UserDto newUserDto = EntityToDto(saveUser);
		UserDto newUserDto = mapper.map(saveUser, UserDto.class);
		return newUserDto;
	}

	// Update User into database
	@Override
	public UserDto updateUser(UserDto userdto, String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNoFoundException("User not found with id :" + userId));
		user.setName(userdto.getName());
		user.setPassword(userdto.getPassword());
		user.setGender(userdto.getGender());
		user.setEmail(userdto.getEmail());
		user.setAbout(userdto.getAbout());
		user.setImageName(userdto.getImageName());

		// update user in database
		User updatedUser = userRepository.save(user);

		// UserDto updatedDto = EntityToDto(updatedUser);
		UserDto updatedDto = mapper.map(updatedUser, UserDto.class);
		return updatedDto;
	}

	@Override
	public void deleteuser(String userId) throws NoSuchFileException  {
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNoFoundException("User not found with ID: " + userId));

	    // Delete user photo from folder
	    String userImagePath = imageUploadPath + user.getImageName();
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

	    // Delete user from repository
	    userRepository.delete(user);
	    log.info("Successfully deleted user with ID: " + userId);

	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pagenumber, int pageSize, String sortBy, String sortDir) {

		// create sort object for sorting
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		// create pagable object

		Pageable pageable = PageRequest.of(pagenumber, pageSize, sort);

		Page<User> page = userRepository.findAll(pageable);

//		List<User> users = page.getContent();
//		List<UserDto> userDtoList = users.stream().map(user -> mapper.map(user, UserDto.class))
//				.collect(Collectors.toList());
//
//		PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
//		pageableResponse.setContent(userDtoList);
//		pageableResponse.setPageNumber(page.getNumber());
//		pageableResponse.setPageSize(page.getSize());
//		pageableResponse.setTotalElement(page.getTotalElements());
//		pageableResponse.setLastpage(page.isLast());
//		pageableResponse.setTotalPage(page.getTotalPages());
		PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
		return pageableResponse;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User is not found with id :-" + userId));

		return mapper.map(user, UserDto.class);

	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNoFoundException("User is not found with  given email id :-" + email));
		// UserDto useremail=EntityToDto(user);
		UserDto useremail = mapper.map(user, UserDto.class);
		return useremail;
	}

	@Override
	public List<UserDto> searchUser(String Name) {
		List<User> users = userRepository.findByName(Name);
		// List<UserDto>userName=users.stream().map(user ->
		// EntityToDto(user)).collect(Collectors.toList());
		List<UserDto> userName = users.stream().map(user -> mapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return userName;
	}

//	private UserDto EntityToDto(User saveUser) {
//
//		UserDto userdto = UserDto.builder().userId(saveUser.getUserId()).name(saveUser.getName())
//				.email(saveUser.getEmail()).password(saveUser.getPassword()).about(saveUser.getAbout())
//				.gender(saveUser.getGender()).imageName(saveUser.getImageName()).build();
//		return userdto;
//	}
//
//	private User dtoEntity(UserDto userDto) {
//		User user = User.builder().userId(userDto.getUserId()).name(userDto.getName()).email(userDto.getEmail())
//				.password(userDto.getPassword()).gender(userDto.getGender()).about(userDto.getAbout())
//				.imageName(userDto.getImageName()).build();
//		return user;
//	}

}
