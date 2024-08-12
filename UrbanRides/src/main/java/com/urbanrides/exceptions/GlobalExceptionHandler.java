package com.urbanrides.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.UnexpectedTypeException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        System.out.println("Global Exception");
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        System.out.println("Gccccclobal Exception");

        return errorResponse;
    }
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<Map<String, String>> handleCustomValidationException(CustomValidationException ex) {
        System.out.println("Global Excddddeption");

        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Map<String, String>> unexpectedTypeException(UnexpectedTypeException ex) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("errors", "Data invalid. Please provide valid data");
        System.out.println("Global dfdfdfException");

        return new ResponseEntity<>(responseMap, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

//    //404 not found
//    @ExceptionHandler({ NoHandlerFoundException.class })
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handle404() {
//        return "errorPages/404Error";
//    }
//
//    //500 error code
//    @ExceptionHandler({ Exception.class })
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleInternalServerError() {
//        return "error/500Error";
//    }































//
//
//
//
//
//    //	Handel the particular error number (status code 500)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handelInternalServerError() {
//        return "admin/index";
//    }
//
//
//    //class not found
//    @ExceptionHandler({ClassNotFoundException.class})
//    public String classNotFoundExceptionHandler() {
//        return "admin/classNotFound";
//    }
//
//
//    //    SQLException
//    @ExceptionHandler({SQLException.class})
//    public String sqlExceptionHandler() {
//        return "admin/sqlError";
//    }
//
//
//    //    FileNotFoundException
//    @ExceptionHandler({FileNotFoundException.class})
//    public String fileNotFoundExceptionHandler() {
//        return "admin/fileNotFound";
//    }
//
//
//    //    ArrayIndexOutOfBoundsException
//    @ExceptionHandler({ArrayIndexOutOfBoundsException.class})
//    public String arrayIndexOutOfBoundsExceptionHandler() {
//        return "admin/arrayIndexOutOfBounds";
//    }
//
//
//    //    ClassCastException
//    @ExceptionHandler({ClassCastException.class})
//    public String classCastExceptionHandler() {
//        return "admin/classCastError";
//    }
//
//
//    //    NoSuchMethodException
//    @ExceptionHandler({NoSuchMethodException.class})
//    public String noSuchMethodExceptionHandler() {
//        return "admin/noSuchMethod";
//    }
//
//
//    //    IllegalAccessException
//    @ExceptionHandler({IllegalAccessException.class})
//    public String illegalAccessExceptionHandler() {
//        return "admin/illegalAccess";
//    }
//
//
//    //    InstantiationException
//    @ExceptionHandler({InstantiationException.class})
//    public String instantiationExceptionHandler() {
//        return "admin/instantiationError";
//    }
//
//
//    //    NoSuchFieldException
//    @ExceptionHandler({NoSuchFieldException.class})
//    public String noSuchFieldExceptionHandler() {
//        return "admin/noSuchField";
//    }


}