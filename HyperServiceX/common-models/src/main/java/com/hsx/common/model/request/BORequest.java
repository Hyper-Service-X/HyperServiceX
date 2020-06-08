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
public class BORequest<T extends BORequestCriteria> implements HSXOperation, Serializable {

    private static final long serialVersionUID = -269795952812364180L;
    @JsonProperty
    private Header header;
    @JsonProperty
    private T requestData;
    @JsonProperty
    private HashMap<String, String> params;
}
