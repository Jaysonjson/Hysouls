package json.jayson.hysouls.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.OpenCustomUIInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.ui.LevelPage;
import org.jetbrains.annotations.NotNull;

public class OpenLevelPageInteraction extends SimpleInstantInteraction {

    public static final BuilderCodec<OpenLevelPageInteraction> CODEC = BuilderCodec.builder(
            OpenLevelPageInteraction.class, OpenLevelPageInteraction::new, SimpleInstantInteraction.CODEC
    ).build();

    @Override
    protected void firstRun(@NotNull InteractionType interactionType, @NotNull InteractionContext interactionContext, @NotNull CooldownHandler cooldownHandler) {
        if(interactionType == InteractionType.Use) {
            Ref<EntityStore> ref = interactionContext.getEntity();
            CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
            if(ref.isValid() && commandBuffer != null) {
                Player player = commandBuffer.getComponent(ref, Player.getComponentType());
                if(player != null) {
                    PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
                    if(playerRef != null) {
                        player.getPageManager().openCustomPage(ref, ref.getStore(), new LevelPage(playerRef));
                    }
                }
            }
        }
    }
}
