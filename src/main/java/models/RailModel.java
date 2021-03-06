package models;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by yuriily on 29-Aug-16.
 *
 */
public class RailModel {
    private final static int TIMEOUT_TIME = 60*60*1000; //1-hour refresh rate for cached items

    private Map<Integer, Project> cachedProjects = new HashMap<>();
    private Map<Integer, Suite> cachedSuites = new HashMap<>();
    private Map<Integer, Configuration> cachedConfigurations = new HashMap<>();
    private Map<Integer, Long> entityTime = new HashMap<>();

    private ObservableList<Project> currentProjects;
    private ObservableList<Plan> currentPlans;
    private ObservableList<Suite> currentSuites;
    private ObservableList<Case> currentCases;
    private ObservableList<Configuration> currentConfigurations;
    private ObservableList<ConfigurationItem> currentConfigurationItems;

    private int selectedProject;
    private int selectedSuite;
    private int[] selectedConfigurations;
    private int selectedPlan;
    private int selectedCase;
    private int selectedConfigurationItem;

    //no access from outside for this field
    private long lastTimeUpdated;
    private RailClient client;

    private static RailModel ourInstance = new RailModel();

    public static RailModel getInstance() {
        return ourInstance;
    }

    private RailModel() {
    }

    public int getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(int selectedProject) throws Exception {
        this.selectedProject = selectedProject;
        if(selectedProject==0)
            return;

        //create a temp. project to reduce calls to testrail
        Project tempProject = null;
        if(!cachedProjects.containsKey(selectedProject)) {
            //no project in the cache
            tempProject = this.downloadProject(selectedProject);
            cachedProjects.put(selectedProject, tempProject);
            entityTime.put(selectedProject, System.currentTimeMillis());
        }
        else {
            //if the project was updated long ago, re-read it
            if(System.currentTimeMillis() - entityTime.get(selectedProject) > TIMEOUT_TIME) {
                tempProject = this.downloadProject(selectedProject);
                cachedProjects.put(selectedProject, tempProject);
                entityTime.put(selectedProject, System.currentTimeMillis());
            }
        }
        if(tempProject==null)
            tempProject = this.cachedProjects.get(selectedProject);
        this.currentPlans = FXCollections.observableArrayList(tempProject.getPlans());
        this.currentSuites = FXCollections.observableArrayList(tempProject.getSuites());
        this.currentConfigurations = FXCollections.observableArrayList(tempProject.getConfigurations());

    }

    public int getSelectedSuite() {
        return selectedSuite;
    }

    public void setSelectedSuite(int selectedSuite) throws Exception {
        this.selectedSuite = selectedSuite;
        if(selectedSuite==0)
            return;

        Suite tempSuite = null;
        if(!cachedSuites.containsKey(selectedSuite)) {
            //no suite in the cache
            tempSuite = this.downloadSuite(selectedSuite);
            cachedSuites.put(selectedSuite, tempSuite);
            entityTime.put(selectedSuite, System.currentTimeMillis());
        }
        else {
            //if the suite was updated long ago, re-read it
            if(System.currentTimeMillis() - entityTime.get(selectedSuite) > TIMEOUT_TIME) {
                tempSuite = this.downloadSuite(selectedSuite);
                cachedSuites.put(selectedSuite, tempSuite);
                entityTime.put(selectedSuite, System.currentTimeMillis());
            }
        }
        if(null==tempSuite)
            tempSuite = this.cachedSuites.get(selectedSuite);
        this.setCurrentCases(FXCollections.observableArrayList(tempSuite.getCases()));

    }

    public int[] getSelectedConfigurations() {
        return selectedConfigurations;
    }

    public void setSelectedConfigurations(int[] selectedConfigurations) throws Exception {
        this.selectedConfigurations = selectedConfigurations;
        if(selectedConfigurations==null)
            return;

        if(selectedConfigurations.length==1) {
            //only one item is selected so we can update the configuration list
            int configId = selectedConfigurations[0];

            //update the project if it's old; no need to update configurations separately as they all are received by one request
            if(System.currentTimeMillis() - entityTime.get(this.getSelectedProject()) > TIMEOUT_TIME)
                this.selectedProject=this.getSelectedProject();

            List<ConfigurationItem> tempList;
            //find a configuration object by its id and extract all its items
            Predicate<Configuration> predicate = c -> c.getId() == configId;
            tempList = Arrays.asList(
                    ((Configuration)
                            this.getCurrentConfigurations().stream().filter(predicate).findFirst().get())
                            .getConfigs());
            this.setCurrentConfigurationItems(FXCollections.observableArrayList(tempList));
            }
    }

    //get new project from testrail
    private Project downloadProject(int projectId) throws Exception {
        client = RailClient.getInstance();
        Project project = client.getOneInstance(projectId, Project.class);
        project.setPlans(client.getAllInstances(projectId, Plan.class));
        project.setSuites(client.getAllInstances(projectId, Suite.class));
        project.setConfigurations(client.getAllInstances(projectId, Configuration.class));
        return project;
    }

    //get new test suite from testrail
    private Suite downloadSuite(int suiteId) throws Exception {
        client=RailClient.getInstance();
        Suite suite = client.getOneInstance(suiteId, Suite.class);
        int[] params = new int[] {this.getSelectedProject(), suiteId};
        suite.setCases(client.getAllInstances(params, Case.class));
        return suite;
    }

    public ObservableList<ConfigurationItem> getCurrentConfigurationItems() {
        return currentConfigurationItems;
    }

    public void setCurrentConfigurationItems(ObservableList<ConfigurationItem> currentConfigurationItems) {
        this.currentConfigurationItems = currentConfigurationItems;
    }

    public ObservableList getCurrentProjects() throws Exception {

        if(currentProjects==null)
           currentProjects = (ObservableList<Project>)
                   FXCollections.observableArrayList(
                           RailClient.getInstance().getAllInstances(null, Project.class));
        return currentProjects;
    }

    public void setCurrentProjects(ObservableList currentProjects) {
        this.currentProjects = currentProjects;
    }

    public ObservableList<Plan> getCurrentPlans() {
        return currentPlans;
    }

    public void setCurrentPlans(ObservableList<Plan> currentPlans) {
        this.currentPlans = currentPlans;
    }

    public ObservableList<Suite> getCurrentSuites() {
        return currentSuites;
    }

    public void setCurrentSuites(ObservableList<Suite> currentSuites) {
        this.currentSuites = currentSuites;
    }

    public ObservableList<Case> getCurrentCases() {
        return currentCases;
    }

    public void setCurrentCases(ObservableList<Case> currentCases) {
        this.currentCases = currentCases;
    }

    public ObservableList<Configuration> getCurrentConfigurations() {
        return currentConfigurations;
    }

    public void setCurrentConfigurations(ObservableList<Configuration> currentConfigurations) {
        this.currentConfigurations = currentConfigurations;
    }

    public int getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(int selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    public int getSelectedCase() {
        return selectedCase;
    }

    public void setSelectedCase(int selectedCase) {
        this.selectedCase = selectedCase;
    }

    public int getSelectedConfigurationItem() {
        return selectedConfigurationItem;
    }

    public void setSelectedConfigurationItem(int selectedConfigurationItem) {
        this.selectedConfigurationItem = selectedConfigurationItem;
    }

    public void resetCache() {
        for(Map.Entry<Integer, Long> entry : entityTime.entrySet())
            entityTime.put(entry.getKey(), 0L);

    }


}
