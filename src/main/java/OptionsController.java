import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by yuriily on 25-Aug-16.
 */
public class OptionsController {
    private Stage primaryStage;

    @FXML
    public TextField urlTextField;
    @FXML
    public TextField loginTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public Button saveOptionsButton;
    @FXML
    public Button closeOptionsButton;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {

        closeOptionsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                urlTextField.getScene().getWindow().hide();
            }
        });

        saveOptionsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if(saveOptions())
                    closeOptionsButton.fire();
                else {
                    //todo tell user something wrong has happened
                }
            }
        });
    }

    private boolean saveOptions() {
        //todo add checking the connection
        OptionsValues.getInstance().setRailsURL(urlTextField.getText());
        OptionsValues.getInstance().setUsername(loginTextField.getText());
        OptionsValues.getInstance().setPassword(passwordTextField.getText());

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("somehardcodedpassword");

        //write the properties - ignore current plan, suite and config
        Properties props = new Properties();
        props.setProperty("railsURL", urlTextField.getText());
        props.setProperty("username", loginTextField.getText());
        props.setProperty("password", "ENC("+encryptor.encrypt(passwordTextField.getText()) +")");

        FileOutputStream file;

        try {
            file = new FileOutputStream(OptionsValues.getInstance().OPTIONS_FILE_PATH);
            props.store(file, "Saved from Options dialog");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;


    }
}
