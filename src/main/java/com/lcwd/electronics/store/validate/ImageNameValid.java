package com.lcwd.electronics.store.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {
	
	// Default error message when validation fails
	String message() default "Invalid Image Name";
	// Required by Bean Validation API for grouping constraints (not commonly used)
	Class<?>[] groups() default { };
	 // Optional metadata for additional processing (not commonly used)
	Class<? extends Payload>[] payload() default { };


}
