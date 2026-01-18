package json.jayson.hysouls;

public class EssenceUtil {

    public static int calculateRequiredEssences(int level) {
        return (int) ((float)325 * Math.pow(1.03, level));
    }

    public static int calculateTotalRequiredEssence(int start, int wanted) {
        int total = 0;
        for (int i = 0; i < wanted; i++) {
            total += calculateRequiredEssences(start + i);
        }
        return total;
    }

}
