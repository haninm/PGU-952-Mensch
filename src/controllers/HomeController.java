package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import models.Board;

/**
 * Created by han on 7/3/2017.
 */
public class HomeController {
    private Board board;
    public TextArea logTxtArea;
    public Button goBtn;

    public HomeController() throws Exception {

    }

    public void goBtnAction(ActionEvent actionEvent) {
        try {
            board.nextRound();
            logTxtArea.appendText(String.valueOf(board.getMessage()));
        } catch (Exception e) {
            logTxtArea.appendText("Fail to Continue Game\n");
        }

    }

    public void startNewGame(ActionEvent actionEvent) {
        try {
            board = new Board();
            logTxtArea.appendText("Start New Game \n");

        } catch (Exception e) {
            logTxtArea.appendText("Fail to Start new Game \n");
        }
    }
}
