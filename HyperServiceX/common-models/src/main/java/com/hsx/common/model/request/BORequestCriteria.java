/*
 Author Name: Shaik Mahaaboob Basha
 File Name: BOOperations.java
 Description:
 Date: 28/04/2020
 */
package com.hsx.common.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BORequestCriteria implements Serializable {
    private String boJobId;
}
