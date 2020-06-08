package com.hsx.common.model.util;


import com.hsx.common.model.error.ApiError;
import com.hsx.common.model.error.Error;
import com.hsx.common.model.error.HSXErrorInfo;
import com.hsx.common.model.error.ServiceError;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.response.HSXResponseWrapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nadith
 */
public class ErrorUtil {


    private static final String API_ERROR_CODE = "API Error Code : ";
    private static final String SERVICE_ERROR_CODE = "SERVICE API Error Code : ";
    private static final String ERROR_CODE_SEPARATOR = " - ";


    /**
     * Following method will Create a ErrorsType object with following 2 attributes.
     *
     * @param HSXErrorInfo The ErrorInfo object which contain error information
     * @Code -  4 1 1230 0000 1111
     * 4						          1						        1230				    0000                   1111
     * |                                 |                                |                        |                     |
     * Client Side Error                API Layer                     API Error Code   Service Error Code     Reason Code
     * Server Side Error                Service Layer
     */
    public static HSXResponseWrapper fillErrorInfo(HSXErrorInfo HSXErrorInfo, HSXResponseWrapper responseWrapper) {
        responseWrapper.setStatusCode(HSXErrorInfo.getStatus().code);

        if (HSXErrorInfo._isSuccess()) {
            setStatusMessage(HSXErrorInfo, responseWrapper);
            return responseWrapper;
        } else if (HSXErrorInfo._isWarning()) {
            setStatusMessage(HSXErrorInfo, responseWrapper);
            if (HSXErrorInfo.getErrorList() != null && HSXErrorInfo.getErrorList().size() > 0) {
                if (responseWrapper.getErrors() == null) {
                    Error error = new Error();
                    responseWrapper.setError(error);
                }
                responseWrapper.getErrors().addAll(HSXErrorInfo.getErrorList());
            }
        } else if (HSXErrorInfo._isTimeOut()) {
            setStatusMessage(HSXErrorInfo, responseWrapper);
            if (HSXErrorInfo.getErrorList() != null && HSXErrorInfo.getErrorList().size() > 0) {
                if (responseWrapper.getErrors() == null) {
                    Error error = new Error();
                    responseWrapper.setError(error);
                }
                responseWrapper.getErrors().addAll(HSXErrorInfo.getErrorList());
            }
        }
        if (HSXErrorInfo.getErrorLayer() == null || HSXErrorInfo.getErrorSource() == null) {
            // No further processing is required
            return responseWrapper;
        } else {
            mapErrorCode(HSXErrorInfo, responseWrapper);
            return responseWrapper;
        }


    }

    private static void setStatusMessage(HSXErrorInfo HSXErrorInfo, HSXResponseWrapper responseWrapper) {
        if (!StringUtils.isEmpty(HSXErrorInfo.getShortText())) {
            responseWrapper.setStatusMessage(HSXErrorInfo.getShortText());
        }
    }

