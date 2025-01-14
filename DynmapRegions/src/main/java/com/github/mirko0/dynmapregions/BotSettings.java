package com.github.mirko0.dynmapregions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BotSettings {

    private int refreshTimeInMinutes;
    private String layerName;
    private String lineColor;
    private String fillColor;
    private double lineOpacity;
    private double fillOpacity;
    private int weight;
    private boolean use3D;
}
