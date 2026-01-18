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
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class EssenceStatComponent implements Component<EntityStore> {


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
                    new KeyedCodec<>("Vigor", Codec.INTEGER),
                    (essenceStatComponent, s) -> essenceStatComponent.vigor = s,
                    essenceStatComponent -> essenceStatComponent.vigor
            ).add()
            .append(
                    new KeyedCodec<>("Endurance", Codec.INTEGER),
                    (essenceStatComponent, s) -> essenceStatComponent.endurance = s,
                    essenceStatComponent -> essenceStatComponent.endurance
            ).add()
            .append(
                    new KeyedCodec<>("Mind", Codec.INTEGER),
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

    public static int calculateRequiredEssences(int level) {
        return (int) ((float)1200 * Math.pow(1.17, level + 1) - (float)1200 * Math.pow(1.17, level));
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
            statMap.putModifier(DefaultEntityStatTypes.getHealth(), "Essence_Vigor", new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, getVigor() * 2.1f));
            statMap.putModifier(DefaultEntityStatTypes.getStamina(), "Essence_Endurance", new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, getEndurance() * 2f));
            statMap.putModifier(DefaultEntityStatTypes.getMana(), "Essence_Mind", new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, getMind() * 2.3f));
        }
    }


    @Override
    public @Nullable Component<EntityStore> clone() {
        return new EssenceStatComponent(getVigor(), getEndurance(), getMind());
    }

}
