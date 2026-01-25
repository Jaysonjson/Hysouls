package json.jayson.hysouls.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import json.jayson.hysouls.ui.BonfirePage;
import org.jetbrains.annotations.NotNull;

public class BonfireInteraction extends SimpleInstantInteraction {

    public static final BuilderCodec<BonfireInteraction> CODEC = BuilderCodec.builder(
                    BonfireInteraction.class, BonfireInteraction::new, SimpleInstantInteraction.CODEC
            )
            .build();

    @Override
    protected void firstRun(@NotNull InteractionType interactionType, @NotNull InteractionContext interactionContext, @NotNull CooldownHandler cooldownHandler) {
        if (interactionType == InteractionType.Use) {
            Ref<EntityStore> ref = interactionContext.getEntity();
            EntityStatMap statMap = ref.getStore().getComponent(ref, EntityStatMap.getComponentType());
            if(statMap == null) { return; }
            statMap.resetStatValue(DefaultEntityStatTypes.getHealth());

            Player player = ref.getStore().getComponent(ref, Player.getComponentType());
            if(player == null) return;

            for (int i = 0; i < player.getInventory().getCombinedEverything().getContainersSize(); i++) {
                for (short j = 0; j < player.getInventory().getCombinedEverything().getContainer(i).getCapacity(); j++) {
                    ItemStack stack = player.getInventory().getCombinedEverything().getContainer(i).getItemStack(j);
                    if(stack == null) continue;

                    if(stack.getItemId().equalsIgnoreCase("Estus_Flask_Empty")) {
                        player.getInventory().getCombinedEverything().getContainer(i).setItemStackForSlot(j, new ItemStack("Estus_Flask_Full"));
                    }
                }
            }
            PlayerRef playerRef = interactionContext.getCommandBuffer().getComponent(ref, PlayerRef.getComponentType());
            player.getPageManager().openCustomPage(ref, ref.getStore(), new BonfirePage(playerRef, interactionContext.getTargetBlock()));

        }
    }

}
