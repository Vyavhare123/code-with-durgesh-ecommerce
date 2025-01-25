package com.lcwd.electronics.store.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
@Id	
//@GeneratedValue(strategy = GenerationType.IDENTITY)
private String userId;

@Column(name = "user_name",nullable = false,length =20)
private String name;

@Column(name = "user_email",unique = true)
private String email;

@Column(name="user_password",unique = true,length =9)
private  String password;

@Column(name = "user_gender")
private String gender;

@Column(name = "user_about",length = 1000)
private String about;

@Column(name="user_image_name")
private String imageName;

}
