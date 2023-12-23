package com.github.mirko0.ucaddtions.rtfile.readtxtlocal;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.dependencies.commons.io.FileUtils;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.charset.Charset;

public class ReadTextFile extends Element {
    public ReadTextFile(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Read local txt file";
    }

    @Override
    public String getInternalName() {
        return "read-local-txt-file";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.WRITTEN_BOOK;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"This element will read a local txt file"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{new Argument("path","Path", DataType.STRING, elementInfo)};
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{new OutcomingVariable("text","Text",DataType.STRING,elementInfo)};
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] {new Child(elementInfo, "true") {
            @Override
            public String getName() {
                return "Reading successful";
            }

            @Override
            public String[] getDescription() {
                return new String[] { "Will be executed if the server", "successfully read the file." };
            }

            @Override
            public XMaterial getIcon() {
                return XMaterial.LIME_STAINED_GLASS_PANE;
            }
        }, new Child(elementInfo, "false") {
            @Override
            public String getName() {
                return "Failed to read file";
            }

            @Override
            public String[] getDescription() {
                return new String[] { "Will be executed if the server", "did not read the file" };
            }

            @Override
            public XMaterial getIcon() {
                return XMaterial.RED_STAINED_GLASS_PANE;
            }
        }};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        final String path = (String) this.getArguments(elementInfo)[0].getValue(scriptInstance);

        Bukkit.getScheduler().runTaskAsynchronously(plugin.getBootstrap(), () ->{
            String fileTxt = FileUtils.readFileToString(new File(path), Charset.defaultCharset());

            try {
                this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    @Override
                    public Object request() {
                        return fileTxt;
                    }
                });
                this.getConnectors(elementInfo)[0].run(scriptInstance);

            }catch (Exception e){
                this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    @Override
                    public Object request() {
                        return "Failed to read txt file";
                    }
                });

                this.getConnectors(elementInfo)[1].run(scriptInstance);
            }
        });
    }
}
