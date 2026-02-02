package json.jayson.hysouls.essence_attribute;

import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import java.util.LinkedHashMap;

public class EssenceAttributes {
    private static LinkedHashMap<String, EssenceAttribute> statsMap = new LinkedHashMap<>();

    public static EssenceAttribute VIGOR = add(new EssenceAttribute("Vigor",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 1.2f, DefaultEntityStatTypes::getHealth),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 5f, DefaultEntityStatTypes::getHealth)));

    public static EssenceAttribute ENDURANCE = add(new EssenceAttribute("Endurance",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 0.75f, DefaultEntityStatTypes::getStamina),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 5f, DefaultEntityStatTypes::getStamina)));

    public static EssenceAttribute MIND = add(new EssenceAttribute("Mind",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 1.06f, DefaultEntityStatTypes::getMana),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 1f, DefaultEntityStatTypes::getMana)
            ));

    public static EssenceAttribute DEXTERITY = add(new EssenceAttribute("Dexterity",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 0.5f, DefaultEntityStatTypes::getStamina)));


    public static EssenceAttribute STRENGTH = add(new EssenceAttribute("Strength",
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_BUFF, 1.05f, DefaultEntityStatTypes::getHealth),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.STAT_DEBUFF, 1f, DefaultEntityStatTypes::getHealth),
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.DMG_BUFF, 0.45f)
    ));

    public static EssenceAttribute LUCK = add(new EssenceAttribute("Luck", 35,
            EssenceAttributeModifier.ofPlayer(EssenceAttributeModifier.Type.EXTRA_DROP_CHANCE, 1)));


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
