package com.github.mirko0.loadbase;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;

public class TestElement extends Element {
    public TestElement(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
        AddonMain instance = AddonMain.instance;
    }

    @Override
    public String getName() {
        return "load-base-test";
    }

    @Override
    public String getInternalName() {
        return "l-b-test";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.FEATHER;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Used internally to test loadbase."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] {new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        String name = AddonMain.instance.getName();
        String author = AddonMain.instance.getAuthor();
        UltraCustomizer.getInstance().getBootstrap().getServer().broadcastMessage(name + " (" + author +") test element.");

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
