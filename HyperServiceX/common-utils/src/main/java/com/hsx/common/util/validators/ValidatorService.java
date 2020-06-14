package com.hsx.common.util.validators;


import com.hsx.common.model.exception.HSXException;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {

    public void validate(Validator... validators) throws HSXException {
        if (validators != null) {
            for (Validator validator : validators) {
                validator.validate();
            }
        }
    }
}
