package com.hsx.bo.controller.health;

import com.hsx.bo.util.BackOfficeEndPointNamingUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Health Check", description = "API for Health Check", tags = {"Health Check Service"})
@RequestMapping(value = BackOfficeEndPointNamingUtil.VERSION + BackOfficeEndPointNamingUtil.HEALTH_CHECK, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthController {

    @RequestMapping(value = BackOfficeEndPointNamingUtil.HEALTH_CHECK_STATUS, method = RequestMethod.GET)
    @ApiOperation(value = "Health Check Response", notes = "This is Health Check end-point", responseContainer = "XBResponseWrapper")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success Response")})
    public String checkHealth() {
        return "ok";
    }
}
