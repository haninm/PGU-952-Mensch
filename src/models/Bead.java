package models;

/**
 * Created by hani on 02/07/2017.
 *
 * final Stage : Arrive models.Bead in Goal Positions
 * point : point in each
 * positionFromPlayer : Each bead in View Of models.Player Can be On only 44 Position ,  [1->40] in Normal Play , [41->44] for Final Positions ( Goals ) and 0 for Waiting Position
 */
public class Bead {

    private boolean inGame;
    private boolean inFinalStage;
    private Player player;
    private int numberInPlayerSet;
    private int point ;
    private int positionFromPlayer;


    public Bead( Player player, int numberInPlayerSet) {
        this.inGame = false;
        this.inFinalStage = false;
        this.player = player;
        this.numberInPlayerSet = numberInPlayerSet;
        this.point = 0;
        this.positionFromPlayer = 0;
    }

    public void increasePoint(int point){
        this.point +=point;
    }



    //region Getters And Setters


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPositionFromPlayer() {
        return positionFromPlayer;
    }

    public void setPositionFromPlayer(int positionFromPlayer) {
        this.positionFromPlayer = positionFromPlayer;
    }

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
