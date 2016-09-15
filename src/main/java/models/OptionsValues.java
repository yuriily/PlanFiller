package models;

/**
 * Created by yuriily on 26-Aug-16.
 */
public final class OptionsValues {
    public final String OPTIONS_FILE_PATH = "options.properties";
    private String railsURL;
    private String username;
    private String password;
    private int lastProject;
    private int lastSuite;
    private int lastConfiguration;


    private static final OptionsValues instance = new OptionsValues();
    private OptionsValues() {}

    public static OptionsValues getInstance() {
        return instance;
    }

    public String getRailsURL() {
        return railsURL;
    }

    public void setRailsURL(String railsURL) {
        this.railsURL = railsURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLastProject() {
        return lastProject;
    }

    public void setLastProject(int lastProject) {
        this.lastProject = lastProject;
    }

    public int getLastSuite() {
        return lastSuite;
    }

    public void setLastSuite(int lastSuite) {
        this.lastSuite = lastSuite;
    }

    public int getLastConfiguration() {
        return lastConfiguration;
    }

    public void setLastConfiguration(int lastConfiguration) {
        this.lastConfiguration = lastConfiguration;
    }
}
