package com.coral.injector.agent.transformers.api;

import java.util.ArrayList;
import java.util.Arrays;

public class TransformerRegistry {

    public static TransformerRegistry instance = null;

    public ArrayList<Class<? extends Object>> registry = new ArrayList<>();

    public void register(Object o) {
        Class<? extends Object> c = o.getClass();
        System.out.println("class:" + c);
        if (c.getAnnotation(ClassTransformer.class) == null) {
            return;
        }
        registry.add(c);
    }

    public static TransformerRegistry getInstance() {
        return (instance == null) ? instance = new TransformerRegistry() : instance;
    }

}