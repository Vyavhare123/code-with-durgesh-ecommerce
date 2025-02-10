package com.lcwd.electronics.store.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.lcwd.electronics.store.dtos.ProductDto;
import com.lcwd.electronics.store.exception.NoSuchFileException;
import com.lcwd.electronics.store.services.FileService;
import com.lcwd.electronics.store.services.ProductService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;
	@Value("${product.image.path}")
	private String imageUploadPath;

	// save product
	@PostMapping
	public ResponseEntity<ProductDto> saveProduct(@Valid@RequestBody ProductDto productDto) {
		ProductDto saveProdct = productService.saveProdct(productDto);
		return new ResponseEntity<ProductDto>(saveProdct, HttpStatus.CREATED);

	}

	// update product
	@PutMapping("/{prodctId}")
	public ResponseEntity<ProductDto> updateProduct(@Valid@RequestBody ProductDto productDto,
			@PathVariable(value = "prodctId") String prodctId) {
		ProductDto updateProdct = productService.updateProdct(productDto, prodctId);
		return new ResponseEntity<ProductDto>(updateProdct, HttpStatus.CREATED);
	}

	// delete product
	@DeleteMapping("/{prodctId}")
	public ResponseEntity<ApiResponseMassage> deleteProduct(@PathVariable(value = "prodctId") String prodctId) throws NoSuchFileException {
		   productService.deletProduct(prodctId);
		 ApiResponseMassage apiResponseMassage = new ApiResponseMassage();
			apiResponseMassage.setMassage("prodcut Deleted successfully having id : " + prodctId);
			apiResponseMassage.setStatus(HttpStatus.OK);
			apiResponseMassage.setSuccess(true);
			apiResponseMassage.setPath("/product/" + prodctId);
		return new ResponseEntity<ApiResponseMassage>(apiResponseMassage, HttpStatus.OK);
	}
	
	@GetMapping("/{prodctId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable(value = "prodctId") String prodctId) {
		   ProductDto productbyId = productService.getProductbyId(prodctId);
		return new ResponseEntity<ProductDto>(productbyId, HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
		@RequestParam(value = "pageNumber",defaultValue ="0",required = false)int pageNumber ,
		@RequestParam(value = "pageSize",defaultValue ="10",required = false)int pageSize,
		@RequestParam(value = "sortBy",defaultValue ="title",required = false)String sortBy,
		@RequestParam(value = "sortDir",defaultValue ="desc",required = false)String sortDir
			){
		
		PageableResponse<ProductDto> allProduct = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allProduct,HttpStatus.OK);
	}
	 @GetMapping("/searchTitle/{quary}")
	public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
			@PathVariable String quary,
			@RequestParam(value = "pageNumber",defaultValue ="0",required = false)int pageNumber ,
			@RequestParam(value = "pageSize",defaultValue ="10",required = false)int pageSize,
			@RequestParam(value = "sortBy",defaultValue ="title",required = false)String sortBy,
			@RequestParam(value = "sortDir",defaultValue ="desc",required = false)String sortDir
			
				){
			
			PageableResponse<ProductDto>searchProductByTitle = productService.serachProduct(quary,pageNumber, pageSize, sortBy, sortDir);
			return new ResponseEntity<PageableResponse<ProductDto>>(searchProductByTitle,HttpStatus.OK);
		}
	 @GetMapping("/liveProduct")
		public ResponseEntity<PageableResponse<ProductDto>> liveProduct(
				@RequestParam(value = "pageNumber",defaultValue ="0",required = false)int pageNumber ,
				@RequestParam(value = "pageSize",defaultValue ="10",required = false)int pageSize,
				@RequestParam(value = "sortBy",defaultValue ="title",required = false)String sortBy,
				@RequestParam(value = "sortDir",defaultValue ="desc",required = false)String sortDir
					){
				
				PageableResponse<ProductDto>searchLiveProduct = productService.searchLiveproduct(pageNumber, pageSize, sortBy, sortDir);
				return new ResponseEntity<PageableResponse<ProductDto>>(searchLiveProduct,HttpStatus.OK);
			}
	 
	 @PostMapping("/productImage/{productId}")
		public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("productImage") MultipartFile file,
				@PathVariable("productId") String productId) throws IOException {
			// copy image to folder and get image name
			String imageName = fileService.uploadfile(file, imageUploadPath);

			// get product to update image name
			 ProductDto product = productService.getProductbyId(productId);
			 product.setProductImageName(imageName);

			// update image name for product table 
			 productService.updateProdct(product, productId);

			// return details information to user
			ImageResponse imageResponse = new ImageResponse();
			imageResponse.setImageName(imageName);
			imageResponse.setMassage(imageName + " uploaded successfully");
			imageResponse.setPath(imageUploadPath);
			imageResponse.setStatus(HttpStatus.CREATED);
			imageResponse.setSuccess(true);
			return new ResponseEntity<ImageResponse>(imageResponse, HttpStatus.CREATED);
		}
	 
	 @GetMapping("/productImage/{productId}")
		public ResponseEntity<Resource> getImage(@PathVariable("productId") String productId)
				throws NoSuchFileException, IOException {

		 ProductDto product = productService.getProductbyId(productId);
		 log.info("product service invoke ***********************************************************", productId);
		 log.info("product ***************", product);
		 
			InputStream imageStream = fileService.getResource(imageUploadPath, product.getProductImageName());
			log.info("imageStream service invoke ***********************************************************");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // Change based on the image type
					.body(new InputStreamResource(imageStream));
		}

}
