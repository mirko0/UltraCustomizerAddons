package com.github.mirko0.vplus.elements;

import com.github.mirko0.vplus.specifications.TestO;
import com.github.mirko0.vplus.specifications.TestSpecification;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class ReceiveRandomDataType extends Element {
    public ReceiveRandomDataType(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "ReceiveRandomDataType";
    }

    @Override
    public String getInternalName() {
        return "test-receive-d-t";
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
        return new String[]{"TEST2"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("data", "Data", DataType.getCustomDataType("testdata"), elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("name", "Name", DataType.STRING, elementInfo),
                new OutcomingVariable("location", "Location", DataType.STRING, elementInfo),
                new OutcomingVariable("time", "Time", DataType.STRING, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }


    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        TestO value = (TestO) this.getArguments(elementInfo)[0].getValue(scriptInstance);

        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return value.getName();
            }
        });
        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return value.getLocation();
            }
        });
        this.getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return value.getTime();
            }
        });
        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
