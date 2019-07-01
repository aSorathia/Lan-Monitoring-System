/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.RemoteException;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import rmiserver.Server.Server;
import rmiserver.Server.Settings.Config;
import rmiserver.utility.DialogBoxes;
import rmiserver.Server.Settings.Configuration;

import rmiserver.model.ConnectionModel;

/**
 * FXML Controller class
 *
 * @author ABDULLAH
 */
public class MainFXMLDocumentController implements Initializable {

    private static final boolean idle = true;
    private static final boolean running = false;

    @FXML
    private TableView<ConnectionModel> connectionList;
    @FXML
    private TableColumn<ConnectionModel, Integer> cIndex;
    @FXML
    private TableColumn<ConnectionModel, String> cAddress;
    @FXML
    private TableColumn<ConnectionModel, String> cPort;

    @FXML
    private Label serverIP;
    @FXML
    private Label serverPort;
    @FXML
    private TextArea textAreaStatus;
    @FXML
    private Button buttonSendMsg;
    @FXML
    private Button buttonSs;
    @FXML
    private Button buttonProcess;
    @FXML
    private Button buttonbroadcast;
    @FXML
    private Button buttonInfo;
    @FXML
    private Button buttonConfig;
    @FXML
    private Button buttonAbout;
    @FXML
    private Button buttonExit;
    @FXML
    private Button buttonStartStop;

    private final IntegerProperty tableIndex = new SimpleIntegerProperty();
    /**
     * Initializes the controller class.
     */
    final ObservableList<ConnectionModel> connectionData = FXCollections.observableArrayList( //new ConnectionModel(0, "Desktop/22dafdsf", "6666")   
            );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableIndex.set(-1);
        Config.loadConfiguration();
        updateStatus();
        Server.initializeFXML(this);
        cIndex.setCellValueFactory(new PropertyValueFactory<>("Index"));
        cAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        cPort.setCellValueFactory(new PropertyValueFactory<>("Port"));
        connectionList.setItems(connectionData);

        connectionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                tableIndex.set(connectionData.indexOf(newValue));
                System.out.println(connectionData.indexOf(newValue));
            }
        });
        tooltipInit();
    }

    public void updateStatus() {
        if (Server.isRunning()) {
            textAreaStatus.setText(Server.getStatus());
            buttonStartStop.setText("Stop Server");
            serverIP.setText(Config.server_address);
            serverPort.setText(Integer.toString(Config.server_port));
        } else {
            serverIP.setText(Config.server_address);
            serverPort.setText(Integer.toString(Config.server_port));
            if (Server.isIdle()) {
                textAreaStatus.setText("Idle.");
            } else {
                textAreaStatus.setText("Stopped.");
            }
            buttonStartStop.setText("Start Server");
        }
    }

    @FXML
    private void buttonStartStopAction() {
        if (Server.isRunning()) {
            Server.Stop();
            Server.removeAllClient();
        } else {
            Server.Start();
        }
        updateStatus();
    }

    @FXML
    private void buttonExitAction() {
        Optional<ButtonType> result = new DialogBoxes().createAlert(
                "Confirmation Dialog",
                "",
                "Exit Application ?",
                AlertType.CONFIRMATION).showAndWait();
        if (result.get() == ButtonType.OK) {
            RmiServer.exit();
        }
    }

    @FXML
    private void buttonConfigAction() {
        Configuration.serverConfiguration();
        updateStatus();
    }

    public void removeAllClient() {
        connectionData.clear();
    }

    public synchronized void clientRemoveViewer(int index) {
        for (ConnectionModel cm : connectionData) {
            if (cm.getIndex() == index) {
                connectionData.remove(connectionData.indexOf(cm));
            }
        }
    }

    public void addViewer(int index, InetAddress inetAddress) {
        ConnectionModel cm = new ConnectionModel(index, inetAddress.toString(), "6666");
        connectionData.add(cm);
    }

    @FXML
    private void printHello() throws RemoteException {
        Server.printH(connectionList.getSelectionModel().getSelectedItem().getIndex());
    }

    @FXML
    private void buttonSendMessage() {
        try {
            Optional<String> result = new DialogBoxes().createMessage("send Message",
                    "Send Message to " + connectionList.getSelectionModel().getSelectedItem().getAddress(),
                    "Enter Your Message"
            ).showAndWait();
            result.ifPresent(
                    message -> {
                        try {
                            Server.sendMessage(message, connectionList.getSelectionModel().getSelectedItem().getIndex());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (Exception e) {
            new DialogBoxes().createAlert(
                    "Error!!",
                    "There was an error.",
                    "Please select proper remote pc from active"
                    + "connection and try again ?",
                    AlertType.ERROR).showAndWait();
        }

    }

    @FXML
    private void buttonBroadcast() {
        try {
            Optional<String> result = new DialogBoxes().createMessage("Send Broadcast",
                    "Broadcast Message to all the connected PC ",
                    "Enter Your Message"
            ).showAndWait();
            result.ifPresent(
                    message -> {
                        try {
                            Server.sendBroadcastMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (Exception e) {
            new DialogBoxes().createAlert(
                    "Error!!",
                    "There was an error.",
                    "Please select proper remote pc from active"
                    + "connection and try again ?",
                    AlertType.ERROR).showAndWait();
        }

    }

    @FXML
    private void buttonProcessList() throws RemoteException, IOException {
        try {
            Server.getProcess(connectionList.getSelectionModel().getSelectedItem().getIndex());
        } catch (Exception e) {
            new DialogBoxes().createAlert(
                    "Error!!",
                    "There was an error.",
                    "Please select proper remote pc from active"
                    + "connection and try again ?",
                    AlertType.ERROR).showAndWait();
        }

    }

    @FXML
    private void getScreenShot() throws RemoteException, IOException {
        try {
            Server.getScreenShot(connectionList.getSelectionModel().getSelectedItem().getIndex());
        } catch (Exception e) {
            new DialogBoxes().createAlert(
                    "Error!!",
                    "There was an error.",
                    "Please select proper remote pc from active"
                    + "connection and try again ?",
                    AlertType.ERROR).showAndWait();
        }
    }

    @FXML
    private void showAbout() throws IOException {
        Server.showAbout();
    }

    @FXML
    private void getSystemInfo() {
        try {
            Server.getSystemInfo(connectionList.getSelectionModel().getSelectedItem().getIndex());
        } catch (Exception e) {            
            new DialogBoxes().createAlert(
                    "Error!!",
                    "There was an error.",
                    "Please select proper remote pc from active"
                    + "connection and try again ?",
                    AlertType.ERROR).showAndWait();
            e.printStackTrace();
        }
    }

    //ToolTips
    private void tooltipInit() {
        //Tootltips
        Tooltip tTextArea = new Tooltip("Displays the status of the Server");
        tTextArea.setFont(new Font("Arial", 12));
        tTextArea.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tServerIp = new Tooltip("Ip Address of the Server");
        tServerIp.setFont(new Font("Arial", 12));
        tServerIp.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tServerPort = new Tooltip("port number of the Server");
        tServerPort.setFont(new Font("Arial", 12));
        tServerPort.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tActiveConnection = new Tooltip("Displays the List of all connected Clients");
        tActiveConnection.setFont(new Font("Arial", 12));
        tActiveConnection.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tSndMsg = new Tooltip("Send message to the Selected client");
        tSndMsg.setFont(new Font("Arial", 12));
        tSndMsg.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tSs = new Tooltip("Get Screenshot of Selected client");
        tSs.setFont(new Font("Arial", 12));
        tSs.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tProcess = new Tooltip("Displays the process of Selected Client");
        tProcess.setFont(new Font("Arial", 12));
        tProcess.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tbroad = new Tooltip("Send message to all the connecetd Client");
        tbroad.setFont(new Font("Arial", 12));
        tbroad.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tInfo = new Tooltip("Show the detail hardware info of the Selected Client");
        tInfo.setFont(new Font("Arial", 12));
        tInfo.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tStartS = new Tooltip("Start Server");
        tStartS.setFont(new Font("Arial", 12));
        tStartS.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tConfig = new Tooltip("Configure Server");
        tConfig.setFont(new Font("Arial", 12));
        tConfig.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tAbout = new Tooltip("About");
        tAbout.setFont(new Font("Arial", 12));
        tAbout.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tExit = new Tooltip("Quit Application");
        tExit.setFont(new Font("Arial", 12));
        tExit.setStyle("-fx-background-color:yellow;-fx-text-fill:black");

        textAreaStatus.setTooltip(tTextArea);
        serverIP.setTooltip(tServerIp);
        serverPort.setTooltip(tServerPort);
        connectionList.setTooltip(tActiveConnection);
        buttonSendMsg.setTooltip(tSndMsg);
        buttonSs.setTooltip(tSs);
        buttonProcess.setTooltip(tProcess);
        buttonbroadcast.setTooltip(tbroad);
        buttonInfo.setTooltip(tInfo);
        buttonStartStop.setTooltip(tStartS);
        buttonConfig.setTooltip(tConfig);
        buttonAbout.setTooltip(tAbout);
        buttonExit.setTooltip(tExit);
    }
}
