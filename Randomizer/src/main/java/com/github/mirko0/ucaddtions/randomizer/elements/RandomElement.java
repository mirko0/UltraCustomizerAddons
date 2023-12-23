package com.github.mirko0.ucaddtions.randomizer.elements;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;

import java.util.Random;

public class RandomElement extends Element {
    public RandomElement(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Random Element";
    }

    @Override
    public String getInternalName() {
        return "randomizer-relement";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.CHEST;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Executes a random element."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }


    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{
                new Child(elementInfo, "first") {
                    @Override
                    public String getName() {
                        return "First set of elements";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"Will be executed if they pass the randomizer.", "(50%/50% chance)"};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.WHITE_STAINED_GLASS_PANE;
                    }
                },
                new Child(elementInfo, "second") {
                    @Override
                    public String getName() {
                        return "Second set of elements";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"Will be executed if they pass the randomizer.", "(50%/50% chance)"};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.BLACK_STAINED_GLASS_PANE;
                    }
                }};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        int randomNumber = new Random().nextInt(2);
        if (randomNumber == 0) {
            this.getConnectors(elementInfo)[0].run(scriptInstance);
        } else {
            this.getConnectors(elementInfo)[1].run(scriptInstance);
        }

    }
}
