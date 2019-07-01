/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;

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
import rmiserver.Server.Settings.SecurityMng;
import rmiserver.Server.Server;
import rmiserver.utility.DialogBoxes;
import rmiserver.utility.OSValidator;
import rmiserver.utility.ScriptLoader;

/**
 *
 * @author ABDULLAH
 */
public class RmiServer extends Application {

    private Stage primaryStage;
    public static String SERVER_CONFIG_FILE;
    public static String SERVER_DIR;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        mainWindow();
    }

    private void mainWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainFXMLDocument.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Optional<ButtonType> result = new DialogBoxes().createAlert(
                    "Confirmation Dialog",
                    "",
                    "Exit Application ?",
                    Alert.AlertType.CONFIRMATION).showAndWait();
            if (result.get() == ButtonType.OK) {
                RmiServer.exit();
            }
        });
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        String os = OSValidator.getOS();
        SERVER_DIR = getCurrentDirectory();
        if (os.equalsIgnoreCase("Linux")) {
            new File(System.getProperty("user.home") + "/screenshot").mkdir();
            ScriptLoader.CopyScripts();
        }
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityMng());
        }
        SERVER_CONFIG_FILE = getCurrentDirectory() + "server.config";
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

    public static void exit() {
        if (Server.isRunning()) {
            Server.Stop();
        }
        System.setSecurityManager(null);
        System.exit(0);
    }
}
