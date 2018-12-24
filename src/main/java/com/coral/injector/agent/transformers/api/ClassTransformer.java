package com.coral.injector.agent.transformers.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ClassTransformer {
    String mappedName();
}