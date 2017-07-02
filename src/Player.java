import com.sun.javafx.css.Rule;

import java.util.List;

/**
 * Created by hani on 02/07/2017.
 * complete :
 */
public class Player {

    private boolean cpu;
    private List<Bead> beads;
    private String name;
    private boolean complete;
    private Color color;
    private int lastRank;
    private Board board;


    public Player(boolean isCpu, List<Bead> beads, String name, Color color , Board board) throws Exception {
        setCpu(cpu);
        setBeads(beads);
        setName(name);
        setBoard(board);
        this.complete = false;
        setColor(color);
    }



    //region Getter And Setters


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getLastRank() {
        return lastRank;
    }

    public void setLastRank(int lastRank) {
        this.lastRank = lastRank;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) throws Exception {
        for (Player p : board.getPlayers()){
            if(p!= this){
                if ( p.getColor() == color){
                    throw  new Exception("Player color must be Unique");
                }
            }
        }
        this.color = color;
    }

    public boolean isCpu() {
        return cpu;
    }

    public void setCpu(boolean cpu) {
        this.cpu = cpu;
    }

    public List<Bead> getBeads() {
        return beads;
    }

    public void setBeads(List<Bead> beads) throws Exception {
//        if(beads == null){
//            this.beads = beads;
//        }
//        if (beads.size() != Rules.numberOfPlayerBeads) {
//            throw new Exception("Number Of Beads is to Low or To High , number of Player Beads is : " + Rules.numberOfPlayerBeads);
//        }
        this.beads = beads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    //endregion
}
