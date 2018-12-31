package com.coral.injector.agent.adders.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AddClass {

    public String inPackage() default "";

    public String extendsClass() default "";

    public String implementsClass() default "";

}