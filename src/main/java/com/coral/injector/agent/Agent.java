package com.coral.injector.agent;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.HashMap;

import com.coral.injector.agent.transformers.GuiMainMenuTransformer;
import com.coral.injector.agent.transformers.api.LoadClassTransformers;
import com.coral.injector.agent.transformers.api.TransformerRegistry;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LineNumberAttribute;

public class Agent {
    public static void agentmain(String args, Instrumentation i) {
        System.out.println("Agent Main");

        TransformerRegistry.getInstance().register(new GuiMainMenuTransformer());

        System.out.println(TransformerRegistry.getInstance().registry);

        LoadClassTransformers lct = new LoadClassTransformers();
        HashMap<String, CtClass> classes = null;

        try {
            classes = lct.load();
        } catch (IllegalArgumentException | IllegalAccessException | NotFoundException | CannotCompileException
                | ClassNotFoundException | InstantiationException e1) {
            e1.printStackTrace();
        }

        ClassDefinition definition;
        try {
            for (String s : classes.keySet()) {
                definition = new ClassDefinition(Class.forName(s), classes.get(s).toBytecode());
                i.redefineClasses(definition);
            }
        } catch (ClassNotFoundException | IOException | CannotCompileException | UnmodifiableClassException e) {
            e.printStackTrace();
        }

    }

    public static void replaceLine(int lineToReplace, String replaceWith, CtMethod m) throws CannotCompileException {
        // Access the code attribute
        CodeAttribute codeAttribute = m.getMethodInfo().getCodeAttribute();

        // Access the LineNumberAttribute
        LineNumberAttribute lineNumberAttribute = (LineNumberAttribute) codeAttribute
                .getAttribute(LineNumberAttribute.tag);

        // Index in bytecode array where the instruction starts
        int startPc = lineNumberAttribute.toStartPc(lineToReplace);

        // Index in the bytecode array where the following instruction starts
        int endPc = lineNumberAttribute.toStartPc(lineToReplace + 1);

        System.out.println("Modifying from " + startPc + " to " + endPc);

        // Let's now get the bytecode array
        byte[] code = codeAttribute.getCode();
        for (int i = startPc; i < endPc; i++) {
            // change byte to a no operation code
            code[i] = CodeAttribute.NOP;
        }

        System.out.println(replaceWith);

        m.insertAt(lineToReplace, replaceWith);
    }
}