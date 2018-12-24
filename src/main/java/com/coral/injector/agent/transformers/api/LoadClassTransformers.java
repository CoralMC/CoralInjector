package com.coral.injector.agent.transformers.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class LoadClassTransformers {

    //Default class pool
    ClassPool cp;

    public LoadClassTransformers() {
        cp = ClassPool.getDefault();
    }

    //TODO: Make this on its own thread
    public HashMap<String, CtClass> load() throws NotFoundException, CannotCompileException, IllegalArgumentException,
            IllegalAccessException, ClassNotFoundException, InstantiationException {

        HashMap<String, CtClass> toReturn = new HashMap<>();

        //loop through all registered transformers
        for (Class<? extends Object> c : TransformerRegistry.getInstance().registry) {
            ClassTransformer transformer = c.getAnnotation(ClassTransformer.class);
            String mappedName = transformer.mappedName();
            CtClass cc = cp.getOrNull(mappedName);

            //get an instance of the transformer
            Object instance = c.newInstance();

            System.out.println("Modding class: " + cc);

            for (Field f : c.getFields()) {
                for (Annotation a : f.getAnnotations()) {
                    if (a instanceof AddLocalVar) {
                        //get the values of fields and annotations
                        AddLocalVar alvAnnotation = (AddLocalVar) a;
                        String varName = f.getName();
                        String type = f.getType().getName();

                        //get the method in class
                        CtMethod cm = cc.getDeclaredMethod(alvAnnotation.methodName());
                        //add the local variable
                        cm.addLocalVariable(varName, cp.getCtClass(type));
                        //get the initial value
                        Object value = f.get(instance);
                        System.out.println(
                                "Adding local var statement: \n" + (varName + " = " + "(" + type + ")" + value) + ";");
                        //add the assignment statement at the beginning of the method
                        cm.insertBefore(varName + " = " + "(" + type + ")" + value + ";");
                    } else if (a instanceof AddInMethod) {
                        //get the values of fields and annotations
                        String body = (String) f.get(instance);
                        AddInMethod aimAnnotation = (AddInMethod) a;

                        //get the method in class
                        CtMethod cm = cc.getDeclaredMethod(aimAnnotation.methodName());
                        //add the code supplied to the bytecode line of the method
                        cm.insertAt(cm.getMethodInfo().getLineNumber(aimAnnotation.line()), body);
                    } else if (a instanceof AddMethod) {
                        //TODO: Do this later ig
                        continue;
                    } else {
                        continue;
                    }
                }
            }

            toReturn.put(mappedName, cc);
        }
        return toReturn;
    }
}