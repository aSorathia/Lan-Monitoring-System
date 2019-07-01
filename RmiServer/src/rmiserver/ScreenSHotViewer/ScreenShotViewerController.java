/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.ScreenSHotViewer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;
import rmiclient.Client.CallBackInterface;
import static rmiserver.RmiServer.SERVER_DIR;
import rmiserver.utility.DialogBoxes;

/**
 * FXML Controller class
 *
 * @author ABDULLAH
 */
public class ScreenShotViewerController implements Initializable {

    private BufferedImage capture;
    private CallBackInterface client;
    @FXML
    private Label clientName;
    @FXML
    private ImageView im;
    @FXML
    private ChoiceBox cb;

    private String fileName;
    @FXML
    private Label fName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb.setItems(FXCollections.observableArrayList("jpg", "png", "bmp"));
        cb.getSelectionModel().select("jpg");
        tooltip();
    }

    @FXML
    private void saveImage() throws IOException {
        try {
            String format = cb.getSelectionModel().getSelectedItem().toString();
            fileName = clientName.getText().replace("/", "-") + "-" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
            ImageIO.write(capture, format, new File(SERVER_DIR + "screenshot/" + fileName + "." + format));
        } catch (Exception e) {
            new DialogBoxes().exceptionBox(
                    new DialogBoxes().createAlert(
                            "Error",
                            "An Exception occured!!",
                            e.getMessage(),
                            Alert.AlertType.ERROR),
                    e
            );
        }

    }

    @FXML
    private void refreshImage() throws RemoteException, IOException {
        capture = javax.imageio.ImageIO.read(new ByteArrayInputStream(client.getScreenShot()));
        fileName = clientName.getText().replace("/", "-") + "-" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        fName.setText(fileName);
        Image image = SwingFXUtils.toFXImage(capture, null);
        im.setImage(image);
    }

    void setImage(CallBackInterface client, String clientName) throws RemoteException, IOException {
        this.client = client;
        this.clientName.setText(clientName);
        capture = javax.imageio.ImageIO.read(new ByteArrayInputStream(client.getScreenShot()));
        fileName = clientName.replace("/", "-") + "-" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        fName.setText(fileName);
        Image image = SwingFXUtils.toFXImage(capture, null);
        im.setImage(image);
    }

    private void tooltip() {

        Tooltip tClientName = new Tooltip("Name of the connected Client");
        tClientName.setFont(new Font("Arial", 12));
        tClientName.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tCb = new Tooltip("Select the format of output file");
        tCb.setFont(new Font("Arial", 12));
        tCb.setStyle("-fx-background-color:yellow;-fx-text-fill:black");
        Tooltip tFName = new Tooltip("Name of the output file");
        tFName.setFont(new Font("Arial", 12));
        tFName.setStyle("-fx-background-color:yellow;-fx-text-fill:black");

        clientName.setTooltip(tClientName);
        cb.setTooltip(tCb);
        fName.setTooltip(tFName);
    }

}
