package com.hsx.common.util.validators.impl;

import com.hsx.common.util.validators.Validator;
import com.hsx.common.model.exception.HSXException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
This file is created by Nadith
on 15/4/2020-12:03 AM     
*/
@RequiredArgsConstructor
public class DateTimeFormatSupportValidator implements Validator {

    @NonNull
    private String actualFormat;

    @NonNull
    private String supportedFormat;

    @NonNull
    private HSXException e;

    @Override
    public void validate() throws HSXException {
        try {
            LocalDateTime.parse(actualFormat, DateTimeFormatter.ofPattern(supportedFormat));
        } catch (Exception ex) {
            throw e;
        }
    }
}
