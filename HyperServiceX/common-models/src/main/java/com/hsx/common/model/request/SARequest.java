package com.hsx.common.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SARequest<T extends SARequestCriteria> implements HSXOperation, Serializable {

    private static final long serialVersionUID = -269795951812364180L;
    private Header header;
    @JsonProperty
    private T requestData;
    @JsonProperty
    private HashMap<String, String> params;
}
