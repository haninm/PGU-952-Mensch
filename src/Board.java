import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hani on 02/07/2017.
 * this is Class for Playing Mensch , each Board have 4 ( in default ) Player and Each player have 4 ( in Default) Bead
 *
 * positions: Each positions beads can be placed ! 1 to 56
 *
 */
public class Board {

    private List<Player> players;
    private Color turn;
    private Dice dice;
    private List<Player> table;
    private Bead[] positions;

    //TODO : Constructor With make Objects

    public void playGame() throws Exception {
        int toss = dice.toss();

        Player player = findPlayerForPlay();
        if (player == null)
            throw  new  Exception("Error in Finding Players Turn");


    }

    @Nullable
    private Player findPlayerForPlay(){
        for (Player p : getPlayers()){
            if(p.getColor() == getTurn())
                return  p;
        }
        return  null;
    }




    private Bead findPlayerBeadForPlay (Player player , int toss){

        for(Bead bead : player.getBeads()){

        }



    }


    // region Getter and Setters


    public Color getTurn() {
        return turn;
    }

    public void setTurn(Color turn) {
        this.turn = turn;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public List<Player> getTable() {
        return table;
    }

    public void setTable(List<Player> table) {
        this.table = table;
    }

    public Bead[] getPositions() {
        return positions;
    }

    public void setPositions(Bead[] positions) {
        this.positions = positions;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Bead> getBeads() {
        List<Bead> res = new ArrayList<Bead>();
        for (Player p : players) {
            res.addAll(p.getBeads());
        }
        return res;
    }
    //endregion
}
