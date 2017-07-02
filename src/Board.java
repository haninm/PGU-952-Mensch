import org.jetbrains.annotations.Nullable;

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hani on 02/07/2017.
 * this is Class for Playing Mensch , each Board have 4 ( in default ) Player and Each player have 4 ( in Default) Bead
 * <p>
 * positions: Each positions beads can be placed  [1 -> 56] , each Position can Have One bead , Except Start points ( 1 , 11 , 21 ,31 ) can have Multiple Of One Player ! and this Array Show Last One ! , position start from Yellow Player !
 */
public class Board {

    private List<Player> players;
    private Color turn;
    private Dice dice;
    private List<Player> table;
    private Bead[] positions;

    public Board() throws Exception {
        turn = Color.YELLOW;

        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player(true, new ArrayList<>(), Color.values()[i].toString(), Color.values()[i], this));
            for (int j = 0; j < 4; j++) {
                players.get(i).getBeads().add(new Bead(players.get(i), j));
            }
        }

        this.dice = new Dice();
        this.table = new ArrayList<>();
        this.positions = new Bead[57];
    }


    public void playGame() throws Exception {

        System.out.println("is " + turn + " turn");

        int toss = dice.toss();
        System.out.println("toss is " + toss);

        Player player = findPlayerForPlay();
        if (player == null) {
            print();
            throw new Exception("Error in Finding Players Turn");
        }
        Bead bead = findPlayerBeadForPlay(player, toss);
        if (bead == null) { // can find Bead
            System.out.println("sorry you cant move !");
            nextTurn();
            print();
            return;
        }

        move(bead, toss);

        print();
        if (toss != 6)
            nextTurn();

    }


    private void print() {
        System.out.println("----------------------------------------");
        for (Player p : players) {
            for (Bead b : p.getBeads()) {
                System.out.println("player : " + p.getName() + " - " + b.getNumberInPlayerSet()
                        + " position :" + b.getPositionFromPlayer() + "( " + findBeadPositionFromBoard(b) + ")");
            }
        }
        System.out.println("----------------------------------------");
        for (Player p : players) {
            for (Bead b : p.getBeads()) {
                if (b.isInGame()) {
                    System.out.println("player : " + p.getName() + " - " + b.getNumberInPlayerSet()
                            + " position :" + b.getPositionFromPlayer() + "( " + findBeadPositionFromBoard(b) + ")");
                }
            }
        }
        System.out.println("----------------------------------------");

    }


    @Nullable
    private Player findPlayerForPlay() {
        for (Player p : getPlayers()) {
            if (p.getColor() == getTurn())
                return p;
        }
        return null;
    }


    /**
     * Smart for Choice best Bead for Move !
     * if return null , player cant move any thing!
     *
     * @param player
     * @param toss
     * @return
     */
    @Nullable
    private Bead findPlayerBeadForPlay(Player player, int toss) {

        for (Bead bead : player.getBeads()) {

            if (bead.getPositionFromPlayer() == 44) { // bead Cannot be moved !
                bead.setPoint(0);
                continue;
            }


            // region Bead Starting Game
            // Bead is in Waiting Position and Want 6 to play game ( move to position 1 )
            if (bead.getPositionFromPlayer() == 0) {
                bead.setPoint(0);
                if (toss == 6) {
                    bead.increasePoint(Rules.beadStartGamePoint);
                    continue;
                } else {
                    continue;
                }
            }
            //endregion


            //region choice In Game Bead
            bead.setPoint(bead.getPositionFromPlayer());
            int beadPositionAfterMoveFromPlayer = bead.getPositionFromPlayer() + toss;
            int beadPositionAfterMoveFromBoard
                    = findBeadPositionFromBoard(bead.getPositionFromPlayer() + toss, bead.getPlayer());


            if (beadPositionAfterMoveFromPlayer >= 1
                    && beadPositionAfterMoveFromPlayer <= 40
                    && positions[beadPositionAfterMoveFromBoard] == null) { // Free Move !
                bead.increasePoint(Rules.beadFreeMoveGamePoint);
            } else if (beadPositionAfterMoveFromPlayer >= 1
                    && beadPositionAfterMoveFromPlayer <= 40
                    && isEnemyInPosition(beadPositionAfterMoveFromBoard, bead)) { // KillEnemyMove
                bead.increasePoint(Rules.beadKillEnemyMovePoint);
            } else if (beadPositionAfterMoveFromPlayer >= 1
                    && beadPositionAfterMoveFromPlayer <= 40
                    && !isEnemyInPosition(beadPositionAfterMoveFromBoard, bead)) { // Moved Position is filled by Friend
                bead.setPoint(0);
            } else if (beadPositionAfterMoveFromPlayer > 40
                    && positions[beadPositionAfterMoveFromBoard] == null) { // Go to free Goal position!
                if (bead.getPositionFromPlayer() > 40) { // Goal to Goal !
                    bead.increasePoint(Rules.beadFreeMoveInGoalPoint);
                } else { // Go to Goal
                    bead.increasePoint(Rules.beadGoToGoalMovePoint);
                }
            } else if (beadPositionAfterMoveFromPlayer > 40
                    && positions[beadPositionAfterMoveFromBoard] != null) { // bead can Go to Goal but is not empty
                bead.setPoint(0);
            }
            // endregion

        }

        //region Fin response ( Max Point Bead )

        boolean canChoiceBead = false; // if All Point is 0 , player cant play

        Bead res = player.getBeads().get(0);
        for (Bead b : player.getBeads()) {
            if (b.getPoint() > 0) {
                canChoiceBead = true;
                if (b.getPoint() > res.getPoint())
                    res = b;
            }

        }
        if (canChoiceBead)
            return res;
        else
            return null;
        //endregion
    }


    private boolean moveBeadTo(Bead bead, int newBeadPositionFromPlayer, int newBeadPositionFromBoard) {


        System.out.println("lets get Move !");

        if (newBeadPositionFromPlayer == 1) { //first move
            System.out.println("player : " + bead.getPlayer().getName() + " Join New Bead :" + bead.getNumberInPlayerSet());
            bead.setInGame(true);
            if (isEnemyInPosition(newBeadPositionFromBoard, bead)) { // if in start position has enemy
                return moveBeadToAndKillEnemy(bead, newBeadPositionFromPlayer, newBeadPositionFromBoard);
            }
            positions[newBeadPositionFromBoard] = bead;
            bead.setPositionFromPlayer(newBeadPositionFromPlayer);
            return true;
        } else {
            if (isEnemyInPosition(newBeadPositionFromBoard, bead)) { // if in move  position has enemy
                return moveBeadToAndKillEnemy(bead, newBeadPositionFromPlayer, newBeadPositionFromBoard);
            }

            System.out.println("player : " + bead.getPlayer().getName() + " bead :" + bead.getNumberInPlayerSet()
                    + " move From :" + bead.getPositionFromPlayer() + " to : " + newBeadPositionFromPlayer + "(" +
                    newBeadPositionFromBoard + ")");


            int oldBeadPositionFromBoard = findBeadPositionFromBoard(bead);
            positions[oldBeadPositionFromBoard] = null;
            positions[newBeadPositionFromBoard] = bead;
            bead.setPositionFromPlayer(newBeadPositionFromPlayer);
            if (newBeadPositionFromPlayer > 40) {

                bead.setInFinalStage(true);
                System.out.println("player : " + bead.getPlayer().getName() + " bead : "
                        + bead.getNumberInPlayerSet() + " COmplete ! ");
                checkPlayerIsWinn(bead.getPlayer());
            }
            return true;
        }

    }


    private boolean checkAllBeadsIsInFinalStage(Player player) {
        for (Bead b : player.getBeads()) {
            if (!b.isInFinalStage()) {
                return false;
            }
        }
        return true;
    }

    private void checkPlayerIsWinn(Player player) {
        if (checkAllBeadsIsInFinalStage(player)) {
            table.add(player);
            players.remove(player);
            player.setComplete(true);
            System.out.println("player : " + player.getName() + "COmplete ! ");
        }
    }

    private boolean moveBeadToAndKillEnemy(Bead bead, int newBeadPositionFromPlayer, int newBeadPositionFromBoard) {
        if (!isEnemyInPosition(newBeadPositionFromBoard, bead))
            return false;
        if (checkIsEnemySafeHouse(bead, newBeadPositionFromBoard)) { // if enemy is in safe house , not kill !
            newBeadPositionFromPlayer += 1;
            newBeadPositionFromBoard = findBeadPositionFromBoard(newBeadPositionFromPlayer, bead.getPlayer());
            return moveBeadTo(bead, newBeadPositionFromPlayer, newBeadPositionFromBoard);
        }
        Bead enemy = positions[newBeadPositionFromBoard];
        int oldBeadPositionFromBoard = findBeadPositionFromBoard(bead);
        positions[oldBeadPositionFromBoard] = null;
        positions[newBeadPositionFromBoard] = bead;
        bead.setPositionFromPlayer(newBeadPositionFromPlayer);
        enemy.setPositionFromPlayer(0);
        enemy.setInGame(false);
        System.out.println("player : " + bead.getPlayer().getName() + " you kill Enemy in position "
                + newBeadPositionFromBoard + " enemy is :" + enemy.getPlayer().getName() + " - " + enemy.getNumberInPlayerSet());

        return true;
    }


    /**
     * @param bead
     * @param newBeadPositionFromBoard
     * @return true if this is enemy safe house
     */
    private boolean checkIsEnemySafeHouse(Bead bead, int newBeadPositionFromBoard) {

        if (!isEnemyInPosition(newBeadPositionFromBoard, bead))
            return false;

        Bead enemy = positions[newBeadPositionFromBoard];
        switch (enemy.getPlayer().getColor()) {
            case YELLOW:
                if (newBeadPositionFromBoard == 1) {
                    System.out.println("enemy Bead is in safe House");

                    return true;
                }
                return false;
            case RED:
                if (newBeadPositionFromBoard == 11) {
                    System.out.println("enemy Bead is in safe House");

                    return true;
                }
                return false;
            case BLUE:
                if (newBeadPositionFromBoard == 21) {
                    System.out.println("enemy Bead is in safe House");

                    return true;
                }
                return false;
            case GREEN:
                if (newBeadPositionFromBoard == 31) {
                    System.out.println("enemy Bead is in safe House");
                    return true;
                }
                return false;
            default:
                return false;

        }

    }


    private boolean move(Bead bead, int toss) {

        int newBeadPositionFromPlayer;
        int newBeadPositionFromBoard;


        // region is first Bead Move
        if (bead.getPositionFromPlayer() == 0) { // first bead move !


            newBeadPositionFromPlayer = 1;
            newBeadPositionFromBoard = findBeadPositionFromBoard(1, bead.getPlayer());

            if (toss == 6) {
                return moveBeadTo(bead, newBeadPositionFromPlayer, newBeadPositionFromBoard);
            } else {
                System.out.println("You cant Move , your Toss isn't 6");
                return false;
            }


        }
        //endregion


        newBeadPositionFromPlayer = bead.getPositionFromPlayer() + toss;
        newBeadPositionFromBoard = findBeadPositionFromBoard(bead.getPositionFromPlayer() + toss, bead.getPlayer());

        if (positions[newBeadPositionFromBoard] == null) {
            return moveBeadTo(bead, newBeadPositionFromPlayer, newBeadPositionFromBoard);
        } else {
            return moveBeadToAndKillEnemy(bead, newBeadPositionFromPlayer, newBeadPositionFromBoard);
        }
    }

    /**
     * Go to Next Turn
     */
    private void nextTurn() {
        this.turn = turn.next();
    }


    /**
     * @param positionFromBoard
     * @param bead
     * @return true : if in @param positionFromBoard is enemy bead , false : if positionFromBoard is null or Friend
     */
    private boolean isEnemyInPosition(int positionFromBoard, Bead bead) {

        if (positions[positionFromBoard] == null) // position is Empty
            return false;


        for (Bead b : bead.getPlayer().getBeads()) {
            if (positions[positionFromBoard] == b) { // is Friend Bead
                return false;
            }
        }
        return true;

    }


    /**
     * Convert Position From the point of view Player  to  From the point of view Board
     *
     * @param bead
     * @return bead position From the point of view Board
     */
    private int findBeadPositionFromBoard(Bead bead) {
        return this.findBeadPositionFromBoard(bead.getPositionFromPlayer(), bead.getPlayer());
    }

    /**
     * Convert Position From the point of view Player  to  From the point of view Board
     *
     * @param position
     * @param player
     * @return bead position From the point of view Board
     */
    private int findBeadPositionFromBoard(int position, Player player) {
        if (position == 0)
            return 0;

        switch (player.getColor()) {
            case YELLOW:
                return position; // Position Form Yellow Player and Board is same ( By Default)
            case RED:
                if (position <= 40) { // bead is not in Goal Positions
                    int res = position + 10; // 10 is : Distance between First Cell and First Red Cell
                    if (res > 40) // res is out of range
                        return res - 40;
                    else
                        return res;
                } else
                    return position + 4; // 4 is : Distance between First Yellow Goal and First RED Goal
            case BLUE:
                if (position <= 40) { // bead is not in Goal Positions
                    int res = position + 20; // 20 is : Distance between First Cell and First BLUE Cell
                    if (res > 40) // res is out of range
                        return res - 40;
                    else
                        return res;
                } else
                    return position + 8; // 8 is : Distance between First Yellow Goal and First BLUE Goal
            case GREEN:
                if (position <= 40) { // bead is not in Goal Positions
                    int res = position + 30; // 30 is : Distance between First Cell and First GREEN Cell
                    if (res > 40) // res is out of range
                        return res - 40;
                    else
                        return res;
                } else
                    return position + 12; // 12 is : Distance between First Yellow Goal and First GREEN Goal
            default:
                return -1;

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
