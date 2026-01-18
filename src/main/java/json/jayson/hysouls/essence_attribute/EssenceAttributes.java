package json.jayson.hysouls.essence_attribute;

import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class EssenceAttributes {
    private static LinkedHashMap<String, EssenceAttribute> statsMap = new LinkedHashMap<>();

    public static EssenceAttribute VIGOR = add(new EssenceAttribute("Vigor", 1.8f, 5) {
        @Override
        public void refreshStatType() {
            setDefaultStatType(DefaultEntityStatTypes.getHealth());
        }

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getVigor();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setVigor(value);
        }

    });

    public static EssenceAttribute ENDURANCE = add(new EssenceAttribute("Endurance", 1.2f, 2) {
        @Override
        public void refreshStatType() {
            setDefaultStatType(DefaultEntityStatTypes.getStamina());
        }

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getEndurance();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setEndurance(value);
        }

    });

    public static EssenceAttribute MIND = add(new EssenceAttribute("Mind", 1.4f, 0) {
        @Override
        public void refreshStatType() {
            setDefaultStatType(DefaultEntityStatTypes.getMana());
        }

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getMind();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setMind(value);
        }
    });



    public static void init() {

    }


    public static LinkedHashMap<String, EssenceAttribute> getAttributeMap() {
        return statsMap;
    }


    private static EssenceAttribute add(EssenceAttribute stat) {
        getAttributeMap().put(stat.getNamed(), stat);
        return stat;
    }
}
