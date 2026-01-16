package json.jayson.hysouls.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.flock.FlockDeathSystems;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class EssenceSystem extends FlockDeathSystems.EntityDeath {

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

    @Override
    public @NotNull Query<EntityStore> getQuery() {
        return AllLegacyLivingEntityTypesQuery.INSTANCE;
    }
}
