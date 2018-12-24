package com.coral.injector.agent.transformers.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AddInMethod {
    public int line();

    public String methodName();
}