package json.jayson.hysouls.components;


import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.Modifier;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;

//TODO maybe get rid of enum
public enum EssenceStats {
    VIGOR("Vigor", 1.8f),
    ENDURANCE("Endurance", 1.2f),
    MIND("Mind",1.4f);


    private String named;
    private float modifierBuff;
    EssenceStats(String named, float modifierBuff) {
        this.named = named;
        this.modifierBuff = modifierBuff;
    }

    public String getNamed() {
        return named;
    }

    public float getModifierBuff() {
        return modifierBuff;
    }

    /*@Deprecated
    public void applyModifierBuff(EntityStatMap map, int input) {
        if(defaultStatType != -1) {
            map.putModifier(getDefaultStatType(), "Essence_" + getNamed(), new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, input * getModifierBuff()));
        }
    }*/

    public <T> KeyedCodec<T> keyedCodec() {
        return new KeyedCodec<T>(getNamed(), (Codec<T>) Codec.INTEGER);
    }
}
