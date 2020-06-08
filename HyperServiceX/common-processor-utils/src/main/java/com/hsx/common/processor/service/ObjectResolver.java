package com.hsx.common.processor.service;

import com.hsx.common.model.request.HSXOperation;

import java.io.IOException;

public interface ObjectResolver<I, O extends HSXOperation> {

    O resolve(I msg) throws IOException;

    String convert(Object payload) throws IOException;

    String getName();
}
