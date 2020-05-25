package NimGame.javafx.controller;

import NimGame.results.GameResult;
import NimGame.results.GameResultDao;
import NimGame.results.HighScoreDao;
import NimGame.results.HighScores;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@Slf4j
public class HighScoreController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private HighScoreDao HighScoreDao;

    @FXML
    private TableView<HighScores> highScoreTable;

    @FXML
    private TableColumn<GameResult, String> player;

    @FXML
    private TableColumn<GameResult, Integer> NumberOfGames;



    @FXML
    private void initialize() {
        log.debug("Loading high scores...");
        List<HighScores> highScoreList = HighScoreDao.findBest(5);

        player.setCellValueFactory(new PropertyValueFactory<>("player"));
        NumberOfGames.setCellValueFactory(new PropertyValueFactory<>("NumberOfGames"));



        ObservableList<HighScores> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Loading launch scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleHistoryButton(ActionEvent actionEvent) throws IOException {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Loading history scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/history.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
