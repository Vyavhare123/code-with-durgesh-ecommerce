package com.lcwd.electronics.store.exception;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.lcwd.electronics.store.dtos.ApiResponseMassage;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(ResourceNoFoundException.class)
	public ResponseEntity<ApiResponseMassage> resouceNotFoundException(ResourceNoFoundException ex ,WebRequest request){
		
		logger.info("resouceNotFoundException Envoked !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		ApiResponseMassage apiResponseMassage= new ApiResponseMassage();
		apiResponseMassage.setMassage(ex.getMessage());
		apiResponseMassage.setStatus(HttpStatus.NOT_FOUND);
		apiResponseMassage.setSuccess(true);
		apiResponseMassage.setPath(request.getDescription(false));
		return new ResponseEntity<>(apiResponseMassage, HttpStatus.NOT_FOUND);
	}
	
	//MethodArgumentNotValidException, which occurs when validation on an argument annotated with @Valid fails
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String ,Object>> handleValidationExceptions(MethodArgumentNotValidException ex){
		
		 Map<String, String> fieldErrors = new HashMap<>();
		// Prepare the response as a map
		 Map<String, Object> response = new HashMap<>();
		  // Extract validation errors
		 for(FieldError  error: ex.getBindingResult().getFieldErrors()) {
			 //fieldErrors.put(error.getField(), error.getDefaultMessage());
			 response.put(error.getField(), error.getDefaultMessage());
		 }
		 
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	//DataIntegrityViolationException : its come when you try insert already existing email id to database.
	
	//private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponseMassage> dataIntegrityViolationException(DataIntegrityViolationException ex ,WebRequest request){
		
		logger.info("dataIntegrityViolationException Envoked !!");
		ApiResponseMassage apiResponseMassage= new ApiResponseMassage();
		//apiResponseMassage.setMassage("Email id allredy exist");
		apiResponseMassage.setMassage(ex.getMessage());
		apiResponseMassage.setStatus(HttpStatus.BAD_REQUEST);
		apiResponseMassage.setSuccess(false);
		apiResponseMassage.setPath(request.getDescription(false));
		return new ResponseEntity<>(apiResponseMassage, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<ApiResponseMassage> propertyReferenceException(PropertyReferenceException ex ,WebRequest request){
		
		logger.info("propertyReferenceException Envoked !!");
		ApiResponseMassage apiResponseMassage= new ApiResponseMassage();
		apiResponseMassage.setMassage(ex.getMessage());
		apiResponseMassage.setStatus(HttpStatus.BAD_REQUEST);
		apiResponseMassage.setSuccess(false);
		apiResponseMassage.setPath(request.getDescription(false));
		return new ResponseEntity<>(apiResponseMassage, HttpStatus.NOT_FOUND);
	}
	// exception come when image has invalid extension.
	
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponseMassage> badApiRequestException(BadApiRequestException ex ,WebRequest request){
		
		logger.info("Bad ApiRequest Envoked !!");
		ApiResponseMassage apiResponseMassage= new ApiResponseMassage();
		apiResponseMassage.setMassage(ex.getMessage());
		apiResponseMassage.setStatus(HttpStatus.BAD_REQUEST);
		apiResponseMassage.setSuccess(false);
		apiResponseMassage.setPath(request.getDescription(false));
		return new ResponseEntity<>(apiResponseMassage, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(NoSuchFileException.class)
	public ResponseEntity<ApiResponseMassage> noSuchFileException(NoSuchFileException ex ,WebRequest request){
		
		logger.info("nosuchFileFoundException  Envoked *********************************");
		ApiResponseMassage apiResponseMassage= new ApiResponseMassage();
		apiResponseMassage.setMassage(ex.getMessage());
		apiResponseMassage.setStatus(HttpStatus.NOT_FOUND);
		apiResponseMassage.setSuccess(false);
		apiResponseMassage.setPath(request.getDescription(false));
		return new ResponseEntity<>(apiResponseMassage, HttpStatus.NOT_FOUND);
	}
	
	//IOException
	

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ApiResponseMassage> handleIOException(IOException ex ,WebRequest request){
		
		logger.info("IOException  Envoked *********************************");
		ApiResponseMassage apiResponseMassage= new ApiResponseMassage();
		apiResponseMassage.setMassage(ex.getMessage());
		apiResponseMassage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		apiResponseMassage.setSuccess(false);
		apiResponseMassage.setPath(request.getDescription(false));
		return new ResponseEntity<>(apiResponseMassage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseMassage> handleGeneralException(Exception ex, WebRequest request) {
	    logger.error("General Exception Invoked", ex);
	    ApiResponseMassage apiResponseMassage = new ApiResponseMassage();
	    apiResponseMassage.setMassage("An error occurred: " + ex.getMessage());
	    apiResponseMassage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	    apiResponseMassage.setSuccess(false);
	    apiResponseMassage.setPath(request.getDescription(false));
	    return new ResponseEntity<>(apiResponseMassage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
