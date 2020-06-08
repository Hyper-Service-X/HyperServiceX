package com.hsx.common.model.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hsx.common.model.error.HSXErrorInfo;
import com.hsx.common.model.request.Header;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HSXMessage<T> {

    @JsonProperty
    private T data;
    @JsonProperty
    private Header header;
    @JsonProperty
    private HSXErrorInfo hsxErrorInfo;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HSXErrorInfo getHsxErrorInfo() {
        return hsxErrorInfo;
    }

    public void setHsxErrorInfo(HSXErrorInfo hsxErrorInfo) {
        this.hsxErrorInfo = hsxErrorInfo;
    }

    @JsonIgnore
    public boolean isSuccess() {
        if (hsxErrorInfo == null) {
            return true;
        } else if (hsxErrorInfo._isSuccess()) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isWarning() {
        if (hsxErrorInfo == null) {
            return false;
        } else if (hsxErrorInfo._isWarning()) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isError() {
        if (hsxErrorInfo == null) {
            return false;
        } else if (hsxErrorInfo._isError()) {
            return true;
        }
        return false;
    }
}
