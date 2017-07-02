package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Bead;
import models.Board;
import models.Observable.ObservableBead;
import models.ObservablePlayer;
import models.Player;

/**
 * Created by han on 7/3/2017.
 */
public class HomeController {
    public Button startGameBtn;
    public TableView<ObservableBead> beadTable;
    public TableColumn<ObservableBead, String> beadNameColumn;
    public TableColumn<ObservableBead, Number> beadPositionColumn;
    public TableColumn<ObservableBead, Boolean> beadCompleteColumn;
    public TableColumn<ObservableBead, Boolean> beadInGameColumn;
    public TableView<ObservablePlayer> playersTable;
    public TableColumn<ObservablePlayer, String> playerNameColumn;
    public TableView<ObservablePlayer> resultTable;
    public TableColumn<ObservablePlayer, String> resultTableColumn;
    private Board board;
    public TextArea logTxtArea;
    public Button goBtn;
    private ObservableList<ObservableBead> beadsData = FXCollections.observableArrayList();
    private ObservableList<ObservablePlayer> playersData = FXCollections.observableArrayList();
    private ObservableList<ObservablePlayer> resultData = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        beadNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        beadPositionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        beadCompleteColumn.setCellValueFactory(cellData -> cellData.getValue().isComplateProperty());
        beadInGameColumn.setCellValueFactory(cellData -> cellData.getValue().isInGameProperty());
        playerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        resultTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

    }

    public HomeController() throws Exception {

    }

    public void goBtnAction(ActionEvent actionEvent) {
        try {
            board.nextRound();
            logTxtArea.appendText(String.valueOf(board.getMessage()));
            setTableDatas();
        } catch (Exception e) {
            logTxtArea.appendText("Fail to Continue Game\n");
        }

    }

    private void setTableDatas() {
        beadsData = FXCollections.observableArrayList();
        for (Bead b : board.getBeads()) {
            beadsData.add(new ObservableBead(b));
        }
        beadTable.setItems(beadsData);

        playersData = FXCollections.observableArrayList();
        for (Player p : board.getPlayers()) {
            playersData.add(new ObservablePlayer(p));
        }
        playersTable.setItems(playersData);


        resultData = FXCollections.observableArrayList();
        for (Player p : board.getTable()) {
            resultData.add(new ObservablePlayer(p));
        }
        resultTable.setItems(resultData);

    }

    public void startNewGame(ActionEvent actionEvent) {
        try {
            board = new Board();
            logTxtArea.appendText("Start New Game \n");
            setTableDatas();

        } catch (Exception e) {
            logTxtArea.appendText("Fail to Start new Game \n");
        }
    }
}
