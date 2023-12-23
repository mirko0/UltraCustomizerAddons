package com.github.mirko0.ucaddtions.bossbars;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BroadcastBossbar extends Element {

    public BroadcastBossbar(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Broadcast BossBar Message";
    }

    @Override
    public String getInternalName() {
        return "broadcast-bossbar-message";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.GRAY_STAINED_GLASS;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Send a bossbar message to all online players"
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("message", "Message", DataType.STRING, elementInfo),
                new Argument("color", "Color", DataType.STRING, elementInfo),
                new Argument("style", "Style", DataType.STRING, elementInfo),
                new Argument("time", "Time", DataType.NUMBER, elementInfo)
        };
    }


    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    @Override
    public void run(ElementInfo info, ScriptInstance instance) {
        final String message = (String) this.getArguments(info)[0].getValue(instance);
        final String color = (String) this.getArguments(info)[1].getValue(instance);
        final String style = (String) this.getArguments(info)[2].getValue(instance);
        final long time = (long) this.getArguments(info)[3].getValue(instance);

        BossBar bossBar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&',message), parseColor(color), parseStyle(style));
        if (Bukkit.getOnlinePlayers().size() == 0){
            this.getConnectors(info)[0].run(instance);
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }

        if (time > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                        bossBar.removeAll();
                }
            }.runTaskLater(plugin.getBootstrap(), time * 20);
        }

        this.getConnectors(info)[0].run(instance);
    }

    public BarColor parseColor(String color){
        List<String> colors = new ArrayList<>();
        for (BarColor barColor : BarColor.values()){
            colors.add(barColor.toString());
        }

        if (colors.contains(color)){
            return BarColor.valueOf(color);
        }
        return BarColor.PURPLE;
    }

    public BarStyle parseStyle(String style){
        List<String> styles = new ArrayList<>();
        for (BarStyle barStyle : BarStyle.values()){
            styles.add(barStyle.toString());
        }

        if (styles.contains(style)){
            return BarStyle.valueOf(style);
        }
        return BarStyle.SOLID;
    }

}
