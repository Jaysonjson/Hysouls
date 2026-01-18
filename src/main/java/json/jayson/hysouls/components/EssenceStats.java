package json.jayson.hysouls.components;

import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;

public enum EssenceStats {
    VIGOR("Vigor", 1.8f),
    ENDURANCE("Endurance", 1.2f),
    MIND("Mind",1.4f);


    String named;
    float modifierBuff;
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
}
