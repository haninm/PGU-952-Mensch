package models;

import java.util.Random;

/**
 * Created by hani on 02/07/2017.
 */
public class Dice {
    public int toss() {
        return new Random().nextInt(6) + 1;
    }
}
