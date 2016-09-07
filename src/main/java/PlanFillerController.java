import data.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.supercsv.io.ICsvMapWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
    public Button fillPlanButton;
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
                if(null==newValue)
                    return;

                openTableView(false);
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

                openTableView(false);
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
            }
        });
        testCaseList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Case>() {
            @Override
            public void changed(ObservableValue<? extends Case> observable, Case oldValue, Case newValue) {
                if(null==newValue)
                    return;

                openTableView(false);
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
            public void handle(ActionEvent event) { importRecordSet();

            }
        });


    }

    //opens options dialog
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

    //refreshes the table accroding to current selection
    //the following combinations are possible:
    //test suite + configuration
    //configuration + configuration (then, when you reimport such csv, you should be prompted to indicate test case or suite

    public void refreshTable(ActionEvent event) {
        tableView.getItems().clear();
        tableView.getColumns().clear();
        tableView.setPlaceholder(new Label("Loading data..."));

        railModel=RailModel.getInstance();
        //todo ask for a confirmation - the table will be rebuilt and all the data lost
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
            //todo update the table
            refreshTableFromRecordSet(railRecordSet);
            openTableView(true);


        }

    }

    public void refreshTableFromRecordSet(RailRecordSet recordSet) {
        tableView.getItems().clear();
        tableView.getColumns().clear();
        //the table will contain the list of RailRecord objects
        tableView.setPlaceholder(new Label("Refreshing table..."));

        tableView.setItems(FXCollections.observableList(recordSet.getRows()));
        tableView.setEditable(true);
        //doesn't work for now, but should be implemented some day
        //otherwise double clicking the item will erase the text
//        Callback<TableColumn, TableCell> cellCallback = new Callback<TableColumn, TableCell>() {
//          public TableCell call(TableColumn col) {
//              return new EditingCell();
//          }
//        };

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
                    //get a value from the map
                    return null;
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

    public void importRecordSet() {
        if(railRecordSet==null) {
            System.out.println("ERROR: there is nothing to import");
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


}
