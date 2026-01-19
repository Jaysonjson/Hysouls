package json.jayson.hysouls.essence_attribute;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.Modifier;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class EssenceAttribute {

    private String named;
    private List<Integer> defaultStatTypes = new ArrayList<>();
    //Used to debuff the DefaultStats, so it doesnt get too OP at the start
    private List<Float> modifierDebuffs = new ArrayList<>();
    private List<Float> modifierBuffs = new ArrayList<>();

    public EssenceAttribute(String named) {
        this.named = named;
        initialize0();
    }
    public void initialize0() {
        refreshStatType();
        initialize();
    }
    public abstract void initialize();



    public float getModifierType(int index, ModifierType type) {
        switch (type) {
            case BUFF -> {
                if (index < modifierBuffs.size()) {
                    return modifierBuffs.get(index);
                }
            }

            case DEBUFF -> {
                if (index < modifierDebuffs.size()) {
                    return modifierDebuffs.get(index);
                }
            }
        }
        return 0;
    }

    public float setModifierType(int index, float value, ModifierType type) {
        switch (type) {
            case BUFF -> {
                while (modifierBuffs.size() <= index) {
                    modifierBuffs.add(0f);
                }
                modifierBuffs.set(index, value);
            }

            case DEBUFF -> {

                while (modifierDebuffs.size() <= index) {
                    modifierDebuffs.add(0f);
                }
                modifierDebuffs.set(index, value);
            }
        }
        return 0;
    }



    public String getNamed() {
        return named;
    }

    public int getDefaultStatType(int index) {
        if (index < defaultStatTypes.size()) {
            return defaultStatTypes.get(index);
        }
        return 0;
    }

    public void setDefaultStatType(int index, int defaultStatType) {
        while (defaultStatTypes.size() <= index) {
            defaultStatTypes.add(0);
        }
        defaultStatTypes.set(index, defaultStatType);
    }

    public abstract void refreshStatType();

    public void applyModifierBuff(EntityStatMap map, EssenceAttributeHolder input) {
        applyModifierBuff(map, get(input));
    }


    public void applyModifierBuff(EntityStatMap map, int input) {
        refreshStatType();
        for (int i = 0; i < defaultStatTypes.size(); i++) {
            map.putModifier(getDefaultStatType(i), "Essence_" + getNamed(), new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, -getModifierType(i, ModifierType.DEBUFF)));
            map.putModifier(getDefaultStatType(i), "Essence_" + getNamed(), new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, input * getModifierType(i, ModifierType.BUFF)));
        }
    }

    public <T> KeyedCodec<T> keyedCodec() {
        return new KeyedCodec<T>(getNamed(), (Codec<T>) Codec.INTEGER);
    }

    public abstract int get(EssenceAttributeHolder stated);
    public abstract void set(EssenceAttributeHolder stated, int value);


    /* Interface stuff */
    public void levelUiAction(UIEventBuilder builder) {
        builder.addEventBinding(CustomUIEventBindingType.Activating, "#" + getNamed() + "Minus", EventData.of("Action", getNamed() + "Minus"));
        builder.addEventBinding(CustomUIEventBindingType.Activating, "#" + getNamed() + "Plus", EventData.of("Action", getNamed() + "Plus"));
    }

    public void levelUiText(UICommandBuilder builder, EssenceAttributeHolder component, EssenceAttributeHolder page) {
        builder.set("#Next" + getNamed() + ".TextSpans", Message.raw("" + (get(component) + get(page))));
        builder.set("#Current" + getNamed() + ".TextSpans", Message.raw("" + (get(component))));
    }

    public void levelUiEventAction(EssenceAttributeHolder page, String action) {
        if(action.equalsIgnoreCase(getNamed() + "Minus")) {
            if (get(page) != 0) {
                set(page, get(page) - 1);
                page.setLevel(page.getLevel() - 1);
            }
        } else if(action.equalsIgnoreCase(getNamed() + "Plus")) {
            if (get(page) != 99) {
                set(page, get(page) + 1);
                page.setLevel(page.getLevel() + 1);
            }
        }
    }

    public String getLevelPageUIPath() {
        return "Pages/EssenceAttributes/" + getNamed() + ".ui";
    }

    //Only call this with gradle
    //This is ugly and lazy but I dont care
    public String asLevelPageUI(String parent) {
        return parent.replaceAll("ATTRIBUTEINS", getNamed());
    }

    public enum ModifierType {
        BUFF,
        DEBUFF
    }

}
