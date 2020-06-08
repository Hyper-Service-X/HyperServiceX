package com.hsx.bo.assembler;

import com.hsx.common.model.error.Status;
import com.hsx.common.model.response.HSXApiResponse;
import com.hsx.common.model.response.HSXMessage;


public class ResultAssembler {

    public static HSXApiResponse assemblingResources(HSXMessage result) {
        //toDO we can populate all other data in this assembling for the rest Response like meta-data,hateoas
        if (result.isSuccess()) {
            return new HSXApiResponse(result.getData());
        } else if (result.isWarning()) {
            return new HSXApiResponse(Status.PARTIAL_SUCCESS,result.getData());
        } else {
            return new HSXApiResponse(result.getHsxErrorInfo());
        }
    }
}