package com.hsx.solace.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EndPointConfigurations {

    public AccessType accessType() default AccessType.EXCLUSIVE; //"NONEXCLUSIVE"

    public Permission permission() default Permission.CONSUME; //"NONE", "READ_ONLY", "CONSUME", "MODIFY_TOPIC", "DELETE"

    public int maxMessageSize() default -1;

    public int quota() default -1;

    public boolean respectsMsgTTL() default true;

    public DiscardBehavior discardBehavior() default DiscardBehavior.NOTIFY_SENDER_OFF; //"NOTIFY_SENDER_ON", "NOTIFY_SENDER_OFF"

    public int maxMsgRedelivery() default -1;
}
