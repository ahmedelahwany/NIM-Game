package NimGame.javafx.controller;

import NimGame.results.GameResultDao;
import NimGame.results.HighScoreDao;
import NimGame.results.HighScores;
import NimGame.state.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import NimGame.results.GameResult;
import NimGame.state.NimGameState;


import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    @Inject
    private HighScoreDao HighScoreDao;

    private String playerName1;
    private String playerName2;



    private NimGameState StateOfGame;

    private IntegerProperty StepsPlayer1 = new SimpleIntegerProperty();
    private IntegerProperty StepsPlayer2= new SimpleIntegerProperty();


   private int row;
    private int col;

    private Instant startTime;
    private List<Image> SquareImages;

    @FXML
    private Label messageLabel;

    @FXML

    private Label ErrorMessages;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stepsPlayer1Label;

    @FXML
    private Label stepsPlayer2Label;

    @FXML
    private Label stepsPlayer1;

    @FXML
    private Label stepsPlayer2;

    @FXML
    private Label stopWatchLabel;

    private Timeline stopWatchTimeline;

    @FXML
    private Button resetButton;

    @FXML
    private Button EndGameButton;

    @FXML
    private Button SwitchTurnsButton;



    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public void setPlayerName1(String playerName) {
        this.playerName1 = playerName;
    }
    public void setPlayerName2(String playerName) {
        this.playerName2 = playerName;
    }

    @FXML
    public void initialize() {
            SquareImages = List.of(
                new Image(getClass().getResource("/images/square.png").toExternalForm()),
                new Image(getClass().getResource("/images/squareFilled.png").toExternalForm())

        );
        stepsPlayer1Label.textProperty().bind(StepsPlayer1.asString());
        stepsPlayer2Label.textProperty().bind(StepsPlayer2.asString());


        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.info("Game is over");
                log.debug("Saving result to database...");
                gameResultDao.persist(createGameResult());

                HighScores em =HighScoreDao.find(StateOfGame.getWinner().getName());
                if(em==null){
                HighScoreDao.persist(createHighScore());}
                else{
                    HighScoreDao.update(em.getPlayer(),em.getNumberOfGames()+1);
                }

                stopWatchTimeline.stop();
            }
        });

        Platform.runLater(() ->resetGame());
    }


    private void resetGame() {

   StateOfGame = new NimGameState(new Player(playerName1),new Player(playerName2));
        StepsPlayer1.set(0);
        StepsPlayer1.set(0);
        stepsPlayer1.setText(playerName1+" Moves");
        stepsPlayer2.setText(playerName2+" Moves");

        startTime = Instant.now();
        gameOver.setValue(false);
        displayGameState();
        createStopWatch();
       messageLabel.setText("Good luck, " + playerName1 + " AND "+playerName2+ " ! \n"+
                                                     "To take a stone click on it \n"+
                                                     "To Switch turns during the game ,Click Switch turns");
      ErrorMessages.setText("Player "+ playerName1+" now Has the turn");

    }

    private void displayGameState() {

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4;++j)  {

                ImageView view = (ImageView) gameGrid.getChildren().get(i * 4 + j);
                if (view.getImage() != null) {
                    log.trace("Image({}, {}) = {}", i, j, view.getImage().getUrl());
                }
                view.setImage(SquareImages.get(StateOfGame.getBoard()[i][j].isExists()));
            }
        }
    }

    public void handleClickOnCube(MouseEvent mouseEvent) {

        try{
            row = GridPane.getRowIndex((Node) mouseEvent.getSource());
             col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        }catch(NullPointerException ex) {

            StateOfGame.setErrorMessage("");
            log.info("Stone ({}, {}) is pressed", row, col);
            if (!StateOfGame.isSolved() && StateOfGame.canBeTaken(row, col)) {
                try {
                    StateOfGame.takeStone(row, col);
                  if(StateOfGame.getErrorMessage()=="")
                      if(playerName1==StateOfGame.getActivePlayer().getName()) {
                          StepsPlayer1.set(StepsPlayer1.get()+1);
                      } else{
                        StepsPlayer2.set(StepsPlayer2.get()+1);
                    }
                } catch (UnsupportedOperationException e) {
                    log.debug("UnsupportedOperationException has been found in take stone method");
                }
                if (StateOfGame.isSolved()) {
                    gameOver.setValue(true);
                    log.info("Player {} has won the game in {} steps", StateOfGame.getWinner().getName(), StateOfGame.getActivePlayer().getTotalMoves());
                    messageLabel.setText("Congratulations, " + StateOfGame.getWinner().getName() + "!");
                    resetButton.setDisable(true);
                    SwitchTurnsButton.setDisable(true);
                    EndGameButton.setText("Finish");
                }
            }
            ErrorMessages.setText(StateOfGame.getErrorMessage());

            displayGameState();
        }
    }
    public void handleSwitchTurnsButton(ActionEvent actionEvent){
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Switching Turns...");
        StateOfGame.switchPlayers();
        log.info("Player {} now Has the turn", StateOfGame.getActivePlayer().getName());
        Platform.runLater(()->     ErrorMessages.setText(StateOfGame.getErrorMessage()));


    }

    public void handleResetButton(ActionEvent actionEvent)  {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Resetting game...");
        stopWatchTimeline.stop();
        resetGame();
    }

    public void handleEndGameButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        log.debug("{} is pressed", buttonText);
        if (buttonText.equals("End Game")) {
            log.info("The game has been ended");
        }
        gameOver.setValue(true);
        log.info("Loading high scores scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GameResult createGameResult() {


        GameResult result = GameResult.builder()
                .player1(playerName1)
                .player2(playerName2)
                .winner(StateOfGame.getWinner().getName())
                .solved(StateOfGame.isSolved())
                .duration(Duration.between(startTime, Instant.now()))
                .stepsPlayer1(StepsPlayer1.get())
                .stepsPlayer2(StepsPlayer2.get())
                .build();
        return result;
    }

    private HighScores createHighScore() {

            HighScores newHighScore = HighScores.builder()
                    .NumberOfGames(1).player(StateOfGame.getWinner().getName()).build();
            return newHighScore;
    }


    private void createStopWatch() {
        stopWatchTimeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            long millisElapsed = startTime.until(Instant.now(), ChronoUnit.MILLIS);
            stopWatchLabel.setText(DurationFormatUtils.formatDuration(millisElapsed, "HH:mm:ss"));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));
        stopWatchTimeline.setCycleCount(Animation.INDEFINITE);
        stopWatchTimeline.play();
    }

}
