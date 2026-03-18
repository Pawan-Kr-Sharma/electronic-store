package com.lcwd.electronic.exceptions;

import com.lcwd.electronic.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND).build();
        logger.info("Exception Handler invoke !!");
        return new ResponseEntity(response,HttpStatus.NOT_FOUND);
    }
    //MethodArgumentNotValidException
   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
       List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
  //create map to store all error
       Map<String,Object> response=new HashMap<>();
       allErrors.stream().forEach(objectError -> {
           String message = objectError.getDefaultMessage();
           String field = ((FieldError) objectError).getField();
           response.put(field,message);
       });
       return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
   }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequestHandler(BadApiRequestException ex){
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        logger.info("Bad Api Request !!");
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

}
