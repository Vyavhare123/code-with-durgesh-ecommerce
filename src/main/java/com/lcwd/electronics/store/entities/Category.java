package com.lcwd.electronics.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table (name = "category")
public class Category {
	
	
	@Id
	@Column(name ="category_id" )
	private String categoryId;
	
	@Column(name = "category_title",length = 1000,nullable = false)
	private String title;
	@Column(name = "category_disc",length = 1000)
	private String discription;

	private String coverImage;

}
