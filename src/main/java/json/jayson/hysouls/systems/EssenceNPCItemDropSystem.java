package json.jayson.hysouls.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.gameplay.DeathConfig;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.modules.item.ItemModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceAttributeComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class EssenceNPCItemDropSystem extends DeathSystems.OnDeathSystem {
    @Override
    public void onComponentAdded(@NotNull Ref<EntityStore> ref, @NotNull DeathComponent deathComponent, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        Damage damageInfo = deathComponent.getDeathInfo();
        if (damageInfo == null) return;
        if(damageInfo.getSource() instanceof Damage.EntitySource entitySource) {
            Ref<EntityStore> killer = entitySource.getRef();
            if (!killer.isValid()) return;

            EssenceAttributeComponent attributeComponent = store.getComponent(killer, ComponentTypes.ESSENCE_ATTRIBUTE);
            if (attributeComponent == null) return;

            if (deathComponent.getItemsLossMode() == DeathConfig.ItemsLossMode.ALL) {
                NPCEntity npcComponent = commandBuffer.getComponent(ref, NPCEntity.getComponentType());

                assert npcComponent != null;

                Role role = npcComponent.getRole();
                assert role != null;

                List<ItemStack> itemsToDrop = new ObjectArrayList();
                if (role.isPickupDropOnDeath()) {
                    Inventory inventory = npcComponent.getInventory();
                    itemsToDrop.addAll(inventory.getStorage().dropAllItemStacks());
                }

                String dropListId = role.getDropListId();
                if (dropListId != null) {
                    ItemModule itemModule = ItemModule.get();
                    if (itemModule.isEnabled()) {
                        List<ItemStack> randomItemsToDrop = itemModule.getRandomItemDrops(dropListId);
                        itemsToDrop.addAll(randomItemsToDrop);
                    }
                }

                TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());

                if(transformComponent == null) return;

                Vector3d position = transformComponent.getPosition();
                HeadRotation headRotationComponent = store.getComponent(ref, HeadRotation.getComponentType());
                Vector3d dropPosition = position.clone().add(0.0F, 1.0F,0.0F);

                if(headRotationComponent == null) return;
                Vector3f headRotation = headRotationComponent.getRotation();
                Random random = new Random();
                for (ItemStack itemStack : itemsToDrop) {
                    if(random.nextFloat() <= attributeComponent.extraDropChance()) {
                        itemStack = itemStack.withQuantity(random.nextInt(1, itemStack.getQuantity() + 1));
                        Holder<EntityStore> itemEntityHolder = ItemComponent.generateItemDrop(store, itemStack, dropPosition, headRotation.clone(), 0, 0, 0);
                        commandBuffer.addEntity(itemEntityHolder, AddReason.SPAWN);
                    }
                }

            }

        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(NPCEntity.getComponentType(), TransformComponent.getComponentType(), HeadRotation.getComponentType(), Query.not(Player.getComponentType()));
    }
}
