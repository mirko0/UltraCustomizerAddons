package com.github.mirko0.ucaddtions.rtfile.readtxtlocal;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.dependencies.commons.io.FileUtils;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class ReadTextFileLines extends Element {
    public ReadTextFileLines(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Read local txt file line";
    }

    @Override
    public String getInternalName() {
        return "read-local-txt-file-line";
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
        return new String[]{"This element will read a", "line from local txt file"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("path","Path", DataType.STRING, elementInfo),
                new Argument("line", "Lines", DataType.NUMBER, elementInfo)
        };
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
        Object lineo = this.getArguments(elementInfo)[1].getValue(scriptInstance);
        if (lineo != null) {

            long line = (long) lineo;
            if (line == 0) line = 1;
            else line = line - 1;

            int finalLine = (int) line;
            Bukkit.getScheduler().runTaskAsynchronously(plugin.getBootstrap(), () -> {

                try {
                    List<String> fileTxt = FileUtils.readLines(new File(path), Charset.defaultCharset());
                    String fileTxtString = fileTxt.get(finalLine);
                    this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                        @Override
                        public Object request() {
                            return fileTxtString.replace("\r\n", "\n").replace("\r", "\n");
                        }
                    });
                    this.getConnectors(elementInfo)[0].run(scriptInstance);
                } catch (IndexOutOfBoundsException e) {
                    this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                        @Override
                        public Object request() {
                            return "null";
                        }
                    });
                    this.getConnectors(elementInfo)[1].run(scriptInstance);
                }
            });

        }else {
            plugin.getBootstrap().getLogger().warning("ReadTxtFile - line argument must not be null!");
            this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                @Override
                public Object request() {
                    return "null";
                }
            });
            this.getConnectors(elementInfo)[1].run(scriptInstance);
        }
    }
}