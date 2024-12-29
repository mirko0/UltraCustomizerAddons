package com.github.mirko0.vplus.elements;

import com.github.mirko0.vplus.specifications.TestO;
import com.github.mirko0.vplus.specifications.TestSpecification;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GetRandomDataTypeData extends Element {
    public GetRandomDataTypeData(UltraCustomizer plugin) {
        super(plugin);
        DataType.registerCustomDataType("testdata", new TestSpecification());
    }

    @Override
    public String getName() {
        return "GetRandomDataType";
    }

    @Override
    public String getInternalName() {
        return "test-r-d-t";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BEACON;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"TEST1"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("data", "Data", elementInfo, DataType.getCustomDataType("testdata"))
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }


    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return new TestO("Mirko", "Serbia", "10:00");
            }
        });
        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
