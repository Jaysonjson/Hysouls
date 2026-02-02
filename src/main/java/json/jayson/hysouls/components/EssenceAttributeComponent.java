package json.jayson.hysouls.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.lookup.CodecMapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributeMapCodec;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import json.jayson.hysouls.essence_attribute.EssenceAttributeHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class EssenceAttributeComponent implements Component<EntityStore>, EssenceAttributeHolder {


    public EssenceAttributeComponent() {
        addDefaultEssenceAttributes(1);
    }

    public EssenceAttributeComponent(HashMap<String, Integer> essenceAttributes) {
        this.essenceAttributes = essenceAttributes;
    }

    @Nonnull
    public static final BuilderCodec<EssenceAttributeComponent> CODEC = BuilderCodec.builder(EssenceAttributeComponent.class, EssenceAttributeComponent::new)
            .append(
                    new KeyedCodec<>("Map", new EssenceAttributeMapCodec()),
                    (essenceAttributeComponent, s) -> essenceAttributeComponent.essenceAttributes = s,
                    essenceAttributeComponent -> essenceAttributeComponent.essenceAttributes
            ).add()
            .build();


    private HashMap<String, Integer> essenceAttributes = new HashMap<>();

    @Override
    public HashMap<String, Integer> getEssenceAttributes() {
        return essenceAttributes;
    }

    public int getLevel() {
        int level = 0;
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            level += value.get(this);
        }
        return level;
    }


    public void apply(Ref<EntityStore> ref) {
        EntityStatMap statMap = ref.getStore().getComponent(ref, EntityStatsModule.get().getEntityStatMapComponentType());
        if(statMap != null) {
            for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
                value.applyModifierBuff(statMap, this);
            }
        }
    }

    public float extraDropChance() {
        float dropChance = 0;

        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            dropChance += value.getExtraDropChance(this);
        }

        return dropChance;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new EssenceAttributeComponent(getEssenceAttributes());
    }

}
