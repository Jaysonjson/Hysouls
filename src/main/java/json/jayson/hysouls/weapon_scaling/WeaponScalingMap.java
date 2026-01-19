package json.jayson.hysouls.weapon_scaling;

import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import json.jayson.hysouls.util.Pair;

import java.util.HashMap;


//Temporary until I figure otu a better way to store the info inside the item
public class WeaponScalingMap {

    private static HashMap<String, Scaling> scalingMap = new  HashMap<>();


    public static void init() {
        scalingMap.put("Weapon_Sword_Iron", new Scaling(new Pair<>(ScalingType.D, EssenceAttributes.DEXTERITY), new Pair<>(ScalingType.C, EssenceAttributes.STRENGTH)));
    }

    public static HashMap<String, Scaling> getScalingMap() {
        return scalingMap;
    }

    public static Scaling getScaling(String itemId) {
        return scalingMap.get(itemId);
    }

    public static class Scaling {
        private HashMap<ScalingType, String> map = new HashMap<>();

        @SafeVarargs
        public Scaling(Pair<ScalingType, EssenceAttribute>... pairs) {
            for (Pair<ScalingType, EssenceAttribute> pair : pairs) {
                map.put(pair.key(), pair.value().getNamed());
            }
        }

    }

    enum ScalingType {
        A(1.18f),
        B(1.15f),
        C(1.12f),
        D(1f);


        float modifier = 1f;
        ScalingType(float modifier) {
            this.modifier = modifier;
        }

        public float getModifier() {
            return modifier;
        }
    }

}
