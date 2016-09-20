package models; /**
 * Created by yuriily on 25-Aug-16.
 */

import data.Project;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

public class PlanFiller extends Application {
    private Stage currentStage;
    private Scene mainScene, optionsScene;
    private PlanFillerController planFillerController;
    private OptionsController optionsController;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("/options.fxml"));
        Parent root = mainLoader.load();
        Parent options = optionsLoader.load();
        planFillerController = (PlanFillerController) mainLoader.getController();
        optionsController = (OptionsController) optionsLoader.getController();

        currentStage = primaryStage;
        planFillerController.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Rails UI");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(1200);
        mainScene = new Scene(root, 1200, 600);
        mainScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        optionsScene = new Scene(options,600,600);
        optionsScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        boolean isConnectionFailed = true;

        //trust all certificates to enable it working with corporate proxy
        trustEveryone();

        primaryStage.setScene(mainScene);
        primaryStage.show();

        //try to load config from file
        while(!loadOptions(OptionsValues.getInstance().OPTIONS_FILE_PATH)) {
            System.out.println("Cannot load configuration file. Please add new configuration.");
            planFillerController.optionsButton.fire();
        }

        //try to connect with existing credentials; skip if config is empty
        if(RailClient.getInstance().connect()) {
                //load data into model
                //load only the list of projects - other lists will be filled when project will be selected from list
                isConnectionFailed=false;
                System.out.println("Filling project list...");
                planFillerController.testProjectList.setItems((ObservableList<Project>)
                        FXCollections.observableArrayList(RailClient.getInstance().getAllInstances(null, Project.class)));


            } else {
                //cannot connect now; nothing to show
                System.out.println("Cannot connect to TestRail. Check your Internet connection or TestRails parameters.");
            }


    }

    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    private boolean loadOptions(String path) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("somehardcodedpassword");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        Properties props = new EncryptableProperties(encryptor);
        FileInputStream file;

        //if the options file was not created yet, or was deleted during the session
        try {
            file = new FileInputStream(path);

        } catch (FileNotFoundException e) {
            OptionsValues.getInstance().setRailsURL(null);
            OptionsValues.getInstance().setUsername(null);
            OptionsValues.getInstance().setPassword(null);
            return false;
        }

        //load properties
        try {
            props.load(file);
            OptionsValues.getInstance().setRailsURL(props.getProperty("railsURL"));
            OptionsValues.getInstance().setUsername(props.getProperty("username"));
            OptionsValues.getInstance().setPassword(props.getProperty("password"));
            if (null != props.getProperty("lastProject"))
                OptionsValues.getInstance().setLastProject(Integer.parseInt(props.getProperty("lastProject")));
            if (null != props.getProperty("lastSuite"))
                OptionsValues.getInstance().setLastSuite(Integer.parseInt(props.getProperty("lastSuite")));
            if (null != props.getProperty("lastConfiguration"))
            OptionsValues.getInstance().setLastConfiguration(Integer.parseInt(props.getProperty("lastConfiguration")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

}
