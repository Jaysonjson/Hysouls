package json.jayson.hysouls;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceStatComponent;

public class EssenceUtil {

    @Deprecated
    public static void levelUp(Ref<EntityStore> ref) {
        EssenceStatComponent statComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_STAT);
        if(statComponent != null) statComponent.apply(ref);
    }

}
