package NimGame.javafx;

import NimGame.results.GameResultDao;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import util.guice.PersistenceModule;

import javax.inject.Inject;
import java.util.List;

@Slf4j
public class NimGameApplication extends Application {

    private GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                     install(new PersistenceModule("NIMGAMEdbs"));
                     bind(GameResultDao.class);

                 }
            }
    ));

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting application...");
        context.init();
        fxmlLoader.setLocation(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("NIM Game");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
