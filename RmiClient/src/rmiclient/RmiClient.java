/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import rmiclient.Client.Client;
import rmiclient.Client.Settings.SecurityMng;
import rmiclient.utility.OSValidator;
import rmiclient.utility.ScriptLoader;

/**
 *
 * @author ABDULLAH
 */
public class RmiClient extends Application {

    private Stage primaryStage;
    public static String CLIENT_CONFIG_FILE;
    public static String CHECKER_SCRIPT_FILE;
    public static String CLIENT_DIR;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        mainWindow();
    }

    private void mainWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainFXMLDocument.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            cExit();
        });
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        CLIENT_CONFIG_FILE = getCurrentDirectory() + "client.config";
        CLIENT_DIR = getCurrentDirectory();
        CHECKER_SCRIPT_FILE = getCurrentDirectory() + "pkgChecker.sh";
        String os = OSValidator.getOS();
        if (os.equalsIgnoreCase("Linux")) {
            ScriptLoader.installProcessScript();
            if (!ScriptLoader.packageChecker()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Package Not found");
                alert.setHeaderText("Please Install Package 'wmctrl' using \n "
                        + "apt-get wmctrl");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    System.setSecurityManager(null);
                    System.exit(0);
                }
            }
        }
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityMng());
        }

        System.getProperties().remove("java.rmi.server.hostname");

        launch(args);
    }

    public static String getCurrentDirectory() {
        String currentDirectory = null;
        try {
            currentDirectory = new File(".").getCanonicalPath() + File.separatorChar;
        } catch (IOException e) {
            e.getStackTrace();
        }
        return currentDirectory;
    }

    public static void cExit() {
        if (Client.isConnected()) {
            Client.cStop();
        }
        System.setSecurityManager(null);
        System.exit(0);
    }

    public static void startViewer() {
        Client.Start();
    }
}
