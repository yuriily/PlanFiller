package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
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

    //todo move all prefixes hashmaps out of the methods

    private RailClient() {
    }

    public static RailClient getInstance() {
        return instance;
    }


    public boolean connect() {
        OptionsValues optionsValues = OptionsValues.getInstance();
        if(optionsValues.getRailsURL() != null
                && optionsValues.getUsername() != null
                && optionsValues.getPassword() != null
                && !optionsValues.getRailsURL().isEmpty()) {
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

    //todo this should be deleted - now cannot cast int to int[] in a one-liner
    public <T> List<T> getAllInstances(int parameterId, Class<T> instanceClass) throws Exception {
        int[] param = new int[1];
        param[0] = parameterId;
        return getAllInstances(param, instanceClass);
    }

    public <T> List<T> getAllInstances(int[] parameterId, Class<T> instanceClass) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
        if(parameterId.length>1)
            prefixes.put(Case.class, "get_cases/"+parameterId[0]+"&suite_id="+parameterId[1]);

        String prefix = prefixes.get(inst.getClass());

        if(null!=prefix && !prefix.isEmpty()) {
            System.out.println("Requesting TestRail with: '"+prefix+"'");
            ArrayList<T> results = mapper.readValue(client.sendGet(prefix).toString(), type);
            return results;
        }
        else {
            //our map doesn't have such things in it; maybe it should be updated
            System.out.println("I don't know how to request TestRail for item:'"+type.toString()+"'");

        }
        return null;

    }

    public <T extends TestRailsEntity> T getOneInstance(int parameterId, Class<T> instanceClass) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        T inst = instanceClass.newInstance();
        JavaType type = mapper.getTypeFactory().constructType(inst.getClass());

        if(0==parameterId) {
            //impossible - an id is needed for every element
            System.out.println("No ID was passed to get an instance of '"+type.toString()+"'");
        }

        Map<Class, String> prefixes = new HashMap<>();
        prefixes.put(Project.class, "get_project/"+parameterId);
        prefixes.put(Plan.class, "get_plan/"+parameterId);
        prefixes.put(Suite.class, "get_suite/"+parameterId);
        //todo check if we need to get one configuration only
        //prefixes.put(Configuration.class, "get_config/"+parameterId[0]);
        //[0] = projectId, [1] = suiteId, [2]=sectionId; [2] is not used now
        prefixes.put(Case.class, "get_case/"+parameterId);

        String prefix = prefixes.get(inst.getClass());

        if(null!=prefix && !prefix.isEmpty()) {
            System.out.println("Requesting TestRail with: '"+prefix+"'");
            String tmp = client.sendGet(prefix).toString();
            T results = mapper.readValue(tmp, type);
            return results;
        }
        else {
            //our map doesn't have such things in it; maybe it should be updated
            System.out.println("I don't know how to request TestRail for item:'"+type.toString()+"'");
        }
        return null;

    }

    public boolean deleteOneInstance(int parameterId, Class instanceClass) {
        Map<Class,String> prefixes = new HashMap<>();
        prefixes.put(ConfigurationItem.class, "delete_config/"+parameterId);
        String prefix = prefixes.get(instanceClass);
        try {
            System.out.println("Requesting TestRail with: '"+prefix+"'");
            //post request cannot accept second parameter as null, so we need to pass something there
            //empty map is OK
            Map<String,String> emptyMapForPost = new HashMap<>();
            emptyMapForPost.put("","");
            String results = client.sendPost(prefix, emptyMapForPost).toString();
            if(results.equals("{}"))
                System.out.println("Deleted successfully.");
            else {
                //todo print out some error
                //todo create a map with error codes
                //400 - invalid/unknown config
                //403 - no permissions
                //404 - no access
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    //temporary method - just for presentation; should be changed to allow adding of different complex entities
    //test plans, plan entries
    public String tempPostPlan(int planId, Map<String,Object> postMap) throws Exception {
        return formatJsonToPrettyString(client.sendPost("add_plan_entry/"+planId, postMap).toString());
    }

    public void addConfiguration(int projectId, Configuration configuration) {
        String result="";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //add new configuration; there can be multiple configs with the same name, so don't check for uniqueness
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("name", configuration.getName());
        try {
            result = client.sendPost("add_config_group/" + projectId, configMap).toString();
            //any erroneous response will be handled by APIException, so only good ones will hit the next lines
            Configuration newConfig = mapper.readValue(result, Configuration.class);
            System.out.println("Successfully added new configuration: "+ newConfig.getName());
            int newConfigId = newConfig.getId();
            //now add all its items one by one, duplicates are accepted too
            for(ConfigurationItem configItem : configuration.getConfigurationItems()) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("name", configItem.getName());
                itemMap.put("group_id", newConfigId);
                result = client.sendPost("add_config/" + newConfigId, itemMap).toString();
                ConfigurationItem newConfigItem = mapper.readValue(result, ConfigurationItem.class);
                System.out.println("New item added: " + newConfigItem.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String formatJsonToPrettyString(String jsonString) {
        if(!jsonString.contains("{"))
            return jsonString;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            Object json = mapper.readValue(jsonString, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed to format json string :(";
    }

}