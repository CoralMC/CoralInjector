package com.coral.injector.agent.adders;

import com.coral.injector.agent.adders.api.AddClass;
import com.coral.injector.agent.adders.api.OverrideMethod;

@AddClass(extendsClass = "cgj", inPackage = "")
public class ReflectiveGuiButton {

    @OverrideMethod(sig = "(DD)V")
    public String a = "System.out.println(\"Click!\");";

}