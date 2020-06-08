package com.hsx.solace.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SolaceGuaranteedMessageConsumer {
    public String name();

    public String[] topics();

    public SolaceSite[] sites() default SolaceSite.DEFAULT;

    public SolaceSite.Collections siteCollection() default SolaceSite.Collections.NONE;

    public String threadCount() default "1";

}
