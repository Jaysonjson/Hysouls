package json.jayson.hysouls.util;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import json.jayson.hysouls.ui.EssenceHud;

public class EssenceUtil {

    public static final int ESSENCE_LEVEL_CAP = 99999999;

    public static int calculateRequiredEssences(int level) {
        int requiredEssences = (int) ((float)300 * Math.pow(1.03, level));
        if(requiredEssences < 0 || requiredEssences > ESSENCE_LEVEL_CAP) {
            return  ESSENCE_LEVEL_CAP;
        }
        return requiredEssences;
    }

    public static int calculateTotalRequiredEssence(int start, int wanted) {
        int total = 0;
        for (int i = 0; i < wanted; i++) {
            total += calculateRequiredEssences(start + i);
        }
        if(total < 0 || total > ESSENCE_LEVEL_CAP) {
            return  ESSENCE_LEVEL_CAP;
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

    //TODO not hardcode this
    public static boolean isSoulExtractorWeapon(String itemId) {
        return itemId.equalsIgnoreCase("Weapon_Battleaxe_Soul") || itemId.equalsIgnoreCase("Weapon_Daggers_Soul") || itemId.equalsIgnoreCase("Weapon_Mace_Soul") || itemId.equalsIgnoreCase("Weapon_Sword_Soul");
    }
}
