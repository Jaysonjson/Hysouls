package json.jayson.hysouls.essence_attribute;

import java.util.function.Supplier;

public class EssenceAttributeModifier {

    private Type type;
    private Target target;
    private float value;
    private Supplier<Integer> statIndex;

    public EssenceAttributeModifier(Type type, Target target, float value, Supplier<Integer> statIndex) {
        this.type = type;
        this.target = target;
        this.value = value;
        this.statIndex = statIndex;
    }


    public static EssenceAttributeModifier ofPlayer(Type type, float value, Supplier<Integer> statIndex) {
        return new EssenceAttributeModifier(type, Target.PLAYER, value, statIndex);
    }


    public static EssenceAttributeModifier ofPlayer(Type type, float value) {
        return new EssenceAttributeModifier(type, Target.PLAYER, value, EssenceAttributes::emptyStat);
    }

    public static EssenceAttributeModifier ofItem(Type type, float value,Supplier<Integer> statIndex) {
        return new EssenceAttributeModifier(type, Target.ITEM, value, statIndex);
    }

    public int getStatIndex() {
        return statIndex.get();
    }

    public float getValue() {
        return value;
    }

    public Target getTarget() {
        return target;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        STAT_BUFF,
        STAT_DEBUFF,
        DMG_BUFF
    }

    public enum Target {
        PLAYER,
        ITEM
    }

}
