package json.jayson.hysouls.essence_attribute;

import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import java.util.LinkedHashMap;

public class EssenceAttributes {
    private static LinkedHashMap<String, EssenceAttribute> statsMap = new LinkedHashMap<>();

    public static EssenceAttribute VIGOR = add(new EssenceAttribute("Vigor",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 1.75f, DefaultEntityStatTypes::getHealth),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 5f, DefaultEntityStatTypes::getHealth)) {

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getVigor();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setVigor(value);
        }

    });

    public static EssenceAttribute ENDURANCE = add(new EssenceAttribute("Endurance",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 1.2f, DefaultEntityStatTypes::getStamina),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 5f, DefaultEntityStatTypes::getStamina)) {

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getEndurance();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setEndurance(value);
        }

    });

    public static EssenceAttribute MIND = add(new EssenceAttribute("Mind",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 2.1f, DefaultEntityStatTypes::getMana),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 1f, DefaultEntityStatTypes::getMana)
            ) {

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getMind();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setMind(value);
        }

    });

    public static EssenceAttribute DEXTERITY = add(new EssenceAttribute("Dexterity",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 1.1f, DefaultEntityStatTypes::getStamina),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 1f, DefaultEntityStatTypes::getStamina),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.DMG_BUFF, 0.15f)
            ) {

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getDexterity();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setDexterity(value);
        }

    });


    public static EssenceAttribute STRENGTH = add(new EssenceAttribute("Strength",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 1.05f, DefaultEntityStatTypes::getHealth),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 1f, DefaultEntityStatTypes::getHealth),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.DMG_BUFF, 0.35f)
    ) {

        @Override
        public int get(EssenceAttributeHolder stated) {
            return stated.getStrength();
        }

        @Override
        public void set(EssenceAttributeHolder stated, int value) {
            stated.setStrength(value);
        }

    });


    public static int emptyStat() {
        return -1;
    }


    public static void init() {
    }


    public static LinkedHashMap<String, EssenceAttribute> getAttributeMap() {
        return statsMap;
    }

    public static EssenceAttribute getAttribute(String attributeName) {
        return statsMap.get(attributeName.toLowerCase());
    }

    private static EssenceAttribute add(EssenceAttribute stat) {
        getAttributeMap().put(stat.getNamed().toLowerCase(), stat);
        return stat;
    }
}
