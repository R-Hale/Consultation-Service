package com.heliosx.consultation_service.exceptions;

import com.heliosx.consultation_service.model.dto.response.ResponseError;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({InvalidConditionException.class, MissingQuestionAnswerException.class})
    protected ResponseEntity<ResponseError> InvalidConditionException(RuntimeException exception){
        log.error("Exception while handling request", exception);
        ResponseError error = new ResponseError(exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(error);
    }
}
