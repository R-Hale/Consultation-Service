package com.heliosx.consultation_service.exceptions;

public class InvalidConditionException extends RuntimeException {
    public InvalidConditionException (String message) {
        super(message);
    }
}
