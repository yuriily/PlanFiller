import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class PlanFillerController {
    private Stage primaryStage;
    private Parent options;

    @FXML
    public ListView testProjectList;
    @FXML
    public ListView testPlanList;
    @FXML
    public Button testPlanAddButton;
    @FXML
    public ListView testSuiteList;
    @FXML
    public Button testSuiteAddButton;
    @FXML
    public ListView testCaseList;
    @FXML
    public ListView configurationList;
    @FXML
    public Button configurationAddButton;
    @FXML
    public Button configurationNewFromFileButton;
    @FXML
    public ListView configurationItemsList;
    @FXML
    public Button itemAddButton;
    @FXML
    public Button itemRemoveButton;
    @FXML
    public Button importPlanButton;
    @FXML
    public Button exportPlanButton;
    @FXML
    public Button previewPlanButton;
    @FXML
    public Button fillPlanButton;
    @FXML
    public TableView tableView;
    @FXML
    public CheckBox transposeCheckbox;
    @FXML
    public Button optionsButton;
    @FXML
    public Button exitButton;


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        optionsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                openOptionsPanel(event);
            }
        });
    }

    public void openOptionsPanel(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Options");
        FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("options.fxml"));
        try {
            Parent options = optionsLoader.load();
            Scene scene = new Scene(options);
            stage.setScene(scene);
//            primaryStage.hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
