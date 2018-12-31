package com.coral.injector.agent.adders.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.coral.injector.agent.globalApi.AddMethod;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class LoadClassAdders {
    ClassPool cp;

    public LoadClassAdders() {
        cp = ClassPool.getDefault();
    }

    //TODO: Make this on its own thread
    public void load() throws NotFoundException, CannotCompileException, IllegalArgumentException,
            IllegalAccessException, ClassNotFoundException, InstantiationException {

        //loop through all registered transformers
        System.out.println(AdderRegistry.getInstance().registry);
        for (Class<? extends Object> c : AdderRegistry.getInstance().registry) {
            AddClass addAnnotation = c.getAnnotation(AddClass.class);
            String className = c.getSimpleName();
            String packageName = addAnnotation.inPackage();

            CtClass cc;

            if (addAnnotation.extendsClass() != "")
                cc = cp.makeClass(packageName + className, cp.getOrNull(addAnnotation.extendsClass()));
            else
                cc = cp.makeClass(packageName + className);

            cc.defrost();

            //get an instance of the transformer
            Object instance = c.newInstance();

            System.out.println("Adding class: " + className);

            for (Field f : c.getFields()) {
                for (Annotation a : f.getAnnotations()) {
                    if (a instanceof AddMethod) {
                        AddMethod addMethodAnnotation = (AddMethod) a;

                        ArrayList<CtClass> params = new ArrayList<>();
                        for (Class<?> type : addMethodAnnotation.params()) {
                            params.add(cp.getOrNull(type.getName()));
                        }

                        cc.defrost();
                        CtMethod m = new CtMethod(cp.getOrNull(f.getType().getName()), f.getName(),
                                (CtClass[]) params.toArray(), cc);

                        m.setBody((String) f.get(instance));

                        cc.addMethod(m);
                    } else if (a instanceof OverrideMethod) {
                        if (addAnnotation.extendsClass() == "" && addAnnotation.implementsClass() == "")
                            continue;

                        OverrideMethod overrideAnnotation = (OverrideMethod) a;

                        CtClass superClass = cp.getOrNull(addAnnotation.extendsClass());

                        cc.defrost();

                        CtMethod m = CtNewMethod.delegator(superClass.getMethod(f.getName(), overrideAnnotation.sig()),
                                cc);

                        m.setBody("{super." + f.getName() + "();" + (String) f.get(instance) + "}");
                        cc.addMethod(m);

                    } else {
                        continue;
                    }
                }
            }
            cc.freeze();

        }
    }
}