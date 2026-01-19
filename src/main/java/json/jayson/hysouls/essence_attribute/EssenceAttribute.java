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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EssenceAttribute {

    private String named;
    private List<EssenceAttributeModifier> modifiers = new ArrayList<>();

    public EssenceAttribute(String named, @Nullable EssenceAttributeModifier... modifier) {
        this.named = named;
        if(modifier != null) {
            this.modifiers.addAll(Arrays.asList(modifier));
        }
    }

    public String getNamed() {
        return named;
    }

    public List<EssenceAttributeModifier> getModifiers() {
        return modifiers;
    }

    public void addModifier(EssenceAttributeModifier modifier) {
        modifiers.add(modifier);
    }

    public void applyModifierBuff(EntityStatMap map, EssenceAttributeHolder input) {
        applyModifierBuff(map, get(input));
    }

    public void applyModifierBuff(EntityStatMap map, int input) {
        for (EssenceAttributeModifier modifier : modifiers) {
            if(modifier.getTarget() == EssenceAttributeModifier.Target.PLAYER) {
                float amount = input * modifier.getValue();
                if(modifier.getType() == EssenceAttributeModifier.Type.DEBUFF) {
                    amount = -modifier.getValue();
                }
                map.putModifier(modifier.getStatIndex(), "Essence_" + getNamed() + "_" + modifier.getType().name(), new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, amount));
            }
        }
    }

    public <T> KeyedCodec<T> keyedCodec() {
        return new KeyedCodec<>(getNamed(), (Codec<T>) Codec.INTEGER);
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

}
