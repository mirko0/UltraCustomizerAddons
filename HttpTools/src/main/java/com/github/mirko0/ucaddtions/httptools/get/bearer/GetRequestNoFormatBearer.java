package com.github.mirko0.ucaddtions.httptools.get.bearer;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class GetRequestNoFormatBearer extends Element {
    public GetRequestNoFormatBearer(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "HTTP Get Raw (Bearer)";
    }

    @Override
    public String getInternalName() {
        return "http-get-request-raw-bearer";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.HOPPER_MINECART;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Sends a http get request to specified url with bearer token. (No new lines)"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{new Argument("url", "URL", DataType.STRING, elementInfo), new Argument("bearer", "Bearer", DataType.STRING, elementInfo)};
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{new OutcomingVariable("response", "Response", DataType.STRING, elementInfo)};
    }

    @Override
    public Child[] getConnectors(final ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    @Override
    public void run(ElementInfo info, ScriptInstance instance) {
        final String urlS = (String) this.getArguments(info)[0].getValue(instance);
        final String bearer = (String) this.getArguments(info)[1].getValue(instance);
        try {
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + bearer);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String output =  in.lines().collect(Collectors.joining());
            this.getOutcomingVariables(info)[0].register(instance, new DataRequester() {
                @Override
                public Object request() {
                    return output;
                }
            });
            this.getConnectors(info)[0].run(instance);

        } catch (IOException e) {
            e.printStackTrace();
            plugin.getBootstrap().getLogger().warning(ChatColor.RED + "Error with get request: " + e.getMessage() + "\nCause: " + e.getCause());
            this.getOutcomingVariables(info)[0].register(instance, new DataRequester() {
                @Override
                public Object request() {
                    return "Error with get request: " + e.getMessage() + "\nCause: " + e.getCause();
                }
            });
            this.getConnectors(info)[0].run(instance);
        }

    }


}