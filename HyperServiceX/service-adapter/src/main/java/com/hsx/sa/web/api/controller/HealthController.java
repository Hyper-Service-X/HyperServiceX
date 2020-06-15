package com.hsx.sa.web.api.controller;

import com.hsx.common.model.cache.HSXHealthCheckCache;
import com.hsx.common.model.constants.Constants;
import com.hsx.sa.web.api.utils.SAEndPointNamingUtil;
import com.hsx.sa.web.api.utils.SAURLParams;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@Api(value = "Health Controller", tags = "Health APIs -", description = "Health API")
@RequestMapping(SAEndPointNamingUtil.VERSION + SAEndPointNamingUtil.HEALTH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HealthController {

    @RequestMapping(value = SAEndPointNamingUtil.STATUS, method = RequestMethod.GET)
    @ApiOperation(value = "Health Check Response", notes = "This is Health Check end-point", response = ResponseEntity.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success Response")})
    public String checkHealth(@ApiParam(value = "Service Name", required = false) @RequestParam(value = SAURLParams.SERVICE_NAME) String serviceName) {
        String status = Constants.Common.NOT_OK;
        if (StringUtils.isEmpty(serviceName)) {
            if (HSXHealthCheckCache.INSTANCE.getStatus(Constants.Common.GLOBAL_STATUS) != null && HSXHealthCheckCache.INSTANCE.getStatus(Constants.Common.GLOBAL_STATUS)) {
                status = Constants.Common.OK;
            } else {
                status = Constants.Common.NOT_OK;
            }
        } else if (HSXHealthCheckCache.INSTANCE.getStatus(serviceName) != null && HSXHealthCheckCache.INSTANCE.getStatus(serviceName).equals(true)) {
            status = Constants.Common.OK;
        }
        return status;
    }

}
