package json.jayson.hysouls.essence_attribute;

import json.jayson.hysouls.Hysouls;

public interface EssenceAttributeHolder {

    int getVigor();
    int getEndurance();
    int getMind();
    int getLevel();
    int getDexterity();
    int getStrength();

    void setVigor(int vigor);
    void setEndurance(int endurance);
    void setMind(int mind);
    void setDexterity(int dexterity);
    void setStrength(int strength);

    default void setLevel(int level) {
        Hysouls.LOGGER.atWarning().log("Tried to set level to something that cant set level");
    }

}
