package json.jayson.hysouls.components;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import json.jayson.hysouls.essence_attribute.EssenceAttributeHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class EssenceAttributeComponent implements Component<EntityStore>, EssenceAttributeHolder {


    public EssenceAttributeComponent() {

    }

    public EssenceAttributeComponent(int vig, int end, int mind, int dex, int str, int luck) {
        this.vigor = vig;
        this.endurance = end;
        this.mind = mind;
        this.dexterity = dex;
        this.strength = str;
        this.luck = luck;
    }

    @Nonnull
    public static final BuilderCodec<EssenceAttributeComponent> CODEC = BuilderCodec.builder(EssenceAttributeComponent.class, EssenceAttributeComponent::new)
            .append(
                    EssenceAttributes.VIGOR.keyedCodec(),
                    (essenceAttributeComponent, s) -> essenceAttributeComponent.vigor = s,
                    essenceAttributeComponent -> essenceAttributeComponent.vigor
            ).add()
            .append(
                    EssenceAttributes.ENDURANCE.keyedCodec(),
                    (essenceAttributeComponent, s) -> essenceAttributeComponent.endurance = s,
                    essenceAttributeComponent -> essenceAttributeComponent.endurance
            ).add()
            .append(
                    EssenceAttributes.MIND.keyedCodec(),
                    (essenceAttributeComponent, s) -> essenceAttributeComponent.mind = s,
                    essenceAttributeComponent -> essenceAttributeComponent.mind
            ).add()
            .append(
                    EssenceAttributes.DEXTERITY.keyedCodec(),
                    (essenceAttributeComponent, s) -> essenceAttributeComponent.dexterity = s,
                    essenceAttributeComponent -> essenceAttributeComponent.dexterity
            ).add()
            .append(
                    EssenceAttributes.STRENGTH.keyedCodec(),
                    (essenceAttributeComponent, s) -> essenceAttributeComponent.strength = s,
                    essenceAttributeComponent -> essenceAttributeComponent.strength
            ).add()
            .append(
                    EssenceAttributes.LUCK.keyedCodec(),
                    (essenceAttributeComponent, s) -> essenceAttributeComponent.luck = s,
                    essenceAttributeComponent -> essenceAttributeComponent.luck
            ).add()
            .build();


    private int vigor = 1;
    private int endurance = 1;
    private int mind = 1;
    private int strength = 1;
    private int dexterity = 1;
    private int luck = 1;

    public int getEndurance() {
        return endurance;
    }

    public int getVigor() {
        return vigor;
    }

    public int getMind() {
        return mind;
    }

    @Override
    public int getDexterity() {
        return dexterity;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int getLuck() {
        return luck;
    }

    public int getLevel() {
        int level = 0;
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            level += value.get(this);
        }
        return level;
    }


    @Override
    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public void setLuck(int luck) {
        this.luck = luck;
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
        return new EssenceAttributeComponent(getVigor(), getEndurance(), getMind(), getDexterity(), getStrength(), getLuck());
    }

}
