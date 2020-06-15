package com.hsx.sa.web.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


@Configuration
//@EnableSwagger2
@Profile({"dev","sit"})
public class SwaggerConfig {


    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ArrayList<ResponseMessage> getResponseMessages() {
        ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(400).message("Bad Request, Syntax or validation error").responseModel(new ModelRef("String")).build());
        responseMessages.add(new ResponseMessageBuilder().code(401).message("Unauthorized").responseModel(new ModelRef("String")).build());
        responseMessages.add(new ResponseMessageBuilder().code(403).message("Forbidden").responseModel(new ModelRef("String")).build());
        responseMessages.add(new ResponseMessageBuilder().code(404).message("Not Found").responseModel(new ModelRef("String")).build());
        responseMessages.add(new ResponseMessageBuilder().code(405).message("Method Not Allowed").responseModel(new ModelRef("String")).build());
        responseMessages.add(new ResponseMessageBuilder().code(409).message("Conflict error").responseModel(new ModelRef("String")).build());
        responseMessages.add(new ResponseMessageBuilder().code(500).message("Server error").responseModel(new ModelRef("String")).build());

        return responseMessages;
    }

    private ArrayList<Parameter> getOpernlParameters() {
        ArrayList<Parameter> paramList = new ArrayList<>();
        paramList.add(new ParameterBuilder().name("api-key")
                .description("API Key issued to the overseas Gateway")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true).build());
        paramList.add(new ParameterBuilder().name("x-bcs-instruction-id")
                .description("Value of instruction Id. (Set by host and required send back in Leg2/Leg4)")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());
        return paramList;
    }
}
