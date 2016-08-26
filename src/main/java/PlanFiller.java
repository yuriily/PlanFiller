/**
 * Created by yuriily on 25-Aug-16.
 */

import javafx.application.Application;
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
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("options.fxml"));
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
        optionsScene = new Scene(options,600,600);

        //trust all certificates to enable it working with corporate proxy
        trustEveryone();

        //try to load config
        if(loadOptions(OptionsValues.getInstance().OPTIONS_FILE_PATH)) {

            //todo try to connect with existing credentials; skip if config is empty
            if(connectToRails()) {
                //todo load data into model

            } else {
                //todo if it's not, try to open last known project and suite and configuration
            }
        }

        primaryStage.setScene(mainScene);
        primaryStage.show();

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
            OptionsValues.getInstance().setLastProject(Integer.parseInt(props.getProperty("lastProject")));
            OptionsValues.getInstance().setLastSuite(Integer.parseInt(props.getProperty("password")));
            OptionsValues.getInstance().setLastConfiguration(Integer.parseInt(props.getProperty("password")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    private boolean connectToRails() {
        return true;
    }
}
