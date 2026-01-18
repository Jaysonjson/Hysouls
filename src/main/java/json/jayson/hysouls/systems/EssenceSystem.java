package json.jayson.hysouls.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;

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
                        if(statMap != null) {
                            essenceComponent.setEssences(entityStoreRef, (int) (essenceComponent.getEssences() + statMap.get(DefaultEntityStatTypes.getHealth()).getMax()));
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
            if(ref.isValid()) {
                EssenceComponent essenceComponent = store.getComponent(ref, ComponentTypes.ESSENCES);
                if(essenceComponent != null) {
                    essenceComponent.setEssences(ref, 0);
                }
            }
        }
    }


}
