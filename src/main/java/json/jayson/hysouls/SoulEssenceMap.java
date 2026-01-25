package json.jayson.hysouls;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class SoulEssenceMap {

    private static TreeMap<Integer, String> map = new TreeMap<>(Collections.reverseOrder());


    public static void init() {
        map.put(100000000, "Ingredient_Soul_Essence_Singularity");
        map.put(1000000, "Ingredient_Soul_Essence_Vast");
        map.put(10000, "Ingredient_Soul_Essence_Grand");
        map.put(100, "Ingredient_Soul_Essence_Concentrated");
        map.put(1, "Ingredient_Soul_Essence");

    }

    public static void drop(int essences, Store<EntityStore> store, Vector3d position, CommandBuffer<EntityStore> buffer) {
        for (Pair<String, Integer> pair : SoulEssenceMap.getFor(essences)) {
            ItemStack itemStack = new ItemStack(pair.key(), pair.value());
            Holder<EntityStore> itemEntityHolder = ItemComponent.generateItemDrop(store, itemStack, position, Vector3f.ZERO, 0, 0, 0);
            buffer.addEntity(itemEntityHolder, AddReason.SPAWN);
        }
    }

    public static ArrayList<Pair<String, Integer>> getFor(int essences) {
        ArrayList<Pair<String, Integer>> list = new ArrayList<>();
        for (Integer i : getMap().keySet()) {
            if (essences <= 0) break;

            int quantity = essences / i;
            if (quantity <= 0) continue;

            while (quantity > 0) {
                int stackQuantity = Math.min(100, quantity);
                list.add(new Pair<>(map.get(i), stackQuantity));
                quantity -= stackQuantity;
            }
            essences %= i;
        }
        return list;
    }

    public static void give(Player player, int souls) {
        for (Pair<String, Integer> pair : getFor(souls)) {
            ItemStack itemStack = new ItemStack(pair.key(), pair.value());
            if (player.getInventory().getCombinedEverything().canAddItemStack(itemStack)) {
                player.getInventory().getCombinedEverything().addItemStack(itemStack);
            } else {
                //TODO Drop Item if inventory full
            }
        }
    }


    public static TreeMap<Integer, String> getMap() {
        return map;
    }
}
