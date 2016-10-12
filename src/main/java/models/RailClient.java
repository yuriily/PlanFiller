package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import data.*;
import javafx.application.Platform;
import javafx.concurrent.Task;

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
            System.out.println("Cannot connect to testrail - URL, username or password are empty");
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
        //[0] = projectId, [1] = suiteId, [2]=sectionId; [2] is not used now
        prefixes.put(Configuration.class, "get_configs/"+parameterId[0]);
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
        //it is not possible now to retrieve one configuration only
        //[0] = projectId, [1] = suiteId, [2]=sectionId; [2] is not used now
        //prefixes.put(Configuration.class, "get_config/"+parameterId[0]);
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
                System.out.println(parseErrorMessage(results));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    //todo maybe return the id of created instance?
    public boolean createOneInstance(Map<String,Object> parameters, Class instanceClass, int parentId) {
        Map<Class,String> prefixes = new HashMap<>();
        //todo check if parent id is null - then nothing can be created except for projects; and we don't have access rights to create projects
        //todo add suite and test case, maybe
        prefixes.put(Plan.class, "add_plan/" + parentId);
        String prefix = prefixes.get(instanceClass);
        try {
            System.out.println("Requesting TestRail with: '"+prefix+"'");
            String results = client.sendPost(prefix, parameters).toString();
            if(results.contains("{"))
                System.out.println("Added successfully.");
            else {
                System.out.println(parseErrorMessage(results));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;

        }

        return true;
    }

    //temporary method - just for presentation; should be changed to allow adding of different complex entities
    //test plans, plan entries
    public String tempPostPlan(int planId, Map<String,Object> postMap) throws Exception {

        //this is a continuous task, so disable all UI before starting
        //show some progress

        ProgressForm progressForm = new ProgressForm();
        progressForm.setLabelText("Posting plan entries to TestRail");

        Task<String> task = new Task<String>() {
          @Override
            public String call() throws Exception {
                String tooLargeString = formatJsonToPrettyString(client.sendPost("add_plan_entry/"+planId, postMap).toString());
              return tooLargeString;
          }
        };

        progressForm.activateProgressBar(task);

        task.setOnSucceeded(e-> {
            System.out.println(task.getValue());
            progressForm.getDialogStage().close();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        progressForm.getDialogStage().show();
        thread.start();

        return "Updating plan has started. Please wait until results are shown here. \n " +
                "It can take about 5 minutes for 200 entry values";
    }


    public void addConfiguration(int projectId, Configuration configuration) {

        Task task = new Task<Void>() {
          @Override
            public Void call() throws Exception {

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

                      Platform.runLater(new Runnable() {
                          @Override
                          public void run() {
                              System.out.println("New item added: " + newConfigItem.getName());

                          }
                      });
                  }

              } catch (Exception e) {
                  e.printStackTrace();
              }
              return null;
          }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }


    public String formatJsonToPrettyString(String jsonString) {
        if(!jsonString.contains("{"))
            return jsonString;
        //if there are more than 200 objects which will spam the logs badly
        if(jsonString.length() - jsonString.replace("{", "").length() > 100)
            return "Request has returned very large response (more than 100 entries) and won't be shown. Please check TestRail for results.";
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

    public String parseErrorMessage(String resultFromRail) {
        String result="Unknown error occurred.";
        if(resultFromRail.contains("400") || resultFromRail.contains("404"))
            result = "Invalid or unknown instance was requested.";
        if(resultFromRail.contains("403"))
            result = "Restricted access to instance";

        return result;
    }

}
