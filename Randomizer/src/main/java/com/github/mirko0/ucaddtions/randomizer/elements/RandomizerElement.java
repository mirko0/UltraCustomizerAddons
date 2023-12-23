package com.github.mirko0.ucaddtions.randomizer.elements;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

import java.util.Random;

public class RandomizerElement extends Element {
    public RandomizerElement(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Randomizer";
    }

    @Override
    public String getInternalName() {
        return "randomizer-chancee";
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
                "Executes a random element, with chance.",
                "Ex: 30% chance means the first option",
                "will have 30% chance to be triggered."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("chance", "Chance", DataType.NUMBER, elementInfo)
        };
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
                        return new String[]{"Will be executed if they pass the randomizer.", "The selected chance is used on this option."};
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
                        return new String[]{"Will be executed if they pass the randomizer."};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.BLACK_STAINED_GLASS_PANE;
                    }
                }};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        long chance = (long) this.getArguments(elementInfo)[0].getValue(scriptInstance);
        if (chance < 0) {
            chance = 50;
        }

        Random random = new Random();
        int randomNumber = random.nextInt(100);
        if (randomNumber < chance) {
            this.getConnectors(elementInfo)[0].run(scriptInstance);
        } else {
            this.getConnectors(elementInfo)[1].run(scriptInstance);
        }

    }
}
