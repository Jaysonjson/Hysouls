package json.jayson.hysouls;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import json.jayson.hysouls.ui.EssenceHud;

public class DependencyManager {

    public static void applyMultiHud(Player player, PlayerRef playerRef, int souls) {
        MultipleHUD.getInstance().setCustomHud(player, playerRef, "HysoulsHud", new EssenceHud(playerRef, souls));
    }

    public static boolean multiHud() {
        return PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD")) != null;
    }

}
