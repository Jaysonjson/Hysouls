package json.jayson.hysouls.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EssenceParticleSystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float v, int i, @NotNull ArchetypeChunk<EntityStore> archetypeChunk, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        EssenceComponent essenceComponent = archetypeChunk.getComponent(i, ComponentTypes.ESSENCES);
        if (essenceComponent != null) {
            TransformComponent transformComponent = archetypeChunk.getComponent(i, TransformComponent.getComponentType());
            if (transformComponent != null) {
                Vector3d pos = transformComponent.getPosition().clone();
                pos.add(0, 1, 0);
                ParticleUtil.spawnParticleEffect("VoidImpact", pos, store);
            }
            //Makes a cool trail tho
           // ParticleUtil.spawnParticleEffect("Item", archetypeChunk.getComponent(i, TransformComponent.getComponentType()).getPosition(), store);
        }
    }


    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(Query.and(AllLegacyLivingEntityTypesQuery.INSTANCE, Query.not(Player.getComponentType())), ComponentTypes.ESSENCES);
    }
}
