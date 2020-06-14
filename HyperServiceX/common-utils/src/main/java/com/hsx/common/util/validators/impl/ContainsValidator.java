package com.hsx.common.util.validators.impl;

import com.hsx.common.util.validators.Validator;
import com.hsx.common.model.exception.HSXException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ContainsValidator<T> implements Validator {

    @NonNull
    private T value1;

    @NonNull
    private List<T> supportList;

    @NonNull
    private HSXException e;

    @Override
    public void validate() throws HSXException {
        if (!supportList.contains(value1)) {
            throw e;
        }
    }
}
