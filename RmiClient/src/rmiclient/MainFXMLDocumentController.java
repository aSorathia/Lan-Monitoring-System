/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import rmiclient.Client.Client;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import rmiclient.Client.Settings.Config;

/**
 * FXML Controller class
 *
 * @author ABDULLAH
 */
public class MainFXMLDocumentController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label index;
    @FXML
    private TextField serverIp;
    @FXML
    private TextField serverPort;
    @FXML
    private Label status;
    @FXML
    private Button startButton;

    @FXML
    private void cExit() {
        RmiClient.cExit();
    }

    public void changeIndex(int i) {
        index.setText(Integer.toString(i));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Client.initializeFXML(this);
        Config.loadConfiguration();
        serverIp.setText(Config.server_address);
        serverPort.setText(Integer.toString(Config.server_port));
    }

    @FXML
    private void startServer() {
        boolean isConnected = Client.Start();
        if (isConnected) {
            status.setText("Connected");
            Color c = Color.web("#6aa71f");
            status.setTextFill(c);
            startButton.setDisable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error!!!");
            alert.setHeaderText("There was an error connecting to server.");
            alert.setContentText("Please check whether Server is running or \ncheck whether you have enter the correct \nIP address and port Number of the server");
            alert.showAndWait();
            status.setText("Not Connected");
            Color c = Color.web("#ff0000");
            status.setTextFill(c);
        }
    }

    @FXML
    private void setServerip() {
        String sIp = serverIp.getText();
        int sPort = Integer.parseInt(serverPort.getText());
        if (!"".equals(sIp) && sPort != 0) {
            Config.server_address = sIp;
            Config.server_port = sPort;
            Config.storeConfiguration();
        }
    }

    @FXML
    public void displayServerMessage(String sMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Server Message");
                        alert.setHeaderText("Server message Recieved on "
                                + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
                        );
                        alert.setContentText(sMessage);
                        Optional<ButtonType> result = alert.showAndWait();
                    }
                });
            }
        }).start();
    }

    @FXML
    public void resetStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setDisable(false);
                        status.setText("Not Connected");
                        Color c = Color.web("#ff0000");
                        status.setTextFill(c);
                        changeIndex(-1);
                    }
                });
            }
        }).start();
    }
}
