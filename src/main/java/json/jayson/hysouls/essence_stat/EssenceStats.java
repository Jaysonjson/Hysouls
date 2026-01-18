package json.jayson.hysouls.essence_stat;

import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import java.util.HashMap;

public class EssenceStats {
    private static HashMap<String, EssenceStat> statsMap = new HashMap<>();

    public static EssenceStat VIGOR = add(new EssenceStat("Vigor", 1.8f) {
        @Override
        public void refreshStatType() {
            setDefaultStatType(DefaultEntityStatTypes.getHealth());
        }

        @Override
        public int get(IEssenceStated stated) {
            return stated.getVigor();
        }

        @Override
        public void set(IEssenceStated stated, int value) {
            stated.setVigor(value);
        }

    });

    public static EssenceStat ENDURANCE = add(new EssenceStat("Endurance", 1.2f) {
        @Override
        public void refreshStatType() {
            setDefaultStatType(DefaultEntityStatTypes.getStamina());
        }

        @Override
        public int get(IEssenceStated stated) {
            return stated.getEndurance();
        }

        @Override
        public void set(IEssenceStated stated, int value) {
            stated.setEndurance(value);
        }

    });

    public static EssenceStat MIND = add(new EssenceStat("Mind", 1.4f) {
        @Override
        public void refreshStatType() {
            setDefaultStatType(DefaultEntityStatTypes.getMana());
        }

        @Override
        public int get(IEssenceStated stated) {
            return stated.getMind();
        }

        @Override
        public void set(IEssenceStated stated, int value) {
            stated.setMind(value);
        }
    });



    public static void init() {

    }


    public static HashMap<String, EssenceStat> getStatsMap() {
        return statsMap;
    }


    private static EssenceStat add(EssenceStat stat) {
        getStatsMap().put(stat.getNamed(), stat);
        return stat;
    }
}
