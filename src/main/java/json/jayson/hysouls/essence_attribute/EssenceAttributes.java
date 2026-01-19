package json.jayson.hysouls.essence_attribute;

import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import java.util.LinkedHashMap;

public class EssenceAttributes {
    private static LinkedHashMap<String, EssenceAttribute> statsMap = new LinkedHashMap<>();

    public static EssenceAttribute VIGOR = add(new EssenceAttribute("Vigor") {
        @Override
        public void initialize() {
            setModifierType(0, 1.8f, ModifierType.BUFF);
            setModifierType(0, 5f, ModifierType.DEBUFF);
        }

        @Override
        public void refreshStatType() {
            setDefaultStatType(0, DefaultEntityStatTypes.getHealth());
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

    public static EssenceAttribute ENDURANCE = add(new EssenceAttribute("Endurance") {
        @Override
        public void initialize() {
            setModifierType(0, 1.2f, ModifierType.BUFF);
            setModifierType(0, 2f, ModifierType.DEBUFF);

            setModifierType(1, 0.25f, ModifierType.BUFF);
        }

        @Override
        public void refreshStatType() {
            setDefaultStatType(0, DefaultEntityStatTypes.getStamina());
            setDefaultStatType(1, DefaultEntityStatTypes.getOxygen());
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

    public static EssenceAttribute MIND = add(new EssenceAttribute("Mind") {
        @Override
        public void initialize() {
            setModifierType(0, 1.4f, ModifierType.BUFF);
            setModifierType(0, 0, ModifierType.DEBUFF);
        }

        @Override
        public void refreshStatType() {
            setDefaultStatType(0, DefaultEntityStatTypes.getMana());
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
