package com.petclinic.billing.http;

import com.petclinic.billing.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RestControllerAdvice
public class BillControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(BillControllerExceptionHandler.class);
    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, Exception ex){
        return createHttpErrorInfo(request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(ServerHttpRequest request, Exception ex) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        LOG.debug("Returning HTTP status: {} for path: {}, message: {}", HttpStatus.UNPROCESSABLE_ENTITY, path, message);

        return new HttpErrorInfo(path, HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