    private static void mapErrorCode(HSXErrorInfo hsxErrorInfo, HSXResponseWrapper responseWrapper) {
        long errorCode;
        errorCode = hsxErrorInfo.getErrorSource().code;
        errorCode = (errorCode * 10) + hsxErrorInfo.getErrorLayer().code;

        //here we can handle if both APIError is null.derived it by exception type
        if (hsxErrorInfo.getApiError() == null && hsxErrorInfo.getXBSServiceError() == null) {

            if (hsxErrorInfo.getException() != null) {
                Exception ex = hsxErrorInfo.getException();
                ex.printStackTrace();
                //todo can handle other exception

                //defaulting to 000
                hsxErrorInfo.setApiError(ApiError.NO_ERROR);
                if (ex instanceof HSXException) {
                    hsxErrorInfo.setReasonCode(((HSXException) ex).getReasonCode());
                }

            }
        }

        errorCode = (errorCode * 10000) + (hsxErrorInfo.getApiError() == null ? 0 : hsxErrorInfo.getApiError().code);
        errorCode = (errorCode * 10000) + (hsxErrorInfo.getXBSServiceError() == null ? 0 : hsxErrorInfo.getXBSServiceError().code);
        if (hsxErrorInfo.getReasonCode() != null && "\\d+".matches(hsxErrorInfo.getReasonCode())) {
            errorCode = (errorCode * 10000) + Long.valueOf(hsxErrorInfo.getReasonCode().substring(0, 4));
        } else if (hsxErrorInfo.getReasonCode() == null) {
            errorCode = (errorCode * 10000) + 0;
        } else {
            errorCode = (errorCode * 10000) + 9999;
        }

        List<String> errors = new ArrayList<>(2);
        if (hsxErrorInfo.getApiError() != null && hsxErrorInfo.getApiError().code > 0) {

            addErrorDescription(hsxErrorInfo.getApiError(), errors);
            if (StringUtils.isEmpty(responseWrapper.getErrorDetails())) {
                responseWrapper.setErrorDetails(hsxErrorInfo.getApiError().message);
            }

        }
        if (hsxErrorInfo.getXBSServiceError() != null && hsxErrorInfo.getXBSServiceError().code > 0) {

            addErrorDescription(hsxErrorInfo.getXBSServiceError(), errors);

        }
        responseWrapper.setErrors(errors);
        responseWrapper.setErrorCode(errorCode);
        responseWrapper.setErrorMessage(hsxErrorInfo.getShortText());

        if (hsxErrorInfo.getErrorList() != null && hsxErrorInfo.getErrorList().size() > 0) {
            responseWrapper.getErrors().addAll(hsxErrorInfo.getErrorList());
        }
    }

    public static String mapErrorCode(HSXErrorInfo hsxErrorInfo) {
        long errorCode = 0;
        if (hsxErrorInfo != null) {
            errorCode = hsxErrorInfo.getErrorSource().code;
            errorCode = (errorCode * 10) + hsxErrorInfo.getErrorLayer().code;

            //here we can handle if both APIError is null.derived it by exception type
            if (hsxErrorInfo.getApiError() == null && hsxErrorInfo.getXBSServiceError() == null) {

                if (hsxErrorInfo.getException() != null) {
                    Exception ex = hsxErrorInfo.getException();
                    ex.printStackTrace();
                    //todo can handle other exception

                    //defaulting to 000
                    hsxErrorInfo.setApiError(ApiError.NO_ERROR);
                    if (ex instanceof HSXException) {
                        hsxErrorInfo.setReasonCode(((HSXException) ex).getReasonCode());
                    }

                }
            }

            errorCode = (errorCode * 10000) + (hsxErrorInfo.getApiError() == null ? 0 : hsxErrorInfo.getApiError().code);
            errorCode = (errorCode * 10000) + (hsxErrorInfo.getXBSServiceError() == null ? 0 : hsxErrorInfo.getXBSServiceError().code);
            if (hsxErrorInfo.getReasonCode() != null && "\\d+".matches(hsxErrorInfo.getReasonCode())) {
                errorCode = (errorCode * 10000) + Long.valueOf(hsxErrorInfo.getReasonCode().substring(0, 4));
            } else if (hsxErrorInfo.getReasonCode() == null) {
                errorCode = (errorCode * 10000) + 0;
            } else {
                errorCode = (errorCode * 10000) + 9999;
            }
        }
        return String.valueOf(errorCode);

    }

    private static void addErrorDescription(ApiError errorCode, List<String> errors) {

        errors.add(new StringBuilder(API_ERROR_CODE).append(errorCode.code).append(ERROR_CODE_SEPARATOR).append(errorCode.message).toString());
    }

    private static void addErrorDescription(ServiceError errorCode, List<String> errors) {

        errors.add(new StringBuilder(SERVICE_ERROR_CODE).append(errorCode.code).append(ERROR_CODE_SEPARATOR).append(errorCode.message).toString());
    }


}
