package com.lcwd.electronics.store.dtos;


import com.lcwd.electronics.store.validate.ImageNameValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

	private String userId;
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String name;

	//@Email(message = "invalid email format")
	//@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$" ,message = "invalid email id format")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail address")
	private String email;

	@Size(min = 6, max = 9, message = "password must be between 6 and 9 characters")
	
	@NotBlank(message = "Email is request")
	private String password;

	@Size(min = 4, max = 6, message = "invalid gender")
	private String gender;

	@Size(max = 100, message = "maximum 100 letter allowed")
	@NotBlank(message = "About discription is empty")
	private String about;
	
	
	// created custom Anotation for image url for learning purpose
	@ImageNameValid
	private String imageName;
}
