package json.jayson.hysouls;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import json.jayson.hysouls.commands.LevelUpPageCommand;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.components.EssenceStatComponent;
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
        ComponentTypes.ESSENCE_STAT = getEntityStoreRegistry().registerComponent(EssenceStatComponent.class, "EssencesStat", EssenceStatComponent.CODEC);

        getCommandRegistry().registerCommand(new LevelUpPageCommand());
        getEntityStoreRegistry().registerSystem(new EssenceSystem.EntityDeath());
        //TODO: Mainly still just testing
        getEventRegistry().registerGlobal(PlayerConnectEvent.class, playerConnectEvent -> {
            if(playerConnectEvent.getHolder().getComponent(ComponentTypes.ESSENCES) == null) playerConnectEvent.getHolder().addComponent(ComponentTypes.ESSENCES, new EssenceComponent());
            if(playerConnectEvent.getHolder().getComponent(ComponentTypes.ESSENCE_STAT) == null) playerConnectEvent.getHolder().addComponent(ComponentTypes.ESSENCE_STAT, new EssenceStatComponent());

            Player playerComponent = playerConnectEvent.getHolder().getComponent(Player.getComponentType());
            if (playerComponent != null) {
                int souls = 0;
                EssenceComponent essenceComponent = playerConnectEvent.getHolder().getComponent(ComponentTypes.ESSENCES);
                if(essenceComponent != null) {
                    souls = essenceComponent.getEssences();
                }
                System.out.println("Custom HuD");
                playerComponent.getHudManager().setCustomHud(playerConnectEvent.getPlayerRef(), new EssenceHud(playerConnectEvent.getPlayerRef(), souls));
            }
        });
    }



}
