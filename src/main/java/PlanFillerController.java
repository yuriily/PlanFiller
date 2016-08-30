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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private RailModel railModel;
    private int[] tempIntArray = new int[2];


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        //add something that was not configured in fxml
        configurationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        //add listeners to fill in other lists

        //when a project is selected, a list of plans, suites and configurations will be updated
        testProjectList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {
            @Override
            public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
                if(null==newValue)
                    return;

                testPlanList.getItems().clear();
                testSuiteList.getItems().clear();
                testCaseList.getItems().clear();
                configurationList.getItems().clear();
                configurationItemsList.getItems().clear();
                railModel = RailModel.getInstance();
                try {
                railModel.setSelectedProject(newValue.getId());
                    //todo bind these things with properties
                railModel.setSelectedSuite(0);
                railModel.setSelectedPlan(0);
                    //there should be max two parameters; and theoretical maximum is 3 - for test cases

                    testPlanList.setItems(railModel.getCurrentPlans());
                    testSuiteList.setItems(railModel.getCurrentSuites());
                    configurationList.setItems(railModel.getCurrentConfigurations());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //when a suite is selected, it will update the list of its test cases
        testSuiteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Suite>() {
            @Override
            public void changed(ObservableValue<? extends Suite> observable, Suite oldValue, Suite newValue) {
                if(null==newValue)
                    return;

                testCaseList.getItems().clear();

                railModel = RailModel.getInstance();
                try {
                railModel.setSelectedSuite(newValue.getId());
                    //todo bind properties to listen to selection changes for every list except for config (it will have multi selection)
                    if(null!=railModel.getCurrentCases() && railModel.getCurrentCases().size()>0)
                        testCaseList.setItems(railModel.getCurrentCases());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

//        configurationList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Configuration>() {
//            @Override
//            public void changed(ObservableValue<? extends Configuration> observable, Configuration oldValue, Configuration newValue) {
//                if(null==newValue)
//                    return;
//
//                configurationItemsList.getItems().clear();
//
//                List<Configuration> selectedConfigs = configurationList.getSelectionModel().getSelectedItems();
//
//                //show configuration items only when only one configuration is selected
//                if(selectedConfigs.size()==1) {
//                    int[] tempInt = new int[]{newValue.getId()};
//                    railModel.setSelectedConfigurations(tempInt);
//                    try {
//                        ObservableList<ConfigurationItem> configurationItems = FXCollections.observableArrayList(railModel.getConfigurationItems());
//                        if(null!=configurationItems && configurationItems.size()>0)
//                            configurationItemsList.setItems(configurationItems);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //otherwise just update the model - this info will be needed to build a config*config table
//                if(selectedConfigs.size()>1) {
//                    int[] tempInt = new int[selectedConfigs.size()];
//                    int iter=0;
//                    for(Configuration config : selectedConfigs) {
//                        tempInt[iter++] = config.getId();
//                    }
//                    railModel.setCurrentConfigurations(tempInt);
//                }
//            }
//        });

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
