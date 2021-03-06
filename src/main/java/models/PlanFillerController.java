package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import data.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

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
    public Button testPlanEntryAddButton;
    @FXML
    public ListView<Suite> testSuiteList;
    @FXML
    public Button testSuiteAddButton;
    @FXML
    public ListView testCaseList;
    @FXML
    public ListView configurationList;
    @FXML
    public Button configurationCopyButton;
    @FXML
    public Button configurationAddButton;
    @FXML
    public Button configurationFillFromFileButton;
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
    public Button uploadPlanButton;
    @FXML
    public TableView<RailRecord> tableView;
    @FXML
    public CheckBox transposeCheckbox;
    @FXML
    public Button optionsButton;
    @FXML
    public Button exitButton;
    @FXML
    public TextArea consoleArea;
    @FXML
    public VBox rightBox;

    private RailModel railModel;
    private RailRecordSet railRecordSet;
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

        //redirect all system data to textarea
        Console console = new Console();
        PrintStream printStream = new PrintStream(console,true);
        System.setOut(printStream);
        System.setErr(printStream);
        System.err.flush();
        System.out.flush();

        //add listeners to fill in other lists

        //when a project is selected, a list of plans, suites and configurations will be updated
        testProjectList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {
            @Override
            public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
                if(newValue==null)
                    return;
                updateProjectList(newValue);

            }
        });

        //when a suite is selected, it will update the list of its test cases
        testSuiteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Suite>() {
            @Override
            public void changed(ObservableValue<? extends Suite> observable, Suite oldValue, Suite newValue) {
                if(null==newValue)
                    return;

                openTableView(false);
                testCaseList.getItems().clear();

                railModel = RailModel.getInstance();
                try {
                railModel.setSelectedSuite(newValue.getId());
                    if(railModel.getCurrentCases() != null && railModel.getCurrentCases().size()>0)
                        testCaseList.setItems(railModel.getCurrentCases());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        //when only one configuration is selected, list its contents
        //if there are two ore more selected configurations, do nothing

        configurationList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Configuration>() {
            @Override
            public void changed(ObservableValue<? extends Configuration> observable, Configuration oldValue, Configuration newValue) {
                if(null==newValue)
                    return;

                openTableView(false);
                configurationItemsList.getItems().clear();

                railModel=RailModel.getInstance();

                List<Configuration> selectedConfigs = configurationList.getSelectionModel().getSelectedItems();
                int[] params = new int[selectedConfigs.size()];
                int iter=0;
                for (Configuration config: selectedConfigs)
                    params[iter++] = config.getId();
                try {
                railModel.setSelectedConfigurations(params);
                    configurationItemsList.setItems(railModel.getCurrentConfigurationItems());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        });

        testPlanList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Plan>() {
            @Override
            public void changed(ObservableValue<? extends Plan> observable, Plan oldValue, Plan newValue) {
                if(null==newValue)
                    return;
                openTableView(false);
                RailModel.getInstance().setSelectedPlan(newValue.getId());
            }
        });
        testCaseList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Case>() {
            @Override
            public void changed(ObservableValue<? extends Case> observable, Case oldValue, Case newValue) {
                if(null==newValue)
                    return;
                openTableView(false);
                RailModel.getInstance().setSelectedCase(newValue.getId());
            }
        });
        configurationItemsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConfigurationItem>() {
            @Override
            public void changed(ObservableValue<? extends ConfigurationItem> observable, ConfigurationItem oldValue, ConfigurationItem newValue) {
                if(null==newValue)
                    return;

                openTableView(false);
            }
        });



        optionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openOptionsPanel(event);
            }
        });

        previewPlanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { refreshTable(event); }
        });

        importPlanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { importRecordSet();  }
        });

        exportPlanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { exportRecordSet();   }
        });

        itemRemoveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeConfigurationItem();
            }
        });

        configurationCopyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (railModel.getSelectedConfigurations().length == 1)
                    copyConfiguration();
                else
                    System.out.println("Please select only one configuration to copy.");
            }}
        );

        testPlanEntryAddButton.setOnAction(event -> {
            if(RailModel.getInstance().getSelectedPlan()==0) {
                System.out.println("Please select a test plan first.");
                return;
            }
            if(railRecordSet!=null && railRecordSet.getRows().size()>0) {

                boolean isCasesInRows = railRecordSet.getRows().get(0).getRowValue().getClass().getSimpleName().equals("Case");

                if(!isCasesInRows && RailModel.getInstance().getSelectedCase()==0) {
                        System.out.println("You've made a [configuration] * [configuration] table. Please also select a test case to proceed.");
                        return;
                }

                addTestPlanEntry(RailModel.getInstance().getSelectedPlan(), isCasesInRows);
            }
            else
                System.out.println("There is nothing to insert. Please create a data set first.");
        });

        testPlanAddButton.setOnAction(event -> {
            if(RailModel.getInstance().getSelectedProject()==0) {
                System.out.println("Please select a project first.");
                return;
            }
            TextInputDialog entryNameDialog = new TextInputDialog("New test plan");
            entryNameDialog.setContentText("Please name a plan:");
            entryNameDialog.setHeaderText("Test plan name");
            Optional<String> result = entryNameDialog.showAndWait();
            if(result.isPresent()) {
                Map<String, Object> planMap = new HashMap<>();
                planMap.put("name", result.get());
                RailClient.getInstance().createOneInstance(planMap, Plan.class,RailModel.getInstance().getSelectedProject());
                //todo refresh the list of plans correctly, not by selecting/deselecting one
                RailModel.getInstance().resetCache();
                int oldProject=testProjectList.getSelectionModel().getSelectedIndex();
                testProjectList.getSelectionModel().select(null);
                testProjectList.getSelectionModel().select(oldProject);
            }
        });

        //always scroll textarea to the bottom when new string is added
        //consoleArea.textProperty().addListener((observable, oldValue, newValue) -> consoleArea.setScrollTop(Double.MAX_VALUE));


    }

    //opens options dialog
    public void openOptionsPanel(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Options");
        FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("/options.fxml"));

        try {
            Parent options = optionsLoader.load();
            Scene scene = new Scene(options);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());

            OptionsController optionsController = (OptionsController) optionsLoader.getController();
            String railsURL = OptionsValues.getInstance().getRailsURL();
            String username = OptionsValues.getInstance().getUsername();
            if(railsURL!=null && !railsURL.isEmpty())
                optionsController.urlTextField.setText(railsURL);
            if(username!=null && !username.isEmpty())
                optionsController.loginTextField.setText(username);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //refreshes the table according to current selection
    //the following combinations are possible:
    //test suite + configuration
    //configuration + configuration (then, when you reimport such csv, you should indicate test case or suite

    public void refreshTable(ActionEvent event) {
        //need a confirmation - the table will be rebuilt and all the data lost
        if(railRecordSet!=null) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Rebuild the table? All data will be lost.", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("New data for the table");
        alert.showAndWait();
        if(alert.getResult() != ButtonType.YES)
            return;
        }
            tableView.getItems().clear();
            tableView.getColumns().clear();
            tableView.setPlaceholder(new Label("Loading data..."));


        railModel=RailModel.getInstance();

        //reset current recordset, we don't need it anymore
        railRecordSet = new RailRecordSet();

        if(railModel.getSelectedSuite()!=0 && railModel.getSelectedConfigurations()!=null && railModel.getSelectedConfigurations().length==1) {
            //make a record as cases*configuration
            railRecordSet.setColumnNames((List)railModel.getCurrentConfigurationItems());
            //we don't have any values in cells, so only the row headers will be retrieved
            for(Case testCase : railModel.getCurrentCases()) {
                RailRecord record = new RailRecord();
                record.setRowValue(testCase);
                railRecordSet.addRow(record);
            }

        }
        else {
            if(railModel.getSelectedConfigurations()!=null && railModel.getSelectedConfigurations().length==2) {
                //make a table as configuration*configuration

                //selection from configurations list will be erased, so I need to preserve both items
                int[] configOneId = new int[] {railModel.getSelectedConfigurations()[0]};
                int[] configTwoId = new int[] {railModel.getSelectedConfigurations()[1]};

                try {
                    //here are the columns
                    railModel.setSelectedConfigurations(configOneId);
                    List<ConfigurationItem> configOneList = railModel.getCurrentConfigurationItems();
                    railRecordSet.setColumnNames((List)configOneList);

                    railModel.setSelectedConfigurations(configTwoId);
                    List<ConfigurationItem> configTwoList = railModel.getCurrentConfigurationItems();
                    for(ConfigurationItem configItem : configTwoList) {
                        RailRecord record = new RailRecord();
                        record.setRowValue(configItem);
                        railRecordSet.addRow(record);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                //only one suite and one to two configurations should be selected
                System.out.println("Wrong combination of test entities selected.");
                System.out.println("Try to select two configurations or a test suite and one configuration.");
                tableView.setPlaceholder(new Label("Wrong selection"));

            }
        }
        //there is some data for at least one row and column
        if(railRecordSet.getRows()!=null && railRecordSet.getColumnNames()!=null) {
            //todo run this in separate thread
            refreshTableFromRecordSet(railRecordSet);
            openTableView(true);


        }

    }

    public void refreshTableFromRecordSet(RailRecordSet recordSet) {
        if(recordSet == null || recordSet.getRows() == null || recordSet.getColumnNames() == null) {
            System.out.println("There are no records in the recordset. Canceling.");
            return;
        }

        tableView.getItems().clear();
        tableView.getColumns().clear();
        //the table will contain the list of models.RailRecord objects
        tableView.setPlaceholder(new Label("Refreshing table..."));

        tableView.setItems(FXCollections.observableList(recordSet.getRows()));
        tableView.setEditable(true);

        //add table columns
        //first column goes for header row, class name is in the header: "row \ column"
        TableColumn<RailRecord, String> headerColumn=new TableColumn<>(
                ((TestRailsEntity)recordSet.getRows().get(0).getRowValue()).getClass().getSimpleName() + " \\ " +
        recordSet.getColumnNames().get(0).getClass().getSimpleName());
        headerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RailRecord, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RailRecord, String> param) {
                return new SimpleStringProperty(param.getValue().getRowValue().toString());
            }
        });
        tableView.getColumns().add(headerColumn);

        //other column set is as long as the list of second entities
        for(TestRailsEntity columnEntity : recordSet.getColumnNames()) {
            TableColumn<RailRecord, String> column = new TableColumn<>(columnEntity.toString());
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RailRecord, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<RailRecord, String> param) {
                    //todo get a value from the map
                    return new SimpleStringProperty(param.getValue().getColumnValues().get(columnEntity));
                }
            });
            //update the corresponding railrecord
            column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RailRecord, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<RailRecord, String> event) {
                    updateRailRecord(event, recordSet);
                }
            });
            tableView.getColumns().add(column);
        }
        tableView.refresh();

    }

    //update our recordset with new values
    public void updateRailRecord(TableColumn.CellEditEvent<RailRecord, String> event, RailRecordSet recordSet) {
        RailRecord currentRow = event.getRowValue();
        TestRailsEntity currentColumn = null;
        //find column object by text in column header
        int columnObjectId = Integer.parseInt(event.getTableColumn().getText().substring(0,6).trim());
        for(TestRailsEntity columnObject: recordSet.getColumnNames()) {
            if(columnObject.getId()==columnObjectId)
                currentColumn=columnObject;
        }
        if(currentColumn==null) {
            System.out.println("Cannot identify column by the following id:" + columnObjectId);
            return;
        }
        currentRow.getColumnValues().put(currentColumn, event.getNewValue());
    }


    //this is to redirect all system output to textarea
    private class Console extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            consoleArea.appendText(String.valueOf((char)b));
        }
    }

    public void openTableView(boolean shouldOpen) {
        if(shouldOpen) {
            tableView.setPrefWidth(800.0);
            rightBox.setPrefWidth(800.0);
            rightBox.setMinWidth(800.0);

        } else {
            tableView.setPrefWidth(200.0);
            rightBox.setPrefWidth(200.0);
            rightBox.setMinWidth(200.0);
        }
    }

    public void exportRecordSet() {
        if(railRecordSet==null) {
            System.out.println("ERROR: there is nothing to export.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if(file != null) {
            try {
                //create csv and save it
                        CsvImporter importer = new CsvImporter(railRecordSet);
                        importer.writeToCSV(file.getAbsolutePath());
                        System.out.println("Map saved to file: "+file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void importRecordSet() {
        //if there is no project selected, then there is no sense in importing the data now - configurations won't be resolved
        if (railModel == null || railModel.getSelectedProject() == 0) {
            System.out.println("Please select a project first, otherwise it won't be possible to work with configurations.");
            return;
        }

        if (railRecordSet != null) {
            //confirm that current table will be erased
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Rebuild the table? All data will be lost.", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES)
                return;
        }

        //Important! Empty the recordset so there is nothing left from previous table
        railRecordSet = new RailRecordSet();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open the file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
                //pass our empty recordset there
                ProgressForm pForm = new ProgressForm();
                pForm.setLabelText("Reading test plan from csv");

                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        CsvImporter importer = new CsvImporter(railRecordSet, railModel);
                        return importer.readFromCSV(file.getAbsolutePath());
                    }
                };

                pForm.activateProgressBar(task);

                task.setOnSucceeded(e -> {
                    pForm.getDialogStage().close();
                    //update the table view only if there was no big errors during map retrieval
                    if(task.getValue()) {
                        refreshTableFromRecordSet(railRecordSet);
                        openTableView(true);
                    }
                });
            task.setOnFailed(e-> {
                System.out.println("Importing from csv was interrupted unexpectedly.");
                pForm.getDialogStage().close();
            });

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                pForm.getDialogStage().show();
                thread.start();

        }

    }

    public void removeConfigurationItem() {
        if(configurationItemsList != null && configurationItemsList.getSelectionModel().getSelectedItem() != null) {
            //no need to get an item from the model - we don't store it there
            //it's much faster to parse the string for id
            int itemId = Integer.parseInt(configurationItemsList.getSelectionModel().getSelectedItem()
                    .toString()
                    .substring(0,6)
                    .trim());
            RailClient client = RailClient.getInstance();
            client.deleteOneInstance(itemId, ConfigurationItem.class);

            //remove the item without modifying the model - configuration items don't trigger any changes
            //move the selection to the next item, so the items can be deleted with ease
            int currentIndex = configurationItemsList.getSelectionModel().getSelectedIndex();
            int newIndex = (currentIndex == configurationItemsList.getItems().size() - 1) ? currentIndex-1 : currentIndex;
            configurationItemsList.getItems().remove(currentIndex);
            configurationItemsList.getSelectionModel().select(newIndex);
            configurationItemsList.refresh();
        }
    }

    //adding an entry to existing test plan
    public void addTestPlanEntry(int planId, boolean isCasesInRows) {
        //FIRST CASE: we've got cases in rows and configurations in columns
        //SECOND CASE: configuration X configuration
        PlanEntry planEntry = new PlanEntry();

        int recordsToAdd = railRecordSet.countEntries();
        if(recordsToAdd==0) {
            System.out.println("There is nothing to add - all table cells are empty. Please enter at least one value.");
            return;
        }
        System.out.println("Number of entries to add: " + recordsToAdd);

        TextInputDialog entryNameDialog = new TextInputDialog("Common tests");
        entryNameDialog.setContentText("Please name a plan entry:");
        entryNameDialog.setHeaderText("Plan entry name");
        Optional<String> result = entryNameDialog.showAndWait();
        if(result.isPresent())
            planEntry.setName(result.get());
        else
            planEntry.setName("Common tests");

        planEntry.setIncludeAll(false);

        //get a suite id from the first test case - we assume that all cases belong to one suite
            int currentSuiteId = (isCasesInRows) ?
                    ((Case)railRecordSet.getRows().get(0).getRowValue()).getSuiteId() :
                    railModel.getSelectedSuite();

        planEntry.setSuiteId(currentSuiteId);

        //get all possible configurations
        //check for uniqueness first - are there any duplicates
        HashSet<TestRailsEntity> configSet = new HashSet<>(railRecordSet.getColumnNames());
        if(configSet.size()!=railRecordSet.getColumnNames().size()) {
            System.out.println("ERROR: there are duplicates in table columns. Plan entry is not added.");
            return;
        }

        //add all test runs to the entry
        System.out.println("Adding test runs from recordset...");
        planEntry.addRunsFromRecordset(railRecordSet, railModel, isCasesInRows);
        System.out.println("Successfully added " + planEntry.getRuns().size() + " runs.");

        //todo set assignee as current user
        planEntry.setAssignedToId(0);

        //transfer this to railclient
        ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map<String,Object> mapToPost = mapper.convertValue(planEntry, Map.class);
        try {
            System.out.println("Trying to insert an entry to plan with id: "+planId);
            System.out.println(RailClient.getInstance().tempPostPlan(planId, mapToPost));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //copies the selected configuration with all its items to the new project
    public void copyConfiguration() {
        try {
            ChoiceDialog<Project> dialog = new ChoiceDialog<>(railModel.getCurrentProjects().get(0), railModel.getCurrentProjects());
        dialog.setTitle("Select a project");
        dialog.setHeaderText("Select a receiving project");
        dialog.setContentText("Project:");
        Optional<Project> result = dialog.showAndWait();
        if(result.isPresent()) {
            System.out.println("Starting to copy configuration to project: " + result.get().toString());
            RailClient.getInstance().addConfiguration(result.get().getId(),
                    railModel.getCurrentConfigurations().get(
                            railModel.getCurrentConfigurations().indexOf(
                                    configurationList.getSelectionModel().getSelectedItem())));

        }
        } catch (Exception e ) {
            e.printStackTrace();
        }

    }

    private void updateProjectList(Project newValue) {
        openTableView(false);
        testPlanList.getItems().clear();
        testSuiteList.getItems().clear();
        testCaseList.getItems().clear();
        configurationList.getItems().clear();
        configurationItemsList.getItems().clear();
        railModel = RailModel.getInstance();
        try {
            railModel.setSelectedProject(newValue.getId());
            //todo bind these things with properties some day
            railModel.setSelectedSuite(0);
            railModel.setSelectedPlan(0);

            testPlanList.setItems(railModel.getCurrentPlans());
            testSuiteList.setItems(railModel.getCurrentSuites());
            configurationList.setItems(railModel.getCurrentConfigurations());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
