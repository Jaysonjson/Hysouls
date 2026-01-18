package json.jayson.hysouls.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.Modifier;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.Hysouls;
import json.jayson.hysouls.essence_stat.EssenceStat;
import json.jayson.hysouls.essence_stat.EssenceStats;
import json.jayson.hysouls.essence_stat.IEssenceStated;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class EssenceStatComponent implements Component<EntityStore>, IEssenceStated {


    public EssenceStatComponent() {

    }

    public EssenceStatComponent(int vig, int end, int mind) {
        this.vigor = vig;
        this.endurance = end;
        this.mind = mind;
    }

    @Nonnull
    public static final BuilderCodec<EssenceStatComponent> CODEC = BuilderCodec.builder(EssenceStatComponent.class, EssenceStatComponent::new)
            .append(
                    EssenceStats.VIGOR.keyedCodec(),
                    (essenceStatComponent, s) -> essenceStatComponent.vigor = s,
                    essenceStatComponent -> essenceStatComponent.vigor
            ).add()
            .append(
                    EssenceStats.ENDURANCE.keyedCodec(),
                    (essenceStatComponent, s) -> essenceStatComponent.endurance = s,
                    essenceStatComponent -> essenceStatComponent.endurance
            ).add()
            .append(
                    EssenceStats.MIND.keyedCodec(),
                    (essenceStatComponent, s) -> essenceStatComponent.mind = s,
                    essenceStatComponent -> essenceStatComponent.mind
            ).add()
            .build();


    private int vigor = 1;
    private int endurance = 1;
    private int mind = 1;

    public int getEndurance() {
        return endurance;
    }

    public int getVigor() {
        return vigor;
    }

    public int getMind() {
        return mind;
    }

    public int getLevel() {
        return getVigor() + getEndurance() + getMind();
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public void setVigor(int vigor) {
        this.vigor = vigor;
    }

    public void setMind(int mind) {
        this.mind = mind;
    }

    public void apply(Ref<EntityStore> ref) {
        EntityStatMap statMap = ref.getStore().getComponent(ref, EntityStatsModule.get().getEntityStatMapComponentType());
        if(statMap != null) {
            for (EssenceStat value : EssenceStats.getStatsMap().values()) {
                value.applyModifierBuff(statMap, this);
            }
        }
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new EssenceStatComponent(getVigor(), getEndurance(), getMind());
    }

}
