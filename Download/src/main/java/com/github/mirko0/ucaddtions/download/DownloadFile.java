package com.github.mirko0.ucaddtions.download;


import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.dependencies.commons.io.FileUtils;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DownloadFile extends Element {
    public DownloadFile(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Download File";
    }

    @Override
    public String getInternalName() {
        return "down-file-from-url";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.STICKY_PISTON;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "This allows you to download a file","from url to specific path."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("url", "URL", DataType.STRING, elementInfo),
                new Argument("path", "path", DataType.STRING, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{new OutcomingVariable("completed", "Completed", DataType.BOOLEAN, elementInfo)};
    }

    @Override
    public Child[] getConnectors(final ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    @Override
    public void run(ElementInfo info, ScriptInstance instance) {
        final String urlS = (String) this.getArguments(info)[0].getValue(instance);
        final String path = (String) this.getArguments(info)[1].getValue(instance);

        try {
            FileUtils.copyURLToFile(
                    new URL(urlS),
                    new File(path), 30000, 30000);
            this.getOutcomingVariables(info)[0].register(instance, new DataRequester() {
                @Override
                public Object request() {
                    return true;
                }
            });
            this.getConnectors(info)[0].run(instance);
        } catch (IOException e) {
            this.getOutcomingVariables(info)[0].register(instance, new DataRequester() {
                @Override
                public Object request() {
                    return false;
                }
            });
            this.getConnectors(info)[0].run(instance);
            e.printStackTrace();
        }
    }
}