package com.hsx.common.util.validators.impl;

import com.hsx.common.util.validators.Validator;
import com.hsx.common.model.exception.HSXException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class OptionalEqualsValidator implements Validator {

    private static Logger LOGGER = LoggerFactory.getLogger(OptionalEqualsValidator.class);

    private Object actual;

    private Object supported;

    private HSXException e;

    @Override
    public void validate() throws HSXException {
        if (!isEqual(this.actual, this.supported)) {
            LOGGER.info("Validation Failed | Expected Value {} | Received Value {} ", this.supported, this.actual);
            throw e;
        }
    }

    public boolean isEqual(Object actual, Object supported) {
        if (actual == null) return true;
        if (actual.getClass() != supported.getClass()) return false;

        return actual.equals(supported);
    }
}
