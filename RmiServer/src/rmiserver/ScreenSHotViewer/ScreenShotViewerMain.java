/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.ScreenSHotViewer;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rmiclient.Client.CallBackInterface;

/**
 *
 * @author ABDULLAH
 */
public class ScreenShotViewerMain {

    private Stage primaryStage;
    private CallBackInterface client;
    private String clientName;

    public void start(CallBackInterface client, String clientName) throws IOException {
        this.client = client;
        this.clientName = clientName;
        primaryStage = new Stage();
        primaryStage.initModality(Modality.WINDOW_MODAL);
        mainWindow();
    }

    private void mainWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("screenShotViewer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Screenshot Viewer");
        primaryStage.setScene(scene);
        ScreenShotViewerController controller = loader.getController();
        controller.setImage(client, clientName);
        primaryStage.show();
    }

}
