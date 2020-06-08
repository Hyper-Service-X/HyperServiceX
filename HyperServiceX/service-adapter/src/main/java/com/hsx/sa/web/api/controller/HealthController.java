package com.hsx.sa.web.api.controller;

import com.hsx.sa.web.api.utils.MAEndPointNamingUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@Api(value = "Health Controller", tags = "Health APIs -", description = "Health API")
@RequestMapping(MAEndPointNamingUtil.VERSION + MAEndPointNamingUtil.HEALTH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HealthController {

    @RequestMapping(value = MAEndPointNamingUtil.STATUS, method = RequestMethod.GET)
    @ApiOperation(value = "Health Check Response", notes = "This is Health Check end-point", response = ResponseEntity.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success Response")})
    public String checkHealth() {
        return "OK";
    }
}
