package json.jayson.hysouls;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.SoulsComponent;
import json.jayson.hysouls.systems.SoulSystem;
import json.jayson.hysouls.ui.SoulsHud;

public class Hysouls extends JavaPlugin {

    public static String VERSION = "0.0.0";
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public Hysouls(JavaPluginInit init) {
        super(init);
        VERSION = getManifest().getVersion().toString();
    }

    @Override
    protected void setup() {

        ComponentTypes.soulsComponentType = getEntityStoreRegistry().registerComponent(SoulsComponent.class, "Souls", SoulsComponent.CODEC);

        getEntityStoreRegistry().registerSystem(new SoulSystem());

        //TODO: Mainly still just testing
        getEventRegistry().registerGlobal(PlayerConnectEvent.class, playerConnectEvent -> {
            if(playerConnectEvent.getHolder().getComponent(ComponentTypes.soulsComponentType) == null) {
                playerConnectEvent.getHolder().addComponent(ComponentTypes.soulsComponentType, new SoulsComponent());
            }
            Player playerComponent = playerConnectEvent.getHolder().getComponent(Player.getComponentType());
            if (playerComponent != null) {
                int souls = 0;
                SoulsComponent soulsComponent = playerConnectEvent.getHolder().getComponent(ComponentTypes.soulsComponentType);
                if(soulsComponent != null) {
                    souls = soulsComponent.getSouls();
                }
                playerComponent.getHudManager().setCustomHud(playerConnectEvent.getPlayerRef(), new SoulsHud(playerConnectEvent.getPlayerRef(), souls));
            }
        });

    }
}
