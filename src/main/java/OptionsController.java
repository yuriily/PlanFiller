import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by yuriily on 25-Aug-16.
 */
public class OptionsController {
    private Stage primaryStage;

    @FXML
    public TextField urlTextField;
    @FXML
    public TextField loginTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public Button saveOptionsButton;
    @FXML
    public Button closeOptionsButton;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
