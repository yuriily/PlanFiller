/**
 * Created by yuriily on 25-Aug-16.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlanFiller extends Application {
    private Stage currentStage;
    private Scene mainScene, optionsScene;
    private PlanFillerController planFillerController;
    private OptionsController optionsController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("options.fxml"));
        Parent root = mainLoader.load();
        planFillerController = (PlanFillerController) mainLoader.getController();
        optionsController = (OptionsController) optionsLoader.getController();
        planFillerController.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Rails UI");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(1200);
        mainScene = new Scene(root, 1200, 600);

        //todo trust all certificates to enable it working with corporate proxy
        //todo try to load config
        //todo try to connect with existing credentials; skip if config is empty
        //if it's not, try to open last known project and suite and configuration

        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
}
