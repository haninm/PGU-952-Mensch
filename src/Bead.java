/**
 * Created by hani on 02/07/2017.
 *
 * final Stage : Arrive Bead in Goal Positions
 */
public class Bead {

    private boolean inGame;
    private boolean inFinalStage;
    private Player player;
    private int numberInPlayerSet;


    //region Getters And Setters
    public boolean isInGame() {
        return inGame;
    }


    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isInFinalStage() {
        return inFinalStage;
    }

    public void setInFinalStage(boolean inFinalStage) {
        this.inFinalStage = inFinalStage;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getNumberInPlayerSet() {
        return numberInPlayerSet;
    }

    public void setNumberInPlayerSet(int numberInPlayerSet) {
        this.numberInPlayerSet = numberInPlayerSet;
    }
    //endregion
}
