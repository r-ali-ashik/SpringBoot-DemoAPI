package com.aliashik.advice;

import com.aliashik.exception.EmployeeNotFoundException;
import org.json.simple.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EmployeeNotFoundException.class, RuntimeException.class})
    protected ResponseEntity<JSONObject> handleApiException(Exception ex, WebRequest request) {

        JSONObject responseJson = new JSONObject();
        if(ex instanceof EmployeeNotFoundException || ex instanceof DataAccessException) {
            responseJson.put("status", HttpStatus.NOT_FOUND);
            responseJson.put("message", ex.getMessage());
        }else {
            responseJson.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            responseJson.put("message", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<JSONObject>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<JSONObject>(responseJson, HttpStatus.NOT_FOUND);
    }
}