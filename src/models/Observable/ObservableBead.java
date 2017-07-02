package models.Observable;

import javafx.beans.property.*;
import models.Bead;
import models.Player;

/**
 * Created by han on 7/3/2017.
 */
public class ObservableBead {

    private final Bead bead;

    private final StringProperty name;
    private final IntegerProperty inBoardPosition;
    private final IntegerProperty position;
    private  final BooleanProperty isComplate;
    private  final BooleanProperty isInGame;



    public ObservableBead(Bead bead) {
        this.bead = bead;
        name = new SimpleStringProperty(bead.toString());
        inBoardPosition = new SimpleIntegerProperty(bead.getPlayer().getBoard().findBeadPositionFromBoard(bead));
        position = new SimpleIntegerProperty(bead.getPositionFromPlayer());
        isComplate = new SimpleBooleanProperty(bead.isInFinalStage());
        isInGame = new SimpleBooleanProperty(bead.isInGame());
    }


    public boolean isIsInGame() {
        return isInGame.get();
    }

    public BooleanProperty isInGameProperty() {
        return isInGame;
    }

    public void setIsInGame(boolean isInGame) {
        this.isInGame.set(isInGame);
    }

    public boolean isIsComplate() {
        return isComplate.get();
    }

    public BooleanProperty isComplateProperty() {
        return isComplate;
    }

    public void setIsComplate(boolean isComplate) {
        this.isComplate.set(isComplate);
    }

    public Bead getBead() {
        return bead;
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

    public int getInBoardPosition() {
        return inBoardPosition.get();
    }

    public IntegerProperty inBoardPositionProperty() {
        return inBoardPosition;
    }

    public void setInBoardPosition(int inBoardPosition) {
        this.inBoardPosition.set(inBoardPosition);
    }

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }
}
