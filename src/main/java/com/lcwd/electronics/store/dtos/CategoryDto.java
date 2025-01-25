package com.lcwd.electronics.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDto {
	private String categoryId;
	@NotBlank
	@Size(min = 4, max = 60, message = "Title must have between 4 and 60 characters")
	private String title;
	@Size(min = 4, max = 100, message = "Description must have between 4 and 100 characters")
	private String discription;
	private String coverImage;

}
