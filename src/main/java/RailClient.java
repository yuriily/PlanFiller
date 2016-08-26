import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;
import data.*;

import java.util.ArrayList;
import java.util.List;

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

    //todo change to generic getAllInstances
    public List<Project> getProjects() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = client.sendGet("get_projects").toString();

        List<Project> projectList = mapper.readValue(jsonString, new TypeReference<List<Project>>() {});

        return projectList;
    }

    //todo change to generic getAllInstances
    public List<Suite> getSuites(int projectId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = client.sendGet("get_suites/"+((Integer)projectId).toString()).toString();

        List<Suite> suiteList = mapper.readValue(jsonString, new TypeReference<List<Suite>>() {});

        //todo check if there are no suites - will the listview be refreshed correctly?

        return suiteList;

    }

    public <T> List<T> getAllInstances(int parameterId, Class<T> instanceClass) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String prefix="";
        T inst = instanceClass.newInstance();

        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, inst.getClass());
        System.out.println(type.toString());

        if(inst instanceof Plan)
            prefix="get_plans/"+parameterId;

        ArrayList<T> results = mapper.readValue(client.sendGet(prefix).toString(), type);
        return results;

    }

}
