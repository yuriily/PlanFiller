import data.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;

public class PlanFillerController {
    private Stage primaryStage;
    private Parent options;

    @FXML
    public ListView<Project> testProjectList;
    @FXML
    public ListView<Plan> testPlanList;
    @FXML
    public Button testPlanAddButton;
    @FXML
    public ListView<Suite> testSuiteList;
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

    private int[] tempIntArray = new int[2];


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        testProjectList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {
            @Override
            public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
                testSuiteList.getItems().clear();
                testPlanList.getItems().clear();
                configurationList.getItems().clear();
                configurationItemsList.getItems().clear();
                try {
                    //there should be max two parameters; and theoretical maximum is 3 - for test cases
                    tempIntArray[0] = newValue.getId();
                    ObservableList<Plan> plans = FXCollections.observableArrayList(RailClient.getInstance().getAllInstances(tempIntArray, Plan.class));
                    ObservableList<Suite> suites = FXCollections.observableArrayList(RailClient.getInstance().getAllInstances(tempIntArray, Suite.class));
                    ObservableList<Configuration> configurations = FXCollections.observableArrayList(RailClient.getInstance().getAllInstances(tempIntArray, Configuration.class));

                    testPlanList.setItems(plans);
                    testSuiteList.setItems(suites);
                    configurationList.setItems(configurations);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        testSuiteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Suite>() {
            @Override
            public void changed(ObservableValue<? extends Suite> observable, Suite oldValue, Suite newValue) {
                testCaseList.getItems().clear();
                try {
                    //todo bind properties to listen to selection changes for every list except for config (it will have multi selection)
                    //project
                    tempIntArray[0]=((Project)testProjectList.getSelectionModel().getSelectedItem()).getId();
                    //suite
                    tempIntArray[1]=newValue.getId();
                    //section - skipped for now
                    ObservableList<Case> cases = FXCollections.observableArrayList(RailClient.getInstance().getAllInstances(tempIntArray, Case.class));
                    if(null!=cases && cases.size()>0)
                        testCaseList.setItems(cases);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

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
