package json.jayson.hysouls.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.ui.EssenceHud;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class EssenceComponent implements Component<EntityStore> {

    public EssenceComponent() {

    }


    public EssenceComponent(int essences) {
        this.essences = essences;
    }

    @Nonnull
    public static final BuilderCodec<EssenceComponent> CODEC = BuilderCodec.builder(EssenceComponent.class, EssenceComponent::new)
            .addField(
                    new KeyedCodec<>("Essences", Codec.INTEGER),
                    (essenceComponent, s) -> essenceComponent.essences = s,
                    essenceComponent -> essenceComponent.essences
            )
            .build();

    private int essences = 0;


    public int getEssences() {
        return essences;
    }

    public void setEssences(@Nullable Ref<EntityStore> ref, int souls) {
        this.essences = souls;
        if (ref != null && ref.isValid()) {
            Player playerComponent = ref.getStore().getComponent(ref, Player.getComponentType());
            if (playerComponent != null) {
                if(playerComponent.getHudManager().getCustomHud() instanceof EssenceHud essenceHud) {
                    essenceHud.setEssences(this.essences);
                }
            }
        }
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new EssenceComponent(getEssences());
    }
}
