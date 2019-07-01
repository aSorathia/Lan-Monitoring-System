package rmiserver.Server.Settings;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import rmiserver.utility.InetAdrUtility;
import javafx.util.Pair;
import rmiserver.utility.DialogBoxes;

/**
 *
 * @author ABDULLAH
 */
public class Configuration {

    public static void serverConfiguration() {
        Config.loadConfiguration();

        ChoiceBox serverAddress = new ChoiceBox();
        serverAddress.getItems().addAll(FXCollections.observableArrayList(InetAdrUtility.getLocalIPAdresses()));
        serverAddress.getSelectionModel().select(Config.server_address);
        TextField serverPort = new TextField();
        serverPort.setText(Integer.toString(Config.server_port));

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Server Configuration");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Address:"), 0, 0);
        grid.add(serverAddress, 1, 0);
        grid.add(new Label("Port:"), 0, 1);
        grid.add(serverPort, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {

                Config.server_address = serverAddress.getSelectionModel().getSelectedItem().toString();
                int server_port = Integer.valueOf(serverPort.getText());
                if (server_port < 1 || server_port > 65535) {
                    server_port = 6666;
                    new DialogBoxes().createAlert(
                            "Error",
                            "Port Range Invalid..!!",
                            "The port Number should be in range of 1-65535."
                            + " Setting default Port 6666",
                            Alert.AlertType.ERROR).showAndWait();
                }
                Config.server_port = server_port;
                Config.storeConfiguration();
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
    }

}
