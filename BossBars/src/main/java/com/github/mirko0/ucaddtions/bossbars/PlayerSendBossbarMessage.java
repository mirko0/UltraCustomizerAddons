package com.github.mirko0.ucaddtions.bossbars;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.ArrayList;
import java.util.List;

public class PlayerSendBossbarMessage extends Element {

    public PlayerSendBossbarMessage(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Player BossBar Message";
    }

    @Override
    public String getInternalName() {
        return "player-bossbar-message";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.PAPER;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Send a bossbar message to specified player"
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
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
        final Player player = (Player) this.getArguments(info)[0].getValue(instance);
        final String message = (String) this.getArguments(info)[1].getValue(instance);
        final String color = (String) this.getArguments(info)[2].getValue(instance);
        final String style = (String) this.getArguments(info)[3].getValue(instance);
        final long time = (long) this.getArguments(info)[4].getValue(instance);

        BossBar bossBar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&',message), parseColor(color), parseStyle(style));
        bossBar.addPlayer(player);

        if (time > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                            bossBar.removePlayer(player);
                    }
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
