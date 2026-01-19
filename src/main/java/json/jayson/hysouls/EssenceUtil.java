package json.jayson.hysouls;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import json.jayson.hysouls.ui.EssenceHud;

public class EssenceUtil {

    public static int calculateRequiredEssences(int level) {
        return (int) ((float)325 * Math.pow(1.03, level));
    }

    public static int calculateTotalRequiredEssence(int start, int wanted) {
        int total = 0;
        for (int i = 0; i < wanted; i++) {
            total += calculateRequiredEssences(start + i);
        }
        return total;
    }

    public static void applyHud(Player player, PlayerRef playerRef, int souls) {
        if(DependencyManager.multiHud()) {
            DependencyManager.applyMultiHud(player, playerRef, souls);
        } else {
            player.getHudManager().setCustomHud(playerRef, new EssenceHud(playerRef, souls));
        }
    }

}
