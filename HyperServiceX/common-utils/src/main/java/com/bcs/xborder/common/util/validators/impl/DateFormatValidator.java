package com.bcs.xborder.common.util.validators.impl;

import com.bcs.xborder.common.util.validators.Validator;
import com.hsx.common.model.exception.HSXException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@AllArgsConstructor
public class DateFormatValidator implements Validator {

    private String date;
    @NonNull
    private String supportFormat;

    private boolean mandatory;
    @NonNull
    private HSXException exception;

    @Override
    public void validate() throws HSXException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(supportFormat);
        try {
            if (mandatory || !StringUtils.isEmpty(date))
                dateFormat.parse(date);
        } catch (ParseException e) {
            throw exception;
        }
    }

}
