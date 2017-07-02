package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by han on 7/3/2017.
 */
public class ObservablePlayer {
    private Player player;
    private final StringProperty name;

    public ObservablePlayer(Player player) {
        this.player = player;
        name = new SimpleStringProperty(player.getName());
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
