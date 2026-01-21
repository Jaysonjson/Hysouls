package json.jayson.hysouls.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import org.jetbrains.annotations.NotNull;

public class SecondarySoulEssenceInteraction extends SimpleInstantInteraction {


    int souls = 1;
    public SecondarySoulEssenceInteraction() {

    }

    public static final BuilderCodec<SecondarySoulEssenceInteraction> CODEC = BuilderCodec.builder(
                    SecondarySoulEssenceInteraction.class, SecondarySoulEssenceInteraction::new, SimpleInstantInteraction.CODEC
            )
            .append(
                    new KeyedCodec<>("Souls", Codec.INTEGER),
                    (secondarySoulEssenceInteraction, s) -> secondarySoulEssenceInteraction.souls = s,
                    secondarySoulEssenceInteraction -> secondarySoulEssenceInteraction.souls
            ).add()

            .build();


    @Override
    protected void firstRun(@NotNull InteractionType interactionType, @NotNull InteractionContext interactionContext, @NotNull CooldownHandler cooldownHandler) {
        if(interactionType == InteractionType.Secondary) {
            Ref<EntityStore> ref = interactionContext.getEntity();
            CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
            if(ref.isValid() && commandBuffer != null) {
                EssenceComponent essenceComponent = commandBuffer.getComponent(ref, ComponentTypes.ESSENCES);
                Player player = commandBuffer.getComponent(ref, Player.getComponentType());
                if(player == null) { return; };
                if(essenceComponent == null) { return; }
                ItemStack stack = player.getInventory().getActiveHotbarItem();
                essenceComponent.setEssences(ref, essenceComponent.getEssences() + souls * stack.getQuantity());
                player.getInventory().getHotbar().setItemStackForSlot(player.getInventory().getActiveHotbarSlot(), stack.withQuantity(0));
            }
        }
    }
}
