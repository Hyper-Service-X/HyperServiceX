package com.hsx.sa.rest;

import com.hsx.common.model.exception.HSXIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import javax.ws.rs.NotFoundException;
import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/*
This file is created by Nadith
on 14/3/2020-11:36 AM     
*/
@Component("restTemplateResponseErrorHandler")
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == CLIENT_ERROR || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR ||
                httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            LOGGER.info(" Server error HttpStatus.Series {}", httpResponse.getStatusCode().series());
            throw new HSXIOException(httpResponse.getStatusCode());
        }
        throw new NotFoundException();
    }
}
