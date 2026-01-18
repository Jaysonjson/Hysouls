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

public abstract class EssenceAttribute {

    private String named;
    private float modifierBuff;
    //Used to debuff the DefaultStats, so it doesnt get too OP at the start
    private int modifierDebuff;
    private int defaultStatType = -1;
    public EssenceAttribute(String named, float modifierBuff, int modifierDebuff) {
        this.named = named;
        this.modifierBuff = modifierBuff;
        this.modifierDebuff = modifierDebuff;
    }

    public int getModifierDebuff() {
        return modifierDebuff;
    }

    public String getNamed() {
        return named;
    }

    public float getModifierBuff() {
        return modifierBuff;
    }

    public int getDefaultStatType() {
        return defaultStatType;
    }

    public void setDefaultStatType(int defaultStatType) {
        this.defaultStatType = defaultStatType;
    }

    public abstract void refreshStatType();

    public void applyModifierBuff(EntityStatMap map, EssenceAttributeHolder input) {
        applyModifierBuff(map, get(input));
    }


    public void applyModifierBuff(EntityStatMap map, int input) {
        refreshStatType();
        if(defaultStatType != -1) {
            map.putModifier(getDefaultStatType(), "Essence_" + getNamed(), new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, -modifierDebuff));
            map.putModifier(getDefaultStatType(), "Essence_" + getNamed(), new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, input * getModifierBuff()));
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


}
