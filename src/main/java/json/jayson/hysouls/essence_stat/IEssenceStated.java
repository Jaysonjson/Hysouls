package json.jayson.hysouls.essence_stat;

import json.jayson.hysouls.Hysouls;

public interface IEssenceStated {

    int getVigor();
    int getEndurance();
    int getMind();
    int getLevel();

    void setVigor(int vigor);
    void setEndurance(int endurance);
    void setMind(int mind);
    default void setLevel(int level) {
        Hysouls.LOGGER.atWarning().log("Tried to set level to something that cant set level");
    }

}
