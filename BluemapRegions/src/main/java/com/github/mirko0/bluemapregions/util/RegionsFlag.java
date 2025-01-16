package com.github.mirko0.bluemapregions.util;

import com.github.mirko0.bluemapregions.AddonMain;
import me.TechsCode.UltraRegions.UltraRegions;
import me.TechsCode.UltraRegions.base.item.XMaterial;
import me.TechsCode.UltraRegions.flags.Flag;
import me.TechsCode.UltraRegions.flags.FlagTypes;
import me.TechsCode.UltraRegions.storage.FlagValue;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;

public class RegionsFlag extends Flag {
    public RegionsFlag(UltraRegions ultraRegions) {
        super(ultraRegions, "BlueMapFlag");
        AddonMain main = AddonMain.instance;
    }

    public RegionsFlag(UltraRegions plugin, String id, FlagTypes type) {
        super(plugin, id, type);
    }


    @Override
    public String getName() {
        return "Internal-BluemapRegions-Flag";
    }

    @Override
    public String getDescription() {
        return "Do not use this.";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.BEDROCK;
    }

    @Override
    public FlagValue getDefaultValue() {
        return FlagValue.ALLOW;
    }

    @Override
    public boolean isPlayerSpecificFlag() {
        return false;
    }

    @EventHandler
    private void onReady(ServerLoadEvent event) {
    }
}
