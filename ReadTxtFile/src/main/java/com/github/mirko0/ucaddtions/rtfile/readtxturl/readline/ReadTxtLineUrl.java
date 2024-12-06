package com.github.mirko0.ucaddtions.rtfile.readtxturl.readline;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtLineUrl extends Element {

    public ReadTxtLineUrl(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Read txt line (URL)";
    }

    @Override
    public String getInternalName() {
        return "read-txt-from-url-line";
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
        return new String[]{"This element will allow you to read", "txt line from file using url."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{new Argument("url", "URL", DataType.STRING, elementInfo), new Argument("line", "Line", DataType.NUMBER, elementInfo)};
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
                return new String[] { "Will be executed if the server", "successfully read the line." };
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
                return new String[] { "Will be executed if the server", "did not read the line" };
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
        long line = (long) this.getArguments(elementInfo)[1].getValue(scriptInstance);

        if (line == 0) line = 1;
        else line = line - 1;

        long finalLine = line;
        Bukkit.getScheduler().runTaskAsynchronously(plugin.getBootstrap(), ()-> {
            try {
                URL url = new URL(SURL);

                BufferedReader read = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String i;
                List<String> list = new ArrayList<>();
                while ((i = read.readLine()) != null)
                    list.add(i);
                read.close();

                String lineString;
                try {
                    lineString = list.get((int) finalLine);
                }catch (IndexOutOfBoundsException e){
                    lineString = "null";
                }
                String finalLineString = lineString;
                this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    @Override
                    public Object request() {
                        return finalLineString.replace("\r\n", "\n").replace("\r", "\n");
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
