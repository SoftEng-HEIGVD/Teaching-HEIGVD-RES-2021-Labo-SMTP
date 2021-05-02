package utils;

import java.util.Random;

public class Utils {

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randNum = rand.nextInt(max - min) + min;
        return randNum;
    }

    public static int randInt(int max) {
        return randInt(0, max);
    }
}
