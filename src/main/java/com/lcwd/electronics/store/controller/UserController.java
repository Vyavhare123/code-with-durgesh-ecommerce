package com.lcwd.electronics.store.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
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
import com.lcwd.electronics.store.dtos.ImageResponse;
import com.lcwd.electronics.store.dtos.PageableResponse;
import com.lcwd.electronics.store.dtos.UserDto;
import com.lcwd.electronics.store.services.FileService;
import com.lcwd.electronics.store.services.UserService;
import com.lcwd.electronics.store.exception.NoSuchFileException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private FileService fileService;
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	// create user
	@PostMapping
	public ResponseEntity<UserDto> createUser( @Valid @RequestBody UserDto userDto){
		UserDto createUser=userService.createUser(userDto);
		return new ResponseEntity<>(createUser,HttpStatus.OK) ;	
	}
	
	//update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser( @Valid @RequestBody UserDto userDto ,@PathVariable String userId){
		UserDto updateuser=userService.updateUser(userDto, userId);
		return new  ResponseEntity<>(updateuser,HttpStatus.OK);	 
	}
	
	//delete user
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMassage> deleteUser(@PathVariable String userId) throws NoSuchFileException {
		userService.deleteuser(userId);
		ApiResponseMassage massage=ApiResponseMassage.builder().massage("user is deleted successfully").success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<>(massage,HttpStatus.OK);
		}
	
	// get all user
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUser
	         ( @RequestParam( value = "pagenumber" ,defaultValue = "0" , required = false) int pagenumber, 
			@RequestParam (value = "pageSize" ,defaultValue = "10",required = false) int pageSize,
			@RequestParam (value = "sortBy",defaultValue ="name",required = false) String sortBy,
			@RequestParam (value = "sortDir",defaultValue ="asc",required = false) String sortDir){
		PageableResponse<UserDto> getallUser=userService.getAllUser(pagenumber, pageSize,sortBy,sortDir);
		return new ResponseEntity<> (getallUser,HttpStatus.OK);	
	}
	
	//get single user
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId){
		UserDto user=userService.getUserById(userId);
		return new ResponseEntity<UserDto>(user,HttpStatus.OK);	
	}
	
	// get by email 
	@GetMapping("email/{email}")
	public ResponseEntity<UserDto> getUserbyEmail(@PathVariable String email){
		UserDto userbyemail=userService.getUserByEmail(email);
		return new ResponseEntity<UserDto>(userbyemail,HttpStatus.OK);	
	}
	
	// search user
	@GetMapping("name/{name}")
	public ResponseEntity<List<UserDto>> serachUser(@PathVariable String name){
		List<UserDto> userbyemail=userService.searchUser(name);
		return new ResponseEntity<List<UserDto>>(userbyemail,HttpStatus.OK);	
	}
	
	//upload image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse>uploadUserImage(@RequestParam("userImage") MultipartFile file,@PathVariable("userId") String userId) throws IOException{
		
		String imageName = fileService.uploadfile(file, imageUploadPath);
		UserDto user = userService.getUserById(userId);
		user.setImageName(imageName);
		userService.updateUser(user, userId);
		ImageResponse imageResponse=new ImageResponse();
		imageResponse.setImageName(imageName);
		imageResponse.setMassage(imageName+" uploaded successfully");
		imageResponse.setPath(imageUploadPath );
		imageResponse.setStatus(HttpStatus.CREATED);
		imageResponse.setSuccess(true);
		return new ResponseEntity<ImageResponse>(imageResponse,HttpStatus.CREATED);
		
		
	}
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable ("userId") String userId, HttpServletResponse response) throws IOException {
		UserDto user = userService.getUserById(userId);
		
		log.info("user image name: {}",user.getImageName());
		
		InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream());
	
	}
}
