package com.github.mirko0.ucaddtions.pvutils;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GetVelocityViewOpposite extends Element {

    public GetVelocityViewOpposite(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Get Velocity From View(Opposite)";
    }

    @Override
    public String getInternalName() {
        return "get-velocity-opp-from-p-direction";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.ENDER_EYE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Will return opposite X, Z axis of where player is looking at."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("power", "Power", DataType.NUMBER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("xAxis", "X Axis", DataType.NUMBER, elementInfo),
                new OutcomingVariable("zAxis", "Z Axis", DataType.NUMBER, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Player player = (Player) this.getArguments(elementInfo)[0].getValue(scriptInstance);
        long power = (long) this.getArguments(elementInfo)[1].getValue(scriptInstance);

        Vector direction = player.getLocation().getDirection().multiply(power);

        long x = new Double(direction.getX()).longValue();
        long z = new Double(direction.getZ()).longValue();

        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return getOpposite(x);
            }
        });

        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return getOpposite(z);
            }
        });

        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }

    public long getOpposite(long number){
        if (number < 0)
        {
            return number*-1;
        }
        return -number;
    }
}

