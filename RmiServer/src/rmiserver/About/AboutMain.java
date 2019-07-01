/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.About;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author ABDULLAH
 */
public class AboutMain {

    private Stage primaryStage;

    public void start() throws IOException {
        this.primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        mainWindow();
    }

    private void mainWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AboutFXML.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("About");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
