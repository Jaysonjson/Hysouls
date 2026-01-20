package json.jayson.hysouls.weapon_scaling;

import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import json.jayson.hysouls.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


//Temporary until I figure otu a better way to store the info inside the item
public class WeaponScalingMap {

    private static HashMap<String, Scaling> scalingMap = new  HashMap<>();

    private static String[] materials = new String[] {"Iron", "Thorium", "Cobalt", "Adamantite", "Mithril"};

    //TODO: Allow changes via server config/json (or both)
    public static void init() {
        for (String material : materials) {
            scalingMap.put("Weapon_Sword_" + material, new Scaling(new Pair<>(ScalingType.D, EssenceAttributes.DEXTERITY), new Pair<>(ScalingType.C, EssenceAttributes.STRENGTH)));
            scalingMap.put("Weapon_Mace_" + material, new Scaling(new Pair<>(ScalingType.D, EssenceAttributes.DEXTERITY), new Pair<>(ScalingType.A, EssenceAttributes.STRENGTH)));
            scalingMap.put("Weapon_Battleaxe_" + material, new Scaling(new Pair<>(ScalingType.D, EssenceAttributes.DEXTERITY), new Pair<>(ScalingType.A, EssenceAttributes.STRENGTH)));
            scalingMap.put("Weapon_Daggers_" + material, new Scaling(new Pair<>(ScalingType.A, EssenceAttributes.DEXTERITY), new Pair<>(ScalingType.D, EssenceAttributes.STRENGTH)));
        }
    }

    public static HashMap<String, Scaling> getScalingMap() {
        return scalingMap;
    }

    public static Scaling getScaling(String itemId) {
        return scalingMap.get(itemId);
    }

    public static class Scaling {
        private HashMap<String, ScalingType> map = new HashMap<>();

        @SafeVarargs
        public Scaling(Pair<ScalingType, EssenceAttribute>... pairs) {
            for (Pair<ScalingType, EssenceAttribute> pair : pairs) {
                map.put(pair.value().getNamed(), pair.key());
            }
        }

        @Nullable
        public ScalingType get(EssenceAttribute attribute) {
            return map.get(attribute.getNamed());
        }

    }

    public enum ScalingType {
        A(0.4f),
        B(0.3f),
        C(0.2f),
        D(0.1f);


        float modifier = 1f;
        ScalingType(float modifier) {
            this.modifier = modifier;
        }

        public float getModifier() {
            return modifier;
        }
    }

}
