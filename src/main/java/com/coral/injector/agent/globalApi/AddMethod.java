package com.coral.injector.agent.globalApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AddMethod {
    public Class<?>[] params();
}