/**
 * Created by yuriily on 26-Aug-16.
 */
public final class RailClient {
    public static final RailClient instance = new RailClient();

    private APIClient client;

    private RailClient() {
    }

    public static RailClient getInstance() {
        return instance;
    }


    public boolean connect() {
        if(OptionsValues.getInstance().getRailsURL() != null
                && OptionsValues.getInstance().getUsername() != null
                && OptionsValues.getInstance().getPassword() != null) {
            client = new APIClient(OptionsValues.getInstance().getRailsURL());
            client.setUser(OptionsValues.getInstance().getUsername());
            client.setPassword(OptionsValues.getInstance().getPassword());

        }
        else {
            System.out.println("Cannot connect to testrail - some parameter is empty");
            return false;
        }
        return true;
    }

    public String getProjects() throws Exception {
        return client.sendGet("get_projects").toString();
    }

//    public List<Project> getProjects() {}
}
