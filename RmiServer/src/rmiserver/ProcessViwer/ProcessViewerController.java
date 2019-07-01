/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.ProcessViwer;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import rmiclient.Client.CallBackInterface;

/**
 * FXML Controller class
 *
 * @author ABDULLAH
 */
public class ProcessViewerController implements Initializable {

    @FXML
    private ListView processList;

    @FXML
    private Label clientName;
    @FXML
    private Button buttonRefresh;

    protected ListProperty<String> listProperty = new SimpleListProperty<>();

    public List<String> values;
    private CallBackInterface client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tooltipInit();
    }

    @FXML
    private void refreshProcess() throws RemoteException {
        String[] p = client.getProcesses().split(",");
        List<String> values = Arrays.asList(p);
        listProperty.set(FXCollections.observableArrayList(values));
        processList.itemsProperty().bind(listProperty);
    }

    void setList(CallBackInterface client, String clientName) throws RemoteException {

        this.client = client;
        String processes = client.getProcesses();
        System.out.println(processes);
        String[] p = processes.split(",");
        List<String> values = Arrays.asList(p);
        listProperty.set(FXCollections.observableArrayList(values));
        processList.itemsProperty().bind(listProperty);
        this.clientName.setText(clientName);
    }

    private void tooltipInit() {
        Tooltip tProcessList = new Tooltip("List the processes of the selected Client");
        tProcessList.setFont(new Font("Arial", 12));
        tProcessList.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tButtonRefresh = new Tooltip("Refresh Process");
        tButtonRefresh.setFont(new Font("Arial", 12));
        tButtonRefresh.setStyle("-fx-background-color:yellow;-fx-text-fill:black");

        processList.setTooltip(tProcessList);
        buttonRefresh.setTooltip(tButtonRefresh);
    }

}
