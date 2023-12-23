package com.github.mirko0.ucaddtions.httptools.post;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class PostRequest extends Element {
    public PostRequest(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "HTTP Post";
    }

    @Override
    public String getInternalName() {
        return "http-post-request";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.PISTON;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Sends a http post request to specified url.",
                "Content-Type: application/json"
        };

    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("url", "URL", DataType.STRING, elementInfo),
                new Argument("requestBody", "RequestBody", DataType.STRING, elementInfo)
                //new Argument("bearer", "Bearer", DataType.STRING, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("response", "Response", DataType.STRING, elementInfo),
                new OutcomingVariable("status", "Status code", DataType.NUMBER, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    @Override
    public void run(ElementInfo info, ScriptInstance instance) {
        final String urlS = (String) this.getArguments(info)[0].getValue(instance);
        final String requestBody = (String) this.getArguments(info)[1].getValue(instance);

        try {
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            //connection.setRequestProperty("Authorization", "Bearer " + bearer);

            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(requestBody);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output =  in.lines().collect(Collectors.joining("\n"));
                feedTheData(info, instance, output, responseCode);
            } else {
                feedTheData(info, instance, responseMessage, responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getBootstrap().getLogger().warning(ChatColor.RED + "Error with post request: " + e.getMessage() + "\nCause: " + e.getCause());
            feedTheData(info,instance, "ERROR", HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public void feedTheData(ElementInfo info, ScriptInstance instance, Object output, Object responseCode) {
        this.getOutcomingVariables(info)[0].register(instance, new DataRequester() {
            @Override
            public Object request() {
                return output;
            }
        });
        this.getOutcomingVariables(info)[1].register(instance, new DataRequester() {
            @Override
            public Object request() {
                return responseCode;
            }
        });
        this.getConnectors(info)[0].run(instance);
    }
}
