package com.hsx.bo.advice;

import com.hsx.common.model.error.ApiError;
import com.hsx.common.model.error.ErrorSource;
import com.hsx.common.model.error.Status;
import com.hsx.common.model.response.HSXApiResponse;
import com.hsx.common.model.util.NetworkUtils;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HSXApiResponse pack = null;
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError objectError :
                ex.getBindingResult().getAllErrors()) {
            errorMessages.add(objectError.getDefaultMessage());
        }

        pack = new HSXApiResponse(Status.ERROR, String.join(",", errorMessages), HttpStatus.BAD_REQUEST, ErrorSource.CLIENT_ERROR, ApiError.API_VALIDATION_ERROR);
        return NetworkUtils.wrap(pack);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> exceptionHandler(ValidationException ex) {
        HSXApiResponse<Resource> pack = null;
        List<String> errorMessages = new ArrayList<>();

        errorMessages.add(ex.getMessage());

        pack = new HSXApiResponse(Status.ERROR, String.join(",", errorMessages), HttpStatus.BAD_REQUEST, ErrorSource.CLIENT_ERROR, ApiError.API_VALIDATION_ERROR);
        return NetworkUtils.wrap(pack);
    }
}