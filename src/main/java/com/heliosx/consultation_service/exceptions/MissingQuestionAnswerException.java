package com.heliosx.consultation_service.exceptions;

public class MissingQuestionAnswerException extends RuntimeException {
    public MissingQuestionAnswerException (String message) {
        super(message);
    }
}
