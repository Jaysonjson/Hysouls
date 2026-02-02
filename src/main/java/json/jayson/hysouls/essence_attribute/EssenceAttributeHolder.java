package json.jayson.hysouls.essence_attribute;

import json.jayson.hysouls.Hysouls;

import java.util.HashMap;

public interface EssenceAttributeHolder {


    HashMap<String, Integer> getEssenceAttributes();


    default void addDefaultEssenceAttributes(int def) {
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            setEssenceAttribute(value.getNamed(), def);
        }
    }

    default int getEssenceAttribute(String attribute) {
        return getEssenceAttributes().getOrDefault(attribute, 0);
    }

    default void setEssenceAttribute(String attribute, int value) {
        getEssenceAttributes().put(attribute, value);
    }

    int getLevel();
    default void setLevel(int level) {
        Hysouls.LOGGER.atWarning().log("Tried to set level to something that cant set level");
    }

}
