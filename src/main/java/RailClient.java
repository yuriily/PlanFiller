import com.codepine.api.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;
import data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public <T> List<T> getAllInstances(int[] parameterId, Class<T> instanceClass) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String prefix="";
        T inst = instanceClass.newInstance();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, inst.getClass());

        if(null==parameterId) {
            //it was a project getter
            parameterId=new int[]{0,0,0};
        }

        Map<Class, String> prefixes = new HashMap<>();
        prefixes.put(Project.class, "get_projects");
        prefixes.put(Plan.class, "get_plans/"+parameterId[0]);
        prefixes.put(Suite.class, "get_suites/"+parameterId[0]);
        prefixes.put(Configuration.class, "get_configs/"+parameterId[0]);
        //[0] = projectId, [1] = suiteId, [2]=sectionId; [2] is not used now
        prefixes.put(Case.class, "get_cases/"+parameterId[0]+"&suite_id="+parameterId[1]);

        prefix = prefixes.get(inst.getClass());

        if(null!=prefix && !prefix.isEmpty()) {
            ArrayList<T> results = mapper.readValue(client.sendGet(prefix).toString(), type);
            return results;
        }
        else {
            //todo write out that map doesn't have such things in it
        }
        return null;

    }

}
