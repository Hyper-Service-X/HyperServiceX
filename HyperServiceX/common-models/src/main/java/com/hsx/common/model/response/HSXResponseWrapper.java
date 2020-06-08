package com.hsx.common.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hsx.common.model.error.Error;
import com.hsx.common.model.error.HSXErrorInfo;
import com.hsx.common.model.util.ErrorUtil;
import org.springframework.hateoas.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"transactionId", "status", "error", "data", "requestedTime", "version"})
public class HSXResponseWrapper<T> {
    @JsonProperty("version")
    private String version;
    @JsonProperty("status")
    private Status status;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("error")
    private Error error;
    @JsonProperty("data")
    private List<T> data = null;


    /**
     * 1
     */
    public static final int SUCCESS = 1;
    /**
     * -1
     */
    public static final int ERROR = -1;
    /**
     * -2
     */
    public static final int TIMEOUT = -2;
    /**
     * 0
     */
    public static final int PARTIAL_SUCCESS = 0;


    public HSXResponseWrapper(HSXApiResponse<T> response) {

        data = new ArrayList<>();
        if (response.getData() != null) {
            data.add(response.getData());
        }
        setVersion("v1.0");
        ErrorUtil.fillErrorInfo(response, this);
    }

    public HSXResponseWrapper(HSXApiResponse<T> response, Resources resources) {
        data = new ArrayList<>();

        if (resources.getContent() != null) {
            data = (List<T>) resources.getContent().stream().collect(Collectors.toList());
        }
        setVersion("v1.0");
        ErrorUtil.fillErrorInfo(response, this);
    }


    public HSXResponseWrapper(HSXErrorInfo HSXErrorInfo) {
        this();
        ErrorUtil.fillErrorInfo(HSXErrorInfo, this);
        setVersion("v1.0");
    }

    public HSXResponseWrapper() {
        setVersion("v1.0");
        status = new Status();
    }

    public HSXResponseWrapper(int statusCode) {
        this();
        this.setStatusCode(statusCode);

    }

    public HSXResponseWrapper(int statusCode, List<T> data) {
        this(statusCode);
        this.setData(data);

    }

    @SuppressWarnings("unchecked")
    public HSXResponseWrapper(int statusCode, T... data) {
        this(statusCode);
        this.setData(Arrays.asList(data));
    }

    public HSXResponseWrapper(int statusCode, long errorCode) {
        this(statusCode);
        setErrorCode(errorCode);
    }

    public HSXResponseWrapper(int statusCode, long errorCode, String errorMsg) {
        this(statusCode, errorCode);
        setErrorMessage(errorMsg);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String apiVersion) {
        this.version = apiVersion;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonIgnore
    public long getStatusCode() {
        return status.getCode();
    }

    @JsonIgnore
    public void setStatusMessage(String message) {
        if (this.status == null) {
            return;
        }
        status.setMessage(message);

    }

    public void setStatusCode(long code) {
        if (this.status == null) {
            status = new Status();
        }
        this.status.setCode(Math.toIntExact(code));
        if (code == SUCCESS) {
            this.status.setMessage("SUCCESS");
            this.error = null;
        } else if (code == PARTIAL_SUCCESS) {
            this.status.setMessage("PARTIAL_SUCCESS");
            this.error = null;
        } else if (code == ERROR) {
            this.status.setMessage("ERROR");
        } else if (code == TIMEOUT) {
            this.status.setMessage("TIMEOUT");
        } else {
            this.status.setMessage(null);
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @JsonIgnore
    public long getErrorCode() {
        return error == null ? 0 : error.getCode();
    }

    public void setErrorCode(long code) {
        _getError().setCode(code);
    }

    @JsonIgnore
    public String getErrorMessage() {
        return error == null ? null : error.getMessage();
    }

    public void setErrorMessage(String message) {
        _getError().setMessage(message);
    }

    @JsonIgnore
    public String getErrorDetails() {
        return error == null ? null : error.getDetails();
    }

    public void setErrorDetails(String details) {
        _getError().setDetails(details);
    }

    @JsonIgnore
    public List<String> getErrors() {
        return error == null ? null : error.getErrors();
    }

    public void setErrors(List<String> errors) {
        _getError().setErrors(errors);
    }

    class Status {
        private int code;
        private String message;

        public long getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private Error _getError() {
        if (error == null) {
            error = new Error();
        }
        return error;
    }
}
