package json.jayson.hysouls.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.protocol.IncrementCooldownInteraction;
import com.hypixel.hytale.protocol.packets.player.DamageInfo;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceAttributeComponent;
import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import json.jayson.hysouls.weapon_scaling.WeaponScalingMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EssenceDamageSystem extends DamageEventSystem {

    @Override
    public @Nullable SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getGatherDamageGroup();
    }

    @Override
    public void handle(int i, @NotNull ArchetypeChunk<EntityStore> archetypeChunk, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer, @NotNull Damage damage) {
        if(damage.getSource() instanceof Damage.EntitySource entitySource) {
            Ref<EntityStore> damager = entitySource.getRef();
            if (damager.isValid()) {
                EssenceAttributeComponent essenceAttributeComponent = damager.getStore().getComponent(damager, ComponentTypes.ESSENCE_ATTRIBUTE);
                Entity damagerEntity = EntityUtils.getEntity(damager, commandBuffer);
                if(essenceAttributeComponent != null) {
                    float extraDamage = 0;
                    for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
                        extraDamage += value.getExtraDamage(essenceAttributeComponent);
                    }

                    damage.setAmount(damage.getAmount() + extraDamage);

                    if (damagerEntity instanceof LivingEntity livingEntity) {
                        String itemId = livingEntity.getInventory().getActiveHotbarItem().getItemId();
                        WeaponScalingMap.Scaling scaling = WeaponScalingMap.getScaling(itemId);
                        if(scaling != null) {
                            for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
                                if (scaling.get(value) != null) {
                                    damage.setAmount((float) (damage.getAmount() + Math.pow(value.get(essenceAttributeComponent), scaling.get(value).getModifier())));
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(AllLegacyLivingEntityTypesQuery.INSTANCE, Query.not(Player.getComponentType()));
    }

}
