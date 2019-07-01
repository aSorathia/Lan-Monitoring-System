/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.SystemViewer;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import rmiclient.Client.CallBackInterface;
import rmiclient.Client.SystemInfoObject;

/**
 * FXML Controller class
 *
 * @author ABDULLAH
 */
public class SystemInfoViewerController implements Initializable {

    private CallBackInterface client;
    @FXML
    private Label clientName;
    //Processor
    @FXML
    private Label processor;
    @FXML
    private Label phyCPU;
    @FXML
    private Label logCPU;
    @FXML
    private Label processorID;

    //memory
    @FXML
    private ProgressBar memoProgressBar;
    @FXML
    private Label memoryLabel;

    //FileSystem
    @FXML
    private BorderPane fsAnchorPane;
    private GridDisplay gridDisplay;
    //networkInterfaces
    @FXML
    private TextArea niTextArea;

    //usb
    @FXML
    private TextArea usbTextArea;
    private SystemInfoObject sio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void refreshMemory() throws RemoteException {
        setMemory(client.getMemory());
    }

    @FXML
    private void refreshusb() throws RemoteException {
        setUsb(client.getUsb());
    }

    @FXML
    private void refreshNi() throws RemoteException {
        setNi(client.getNi());
    }

    @FXML
    private void refreshFs() throws RemoteException {
        setFs(client.getFs());
    }

    void setSystemInfoObject(CallBackInterface client, String clientName) throws RemoteException {
        this.client = client;
        this.sio = client.getSystemInfo();
        setProcessor(sio.getProcessor());
        setMemory(sio.getMemory());
        setUsb(sio.getUsbDevices());
        setNi(sio.getNetworkinterfaces());
        setFs(sio.getFileSystem());
    }

    void setProcessor(String s) {
        System.out.println(s);
        String[] pInfo = s.split(":");
        processor.setText(pInfo[0]);
        phyCPU.setText(pInfo[1]);
        logCPU.setText(pInfo[2]);
        processorID.setText(pInfo[3] + ": " + pInfo[4]);
    }

    void setMemory(String s) {
        String[] t = s.split("/");
        double free = Double.parseDouble(t[0].split(" ")[0]);
        double avail = Double.parseDouble(t[1].split(" ")[0]);
        if (t[0].split(" ")[1].equalsIgnoreCase("Gib")) {
            memoProgressBar.setProgress((free - 0.0) / (avail - 0.0));
        } else {
            memoProgressBar.setProgress(((free / 1024) - 0.0) / (avail - 0.0));
        }
        memoryLabel.setText(s);
    }

    void setUsb(String s) {
        usbTextArea.setText(s);
    }

    void setNi(String s) {
        niTextArea.setText(s);
    }

    void setFs(String s) {
        //
        String[] fs = s.split("!");
        int ta = (int) Math.ceil(fs.length / 3.0);
        gridDisplay = new GridDisplay(ta, 3, fs);
        fsAnchorPane.setCenter(gridDisplay.getDisplay());
        fsAnchorPane.setPadding(new Insets(10));
    }

    public final class GridDisplay {

        private static final double ELEMENT_SIZE = 100;
        private static final double GAP = ELEMENT_SIZE / 10;

        private final TilePane tilePane = new TilePane();
        private final Group display = new Group(tilePane);
        private int nRows;
        private int nCols;

        public GridDisplay(int nRows, int nCols, String[] fs) {
            tilePane.setHgap(GAP);
            tilePane.setVgap(GAP);
            setColsRows(nRows, nCols, fs);
        }

        public void setColsRows(int newRows, int newColumns, String[] fs) {
            nRows = newRows;
            tilePane.setPrefRows(nRows);
            nCols = newColumns;
            tilePane.setPrefColumns(nCols);
            createElements(fs);
        }

        public Group getDisplay() {
            return display;
        }

        private void createElements(String[] fs) {
            tilePane.getChildren().clear();
            for (String t : fs) {
                tilePane.getChildren().add(createElement(t.split(",")));
            }
        }

        private VBox createElement(String[] t) {
            VBox vBox = new VBox();
            vBox.setPadding(new Insets(10));
            //vBox.setSpacing(10);           
            vBox.setStyle("-fx-border-style: solid inside;"
                    + "-fx-border-width: 4;"
                    + "-fx-border-radius: 5;"
                    + "-fx-background-color: #1098E6;"
                    + "-fx-border-color: black");
            String labelStyle = "-fx-text-fill: white;"
                    + "-fx-font-weight:bold";
            Image img = new Image(getClass().getResourceAsStream("hddPic.png"));
            ImageView iv = new ImageView(img);
            vBox.getChildren().add(iv);

            Label d = new Label("Drive " + t[0]);
            d.setStyle(labelStyle);
            vBox.getChildren().add(d);

            Label ty = new Label("Type: [" + t[1] + "]");
            ty.setStyle(labelStyle);
            vBox.getChildren().add(ty);

            Label f = new Label(t[2] + " free of " + t[3]);
            f.setStyle(labelStyle);
            vBox.getChildren().add(f);

            double p = Double.parseDouble(t[t.length - 1]);
            vBox.getChildren().add(new ProgressBar((p - 0.0) / (100 - 0.0)));

            Label per = new Label(t[4] + "%");
            per.setStyle(labelStyle);
            vBox.getChildren().add(per);
            return vBox;
        }
    }
}
