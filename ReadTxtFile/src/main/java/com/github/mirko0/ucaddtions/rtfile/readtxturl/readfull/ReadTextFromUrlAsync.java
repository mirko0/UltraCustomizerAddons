package com.github.mirko0.ucaddtions.rtfile.readtxturl.readfull;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ReadTextFromUrlAsync extends Element {
    public ReadTextFromUrlAsync(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Read txt (URL) (Async)";
    }

    @Override
    public String getInternalName() {
        return "read-txt-from-url-async";
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
        return new String[]{"This element will allow you to read", "txt file from url asynchronously."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{new Argument("url", "URL", DataType.STRING, elementInfo)};
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
        final String SURL = (String) this.getArguments(elementInfo)[0].getValue(scriptInstance);
        Bukkit.getScheduler().runTaskAsynchronously(plugin.getBootstrap(), ()-> {
            try {
                URL url = new URL(SURL);

                BufferedReader read = new BufferedReader(
                        new InputStreamReader(url.openStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String i;

                while ((i = read.readLine()) != null)
                    stringBuilder.append(i).append("\n");
                read.close();

                this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    @Override
                    public Object request() {
                        return stringBuilder.toString();
                    }
                });
                this.getConnectors(elementInfo)[0].run(scriptInstance);


            } catch (IOException e) {
                this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    @Override
                    public Object request() {
                        return e.getLocalizedMessage();
                    }
                });
                this.getConnectors(elementInfo)[1].run(scriptInstance);
            }
        });
    }
}
