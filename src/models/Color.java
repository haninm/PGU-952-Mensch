package models;

/**
 * Created by hani on 02/07/2017.
 */
public enum Color {
    YELLOW, RED, BLUE, GREEN;

    public Color next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

}
