package com.team2.prescriptionservice.Exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsUnitTests {
    @Test
    void testHttpErrorInfoConstructor() {
        HttpErrorInfo httpErrorInfo = new HttpErrorInfo(HttpStatus.NOT_FOUND,"path",  "Not Found");

        assertEquals(httpErrorInfo.getHttpStatus(),  HttpStatus.NOT_FOUND);
        assertEquals(httpErrorInfo.getPath(), "path");
        assertNotNull(httpErrorInfo.getTimestamp());
        assertEquals(httpErrorInfo.getMessage(), "Not Found");
    }

    @Test
    void testNullHttpErrorInfoConstructor() {
        HttpErrorInfo httpErrorInfo = new HttpErrorInfo();

        assertNull(httpErrorInfo.getHttpStatus());
        assertNull(httpErrorInfo.getPath());
        assertNotNull(httpErrorInfo.getTimestamp());
        assertNull(httpErrorInfo.getMessage());
    }

    @Test
    void testCreateHttpErrorInfo() {

        GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();
        assertNotNull(handler);


    }
}