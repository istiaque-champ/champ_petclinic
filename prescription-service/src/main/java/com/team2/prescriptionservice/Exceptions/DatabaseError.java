package com.team2.prescriptionservice.Exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class DatabaseError extends RuntimeException {
//    public DatabaseError(){
//    }

    public DatabaseError(String message){
        super(message);
    }

//    public DatabaseError(Throwable cause){
//        super(cause);
//    }
//
//    public DatabaseError(String message, Throwable cause){
//        super(message, cause);
//    }
}