package json.jayson.hysouls.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.packets.inventory.DropItemStack;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.ItemUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.SoulEssenceMap;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.util.Pair;

import javax.annotation.Nonnull;

public class EssenceSystem {

    public static class EntityDeath extends DeathSystems.OnDeathSystem  {

        @Nonnull
        public Query<EntityStore> getQuery() {
            return Query.and(AllLegacyLivingEntityTypesQuery.INSTANCE, Query.not(Player.getComponentType()));
        }

        @Override
        public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent component, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
            Damage damageInfo = component.getDeathInfo();
            if (damageInfo != null && damageInfo.getSource() instanceof Damage.EntitySource entitySource) {
                if(entitySource.getRef().isValid()) {
                    Ref<EntityStore> entityStoreRef = entitySource.getRef();
                    EssenceComponent essenceComponent = entityStoreRef.getStore().getComponent(entityStoreRef, ComponentTypes.ESSENCES);
                    if(essenceComponent != null) {
                        EntityStatMap statMap = ref.getStore().getComponent(ref, EntityStatsModule.get().getEntityStatMapComponentType());
                        int extra = 0;
                        EssenceComponent victimEssenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
                        if(victimEssenceComponent != null) {
                            extra = victimEssenceComponent.getEssences();
                        }
                        if(statMap != null) {
                            essenceComponent.setEssences(entityStoreRef, (int) (essenceComponent.getEssences() + extra + + statMap.get(DefaultEntityStatTypes.getHealth()).getMax()));
                        }
                    }
                }
            }
        }
    }

    public static class PlayerDeath extends DeathSystems.OnDeathSystem {
        @Nonnull
        public Query<EntityStore> getQuery() {
            return Player.getComponentType();
        }

        public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent component, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
            Damage damageInfo = component.getDeathInfo();
            if(ref.isValid()) {
                EssenceComponent essenceComponent = store.getComponent(ref, ComponentTypes.ESSENCES);
                if(essenceComponent == null) return;
                int essences =  essenceComponent.getEssences();
                essenceComponent.setEssences(ref, 0);
                if(essences == 0) return;
                boolean dropEssences = false;
                if (damageInfo != null && damageInfo.getSource() instanceof Damage.EntitySource entitySource) {
                    Ref<EntityStore> killer = entitySource.getRef();
                    if (killer.isValid()) {
                        EssenceComponent existing = store.getComponent(killer, ComponentTypes.ESSENCES);
                        int totalEssences = essences + (existing != null ? existing.getEssences() : 0);
                        commandBuffer.putComponent(killer, ComponentTypes.ESSENCES, new EssenceComponent(totalEssences));
                    } else {
                        dropEssences = true;
                    }
                }  else {
                    dropEssences = true;
                }

                if(dropEssences) {
                    for (Pair<String, Integer> pair : SoulEssenceMap.getFor(essences)) {
                        ItemStack itemStack = new ItemStack(pair.key(), pair.value());
                        Holder<EntityStore> itemEntityHolder = ItemComponent.generateItemDrop(store, itemStack, ref.getStore().getComponent(ref, TransformComponent.getComponentType()).getPosition(), Vector3f.ZERO, 0, 0, 0);
                        store.addEntity(itemEntityHolder, AddReason.SPAWN);
                    }
                }
            }
        }
    }


}
