package com.aliashik.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException() {
        super("Employee Not Found");
    }
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmployeeNotFoundException(String message) {
        super(message);
    }
    public EmployeeNotFoundException(Throwable cause) {
        super(cause);
    }
}
