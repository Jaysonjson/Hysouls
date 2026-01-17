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

    public EssenceStatComponent(int vit, int dex, int inte) {
        this.vitality = vit;
        this.dexterity = dex;
        this.intelligence = inte;
    }

    @Nonnull
    public static final BuilderCodec<EssenceStatComponent> CODEC = BuilderCodec.builder(EssenceStatComponent.class, EssenceStatComponent::new)
            .addField(
                    new KeyedCodec<>("Vitality", Codec.INTEGER),
                    (essenceStatComponent, s) -> essenceStatComponent.vitality = s,
                    essenceStatComponent -> essenceStatComponent.vitality
            )
            .addField(
                    new KeyedCodec<>("Deterity", Codec.INTEGER),
                    (essenceStatComponent, s) -> essenceStatComponent.dexterity = s,
                    essenceStatComponent -> essenceStatComponent.dexterity
            )

            .addField(
                    new KeyedCodec<>("Intelligence", Codec.INTEGER),
                    (essenceStatComponent, s) -> essenceStatComponent.intelligence = s,
                    essenceStatComponent -> essenceStatComponent.intelligence
            )
            .build();


    private int vitality = 1;
    private int dexterity = 1;
    private int intelligence = 1;

    public int getDexterity() {
        return dexterity;
    }

    public int getVitality() {
        return vitality;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getLevel() {
        return getVitality() + getDexterity() + getIntelligence();
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void apply(Ref<EntityStore> ref) {
        EntityStatMap statMap = ref.getStore().getComponent(ref, EntityStatsModule.get().getEntityStatMapComponentType());
        if(statMap != null) {
            statMap.putModifier(DefaultEntityStatTypes.getHealth(), "Essence_Vitality", new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, getVitality() * 1.5f));
            statMap.putModifier(DefaultEntityStatTypes.getStamina(), "Essence_Dexterity", new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, getDexterity() * 1.3f));
            statMap.putModifier(DefaultEntityStatTypes.getMana(), "Essence_Intelligence", new StaticModifier(Modifier.ModifierTarget.MAX, StaticModifier.CalculationType.ADDITIVE, getIntelligence() * 1.7f));
        }
    }


    @Override
    public @Nullable Component<EntityStore> clone() {
        return new EssenceStatComponent(getVitality(), getDexterity(), getIntelligence());
    }

}
