package com.TrendHive.TrendHive.exceptions;

import com.TrendHive.TrendHive.dto.MerchantDto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handleValidationException(MethodArgumentNotValidException ex){
        List<ValidationErrorResponse> errors = new ArrayList<>();
        for (FieldError error: ex.getBindingResult().getFieldErrors()){
            errors.add(new ValidationErrorResponse(error.getField(), error.getDefaultMessage()));
        }
        return  new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
