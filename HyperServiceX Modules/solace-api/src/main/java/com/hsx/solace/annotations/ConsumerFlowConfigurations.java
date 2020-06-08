package com.hsx.solace.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.hsx.solace.annotations.Constants.NULL;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConsumerFlowConfigurations {

    public AckMode ackMode() default AckMode.NULL;

    public int ackThreshold() default 0;

    public int ackTimeMs() default 0;

    // Flow end point configurations
    //public EndPointConfigurations endPointConfigurations();

    public String sqlSelector() default NULL;

    public int windowSize() default 0;

    public int windowedAckMaxSize() default 255;

    public boolean activeFlowIndication() default false;

    public boolean noLocal() default false;

    public boolean startState() default false;

    public boolean segmentFlow() default false;
}
