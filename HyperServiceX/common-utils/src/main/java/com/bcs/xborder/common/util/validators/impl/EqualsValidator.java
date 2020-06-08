package com.bcs.xborder.common.util.validators.impl;

import com.bcs.xborder.common.util.validators.Validator;
import com.hsx.common.model.exception.HSXException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class EqualsValidator implements Validator {

    private Logger LOGGER = LoggerFactory.getLogger(EqualsValidator.class);

    @NonNull
    private Object value1;

    @NonNull
    private Object value2;

    @NonNull
    private HSXException e;

    @Override
    public void validate() throws HSXException {
        if (!isEqual(value1, value2)) {
            LOGGER.info("Validation Failed | Expected Value {} | Received Value {} ", value1, value2);
            throw e;
        }
    }

    public boolean isEqual(Object value1, Object value2) {
        if (value1 == value2) return true;
        if (value1 == null) return false;
        if (value1.getClass() != value2.getClass()) return false;

        return value1.equals(value2);
    }
}
