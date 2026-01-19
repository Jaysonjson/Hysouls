package json.jayson.hysouls;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceAttributeComponent;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import json.jayson.hysouls.interactions.OpenLevelPageInteraction;
import json.jayson.hysouls.systems.EssenceParticleSystem;
import json.jayson.hysouls.systems.EssenceSystem;
import json.jayson.hysouls.ui.EssenceHud;

public class Hysouls extends JavaPlugin {

    public static String VERSION = "0.0.0";
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public Hysouls(JavaPluginInit init) {
        super(init);
        VERSION = getManifest().getVersion().toString();
        EssenceAttributes.init();
    }

    @Override
    protected void setup() {
        getCodecRegistry(Interaction.CODEC).register("OpenLevelUpUI", OpenLevelPageInteraction.class, OpenLevelPageInteraction.CODEC);

        ComponentTypes.ESSENCES = getEntityStoreRegistry().registerComponent(EssenceComponent.class, "Essences", EssenceComponent.CODEC);
        ComponentTypes.ESSENCE_ATTRIBUTE = getEntityStoreRegistry().registerComponent(EssenceAttributeComponent.class, "EssenceAttributes", EssenceAttributeComponent.CODEC);

        //getCommandRegistry().registerCommand(new LevelUpPageCommand());

        //TODO: Mainly still just testing
        getEventRegistry().registerGlobal(PlayerConnectEvent.class, playerConnectEvent -> {
            if(playerConnectEvent.getHolder().getComponent(ComponentTypes.ESSENCES) == null) playerConnectEvent.getHolder().addComponent(ComponentTypes.ESSENCES, new EssenceComponent());
            if(playerConnectEvent.getHolder().getComponent(ComponentTypes.ESSENCE_ATTRIBUTE) == null) playerConnectEvent.getHolder().addComponent(ComponentTypes.ESSENCE_ATTRIBUTE, new EssenceAttributeComponent());

            Player playerComponent = playerConnectEvent.getHolder().getComponent(Player.getComponentType());
            if (playerComponent != null) {
                int souls = 0;
                EssenceComponent essenceComponent = playerConnectEvent.getHolder().getComponent(ComponentTypes.ESSENCES);
                if(essenceComponent != null) {
                    souls = essenceComponent.getEssences();
                }
                playerComponent.getHudManager().setCustomHud(playerConnectEvent.getPlayerRef(), new EssenceHud(playerConnectEvent.getPlayerRef(), souls));
            }
        });
    }


    @Override
    protected void start() {
        getEntityStoreRegistry().registerSystem(new EssenceSystem.EntityDeath());
        getEntityStoreRegistry().registerSystem(new EssenceSystem.PlayerDeath());
        getEntityStoreRegistry().registerSystem(new EssenceParticleSystem());
    }
}
