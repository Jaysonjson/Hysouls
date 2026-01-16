package json.jayson.hysouls;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.systems.EssenceSystem;
import json.jayson.hysouls.ui.EssenceHud;

public class Hysouls extends JavaPlugin {

    public static String VERSION = "0.0.0";
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public Hysouls(JavaPluginInit init) {
        super(init);
        VERSION = getManifest().getVersion().toString();
    }

    @Override
    protected void setup() {

        ComponentTypes.ESSENCES = getEntityStoreRegistry().registerComponent(EssenceComponent.class, "Essences", EssenceComponent.CODEC);

        getEntityStoreRegistry().registerSystem(new EssenceSystem());
        //TODO: Mainly still just testing
        getEventRegistry().registerGlobal(PlayerConnectEvent.class, playerConnectEvent -> {
            if(playerConnectEvent.getHolder().getComponent(ComponentTypes.ESSENCES) == null) {
                playerConnectEvent.getHolder().addComponent(ComponentTypes.ESSENCES, new EssenceComponent());
            }
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



}
