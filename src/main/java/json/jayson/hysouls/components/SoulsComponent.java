package json.jayson.hysouls.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.ui.SoulsHud;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class SoulsComponent implements Component<EntityStore> {

    public SoulsComponent() {

    }


    public SoulsComponent(int souls) {
        this.souls = souls;
    }

    @Nonnull
    public static final BuilderCodec<SoulsComponent> CODEC = BuilderCodec.builder(SoulsComponent.class, SoulsComponent::new)
            .addField(
                    new KeyedCodec<>("Souls", Codec.INTEGER),
                    (soulsComponent, s) -> soulsComponent.souls = s,
                    soulsComponent -> soulsComponent.souls
            )
            .build();

    private int souls = 0;


    public int getSouls() {
        return souls;
    }

    public void setSouls(@Nullable Ref<EntityStore> ref, int souls) {
        this.souls = souls;
        if (ref != null && ref.isValid()) {
            Player playerComponent = ref.getStore().getComponent(ref, Player.getComponentType());
            if (playerComponent != null) {
                if(playerComponent.getHudManager().getCustomHud() instanceof SoulsHud soulsHud) {
                    soulsHud.setSouls(this.souls);
                }
            }
        }
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new SoulsComponent(getSouls());
    }
}
