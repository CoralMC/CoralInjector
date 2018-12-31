package com.coral.injector.agent.transformers;

import com.coral.injector.agent.transformers.api.AddInMethod;
import com.coral.injector.agent.transformers.api.AddLocalVar;
import com.coral.injector.agent.transformers.api.ClassTransformer;

@ClassTransformer(mappedName = "cjx")
public class GuiMainMenuTransformer {

    @AddLocalVar(methodName = "a")
    public String str1 = "\"Coral Injector 0.1\"";

    @AddLocalVar(methodName = "a")
    public String str2 = "\"WARNING! Coral is in beta! Things may break!\"";

    //add code to methods

    @AddInMethod(methodName = "a", line = 519)
    public String addDrawString = "c(this.r,str1,2,n-21,-1);";

    @AddInMethod(methodName = "a", line = 519)
    public String addBetaDraw = "c(this.r, str2, (cfi.s().f.l()/2-this.r.a(str2))/2, 0,0xffff3333);";

    @AddInMethod(methodName = "c", line = 294)
    public String addReloadButton = "this.a(new ReflectiveGuiButton(50, this.m / 2 - 200, (this.m / 4 + 48) + 72 + 12, 49, 20, \"Reload\"));";

}