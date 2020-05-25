package NimGame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LaunchController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField playerName1TextField;

    @FXML
    private TextField playerName2TextField;

    @FXML
    private Label errorLabel;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerName1TextField.getText().isEmpty()||playerName2TextField.getText().isEmpty()) {
            errorLabel.setText("Enter your names!");
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayerName1(playerName1TextField.getText());
            fxmlLoader.<GameController>getController().setPlayerName2(playerName2TextField.getText());

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            log.info("The players names are set to {} and {}, loading game scene ...", playerName1TextField.getText(),playerName2TextField.getText());
        }
    }

}
