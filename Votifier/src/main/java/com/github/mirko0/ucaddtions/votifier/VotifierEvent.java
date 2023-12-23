package com.github.mirko0.ucaddtions.votifier;

import com.vexsoftware.votifier.model.Vote;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierEvent extends Constructor implements Listener {

    public VotifierEvent(UltraCustomizer plugin) {
        super(plugin);
    }

    public String getName() {
        return "Vote Received (Votifier)";
    }

    public String getInternalName() {
        return "on-vote-received";
    }

    public String getRequiredPlugin() {
        return "Votifier";
    }

    public XMaterial getMaterial() {
        return XMaterial.FEATHER;
    }


    public String[] getDescription() {
        return new String[] { "Will be executed when", "a vote is received" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { new OutcomingVariable("player", "Player", DataType.PLAYER, elementInfo), new OutcomingVariable("name", "Player's Name", DataType.STRING, elementInfo), new OutcomingVariable("address", "Vote site address", DataType.STRING, elementInfo), new OutcomingVariable("serviceName", "Name of voting site", DataType.STRING, elementInfo) };
    }

    public boolean isUnlisted() {
        return false;
    }

    @EventHandler
    public void onVotifierEvent(com.vexsoftware.votifier.model.VotifierEvent event) {
        Vote vote = event.getVote();
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();
            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    Player player = Bukkit.getPlayerExact(vote.getUsername());
                    if (player == null)
                        return Bukkit.getOfflinePlayer(vote.getUsername());
                    return player;
                }
            });
            getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
                public Object request() {
                    return vote.getUsername();
                }
            });
            getOutcomingVariables(elementInfo)[2].register(instance, new DataRequester() {
                public Object request() {
                    return vote.getAddress();
                }
            });
            getOutcomingVariables(elementInfo)[3].register(instance, new DataRequester() {
                public Object request() {
                    return vote.getServiceName();
                }
            });
            return instance;
        });
    }
}
