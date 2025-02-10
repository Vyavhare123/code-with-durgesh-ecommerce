package com.lcwd.electronics.store.dtos;
import java.util.Date;

import com.lcwd.electronics.store.validate.ImageNameValid;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
	
    @NotNull(message = "Product Id should be null")
    private String productId;
    
    @Size(min = 4,max = 100,message = "title should between 4 to 100 character")
	private String title;
    @Size(min = 4,max = 1000,message = "title should between 4 to 1000 character")
	private String discription;
    @Positive(message = "price should not be zero or negative")
	private int price;
    @Positive(message = "discountedPrice should not be zero or negative")
	private int discountedPrice;
    @PositiveOrZero(message = "price should zero or positive")
	private int quantity;
	private Date addedDate;
    @NotNull(message = "Live status cannot be null")
	private boolean live;
    @NotNull(message = "stock status cannot be null")
	private boolean stock;
    @ImageNameValid
    private String ProductImageName;

}
