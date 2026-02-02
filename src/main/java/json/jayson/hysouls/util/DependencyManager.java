package json.jayson.hysouls.util;

import com.buuz135.mhud.MultipleCustomUIHud;
import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import json.jayson.hysouls.ui.EssenceHud;
import org.jetbrains.annotations.Nullable;

public class DependencyManager {

    public static void applyMultiHud(Player player, PlayerRef playerRef, int souls) {
        MultipleHUD.getInstance().setCustomHud(player, playerRef, "HysoulsHud", new EssenceHud(playerRef, souls));
    }

    @Nullable
    public static EssenceHud getEssenceHud(Player player, PlayerRef playerRef) {
        if(player.getHudManager().getCustomHud() instanceof EssenceHud essenceHud) {
            return essenceHud;
        } else if(multiHud()) {
            if(player.getHudManager().getCustomHud() instanceof MultipleCustomUIHud multipleCustomUIHud) {
                multipleCustomUIHud.remove("HysoulsHud");
                EssenceHud essenceHud = new EssenceHud(playerRef, 0);
                multipleCustomUIHud.add("HysoulsHud", essenceHud);
                return essenceHud;
            }
        }
        return null;
    }

    public static boolean multiHud() {
        return PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD")) != null;
    }

}
