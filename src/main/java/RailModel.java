import data.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by yuriily on 29-Aug-16.
 */
public class RailModel {
    private final static int TIMEOUT_TIME = 60*60*1000;

    private List<Project> projects;
    private List<Plan> plans;
    private List<Suite> suites;
    private List<Case> cases;
    private List<Configuration> configurations;
    private List<ConfigurationItem> configurationItems;

    private int currentProject;
    private int currentPlan;
    private int currentSuite;
    private int[] currentConfigurations;
    //no access from outside for this field
    private long lastTimeUpdated;
    private RailClient client;

    private static RailModel ourInstance = new RailModel();

    public static RailModel getInstance() {
        return ourInstance;
    }

    private RailModel() {
    }

    //refresh cache if time has passed
    public void refresh() throws Exception {
        client=RailClient.getInstance();
        //if there is no project selected, no sense to refresh anything
        if(0==currentProject)
            return;

        //if there are no plans or suites yet, but the project was selected
        if(null==this.getPlans())
            this.setPlans(client.getAllInstances(this.getCurrentProject(), Plan.class));
        if(null==this.getSuites())
            this.setSuites(client.getAllInstances(this.getCurrentProject(), Suite.class));
        if(null==this.getConfigurations())
            this.setConfigurations(client.getAllInstances(this.getCurrentProject(), Configuration.class));

        //if suite was selected first time, get cases for it
        if(0!=currentSuite && null==this.getCases()){
            int[] params = new int[] {this.getCurrentProject(), this.getCurrentSuite(), 0 } ;
            this.setCases(client.getAllInstances(params, Case.class));
        }

        //if only one configuration was selected, refresh our configuration items each time
        //as it is not so time consuming
        if(null!=this.getConfigurations()) {
            if(null!=this.getCurrentConfigurations() && 1==this.getCurrentConfigurations().length) {
                List<ConfigurationItem> tempList;
                //find a configuration object by its id and extract all its items
                Predicate<Configuration> predicate = c -> c.getId() == this.getCurrentConfigurations()[0];
                tempList = Arrays.asList(((Configuration) this.getConfigurations().stream().filter(predicate).findFirst().get()).getConfigs());
                this.setConfigurationItems(tempList);
            }
        }

        //if one hour has passed from the last update
        //update everything except projects
        if(System.currentTimeMillis() - this.lastTimeUpdated > TIMEOUT_TIME) {
            this.setPlans(client.getAllInstances(this.getCurrentProject(), Plan.class));
            this.setSuites(client.getAllInstances(this.getCurrentProject(), Suite.class));
            this.setConfigurations(client.getAllInstances(this.getCurrentProject(), Configuration.class));
            this.lastTimeUpdated = System.currentTimeMillis();
        }

    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }

    public List<Suite> getSuites() {
        return suites;
    }

    public void setSuites(List<Suite> suites) {
        this.suites = suites;
    }

    public List<Case> getCases() throws Exception {
        if(currentSuite>0) {
            int[] params = new int[]{this.getCurrentProject(), this.getCurrentSuite(), 0};
            this.setCases(client.getAllInstances(params, Case.class));
        }
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public int getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(int currentProject) {
        this.currentProject = currentProject;
    }

    public int getCurrentSuite() {
        return currentSuite;
    }

    public void setCurrentSuite(int currentSuite) {
        this.currentSuite = currentSuite;
    }

    public int[] getCurrentConfigurations() {
        return currentConfigurations;
    }

    public void setCurrentConfigurations(int[] currentConfigurations) {
        this.currentConfigurations = currentConfigurations;
    }

    public List<ConfigurationItem> getConfigurationItems() {
        return configurationItems;
    }

    public void setConfigurationItems(List<ConfigurationItem> configurationItems) {
        this.configurationItems = configurationItems;
    }

    public int getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(int currentPlan) {
        this.currentPlan = currentPlan;
    }
}
