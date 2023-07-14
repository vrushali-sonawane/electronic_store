package com.bikkadit.electronic_store.exception;

import com.bikkadit.electronic_store.payload.ApiResponseMessage;
import com.bikkadit.electronic_store.payload.AppConstants;
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


    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppConstants> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

        logger.info("Exception Handler invoked !!");

//        ApiResponseMessage response= ApiResponseMessage.builder()
//                .message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity(AppConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    //handleMethodArgumentNotValidException

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidExeception(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        Map<String, Object> response = new HashMap<>();

        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field, message);

        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    //handel badApiRequest
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> badApiRequestHandler(BadApiRequestException ex){
        logger.info("Bad Api request");

        ApiResponseMessage response=ApiResponseMessage
                .builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


}
