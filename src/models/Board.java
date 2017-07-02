package models;


import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hani on 02/07/2017.
 * this is Class for Playing Mensch , each models.Board have 4 ( in default ) models.Player and Each player have 4 ( in Default) models.Bead
 * <p>
 * positions: Each positions beads can be placed  [1 -> 56] , each Position can Have One bead , Except Start points ( 1 , 11 , 21 ,31 ) can have Multiple Of One models.Player ! and this Array Show Last One ! , position start from Yellow models.Player !
 */
public class Board {

    private List<Player> players;
    private Color turn;
    private Dice dice;
    private List<Player> table;
    private Bead[] positions;
    private StringBuilder message;
    private boolean isOver;


    public Board() throws Exception {
        message = new StringBuilder();
        turn = Color.YELLOW;
        isOver = false;
        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player(true, new ArrayList<>(),Rules.playerNames[i], Color.values()[i], this));
            for (int j = 0; j < 4; j++) {
                players.get(i).getBeads().add(new Bead(players.get(i), j));
            }
        }

        this.dice = new Dice();
        this.table = new ArrayList<>();
        this.positions = new Bead[57];
    }


    public void nextRound() throws Exception {


        message = new StringBuilder();
        message.append("--------------------------------------\n");
        if (isOver) {
            message.append("Game is Over You Cant Play , please Start new Game");
            addTableInMessage();
            message.append("--------------------------------------\n");
            return;
        }





        Player player = findPlayerForPlay();
        if (player == null) {

            message.append("Cant Find Any Active Player in Board\n");
            message.append("--------------------------------------\n");
            return;
        }
        message.append(player.getName() + "-Player Turn \n");


        int toss = dice.toss();
        message.append("Player Toss is : " + toss + "\n");


        Bead bead = findPlayerBeadForPlay(player, toss);
        if (bead == null) { // can find models.Bead
            message.append("You Can Move Any Bead!\n");
            nextTurn();
            message.append("--------------------------------------\n");
            return;
        }

        move(bead, toss);

        message.append("--------------------------------------\n");
        if (toss != 6)
            nextTurn();

    }





    @Nullable
    private Player findPlayerForPlay() {
        for (Player p : getPlayers()) {
            if (p.getColor() == getTurn()) {
                if (!p.isComplete()) {
                    return p;
                }
            }
        }
        return null;
    }


    /**
     * Smart for Choice best models.Bead for Move !
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


            // region models.Bead Starting Game
            // models.Bead is in Waiting Position and Want 6 to play game ( move to position 1 )
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


            //region choice In Game models.Bead
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

        //region Fin response ( Max Point models.Bead )

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
        else {
            message.append("Cant Find Any Bead for Move!\n");
            return null;
        }

        //endregion
    }


    private boolean moveBeadTo(Bead bead, int newBeadPositionFromPlayer, int newBeadPositionFromBoard) {



        if (newBeadPositionFromPlayer == 1) { //first move
            message.append(bead.getPlayer().getName() + " -player join new Bead #" + bead.getNumberInPlayerSet() + "\n");
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

            message.append(bead.getPlayer().getName() + " -player , Bead #" + bead.getNumberInPlayerSet()
                    + " Move From : " + bead.getNumberInPlayerSet() + " to : " + newBeadPositionFromPlayer + "\n");


            int oldBeadPositionFromBoard = findBeadPositionFromBoard(bead);
            positions[oldBeadPositionFromBoard] = null;
            positions[newBeadPositionFromBoard] = bead;
            bead.setPositionFromPlayer(newBeadPositionFromPlayer);
            if (newBeadPositionFromPlayer > 40) {

                bead.setInFinalStage(true);
                message.append(bead.getPlayer().getName() + " -player , Bead #" + bead.getNumberInPlayerSet()
                        + " Arrive to Final Stage \n");

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

    private void addTableInMessage() {
        message.append("Result table : \n");
        message.append("---------\n");

        int i = 1;
        for (Player p : table) {
            message.append(i + " - " + p.getName() + "\n");
            i++;
        }
        message.append("---------\n");

    }

    private void checkPlayerIsWinn(Player player) {

        if (checkAllBeadsIsInFinalStage(player)) {
            table.add(player);
            players.remove(player);
            player.setComplete(true);

            message.append(player.getName() + "- Player has been Finished \n");
            addTableInMessage();
            if (players.size() == 0 && table.size() == 4) {
                isOver = true;
                message.append("Game is Over !\n");
                addTableInMessage();
            }
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
        message.append(bead.getPlayer().getName() + " -player , You Kill Enemy in Your " + newBeadPositionFromPlayer
                + " position , you enemy is : " + enemy.getPlayer().getName() + " #" + enemy.getNumberInPlayerSet() + "\n");

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

                    message.append("enemy : " + enemy.getPlayer().getName() + " #" + enemy.getNumberInPlayerSet()
                            + " is in safe house with number : " + newBeadPositionFromBoard + " \n");
                    return true;
                }
                return false;
            case RED:
                if (newBeadPositionFromBoard == 11) {
                    message.append("enemy : " + enemy.getPlayer().getName() + " #" + enemy.getNumberInPlayerSet()
                            + " is in safe house with number : " + newBeadPositionFromBoard + " \n");
                    return true;
                }
                return false;
            case BLUE:
                if (newBeadPositionFromBoard == 21) {
                    message.append("enemy : " + enemy.getPlayer().getName() + " #" + enemy.getNumberInPlayerSet()
                            + " is in safe house with number : " + newBeadPositionFromBoard + " \n");
                    return true;
                }
                return false;
            case GREEN:
                if (newBeadPositionFromBoard == 31) {
                    message.append("enemy : " + enemy.getPlayer().getName() + " #" + enemy.getNumberInPlayerSet()
                            + " is in safe house with number : " + newBeadPositionFromBoard + " \n");
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


        // region is first models.Bead Move
        if (bead.getPositionFromPlayer() == 0) { // first bead move !


            newBeadPositionFromPlayer = 1;
            newBeadPositionFromBoard = findBeadPositionFromBoard(1, bead.getPlayer());

            if (toss == 6) {
                return moveBeadTo(bead, newBeadPositionFromPlayer, newBeadPositionFromBoard);
            } else {
                message.append("You Cant Move Your Bead , your Toss must be 6");
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
            if (positions[positionFromBoard] == b) { // is Friend models.Bead
                return false;
            }
        }
        return true;

    }


    /**
     * Convert Position From the point of view models.Player  to  From the point of view models.Board
     *
     * @param bead
     * @return bead position From the point of view models.Board
     */
    public int findBeadPositionFromBoard(Bead bead) {
        return this.findBeadPositionFromBoard(bead.getPositionFromPlayer(), bead.getPlayer());
    }

    /**
     * Convert Position From the point of view models.Player  to  From the point of view models.Board
     *
     * @param position
     * @param player
     * @return bead position From the point of view models.Board
     */
    public int findBeadPositionFromBoard(int position, Player player) {
        if (position == 0)
            return 0;

        switch (player.getColor()) {
            case YELLOW:
                return position; // Position Form Yellow models.Player and models.Board is same ( By Default)
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


    public StringBuilder getMessage() {
        return message;
    }

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
