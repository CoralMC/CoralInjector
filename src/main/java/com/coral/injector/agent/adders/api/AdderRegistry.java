package com.coral.injector.agent.adders.api;

import java.util.ArrayList;
import java.util.Arrays;

public class AdderRegistry {

    public static AdderRegistry instance = null;

    public ArrayList<Class<? extends Object>> registry = new ArrayList<>();

    public void register(Object o) {
        Class<? extends Object> c = o.getClass();
        System.out.println("class:" + c);
        System.out.println(Arrays.asList(c.getAnnotations()));
        if (c.getAnnotation(AddClass.class) == null) {
            return;
        }
        registry.add(c);
    }

    public static AdderRegistry getInstance() {
        return (instance == null) ? instance = new AdderRegistry() : instance;
    }

}