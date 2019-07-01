/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.Server;

import java.io.IOException;
import rmiserver.Server.Settings.Config;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Set;
import javafx.scene.control.Alert;

import rmiserver.utility.DialogBoxes;
import rmiserver.MainFXMLDocumentController;
import rmiclient.Client.CallBackInterface;
import rmiserver.About.AboutMain;
import rmiserver.ProcessViwer.ProcessViewerMain;
import rmiserver.ScreenSHotViewer.ScreenShotViewerMain;
import rmiserver.SystemViewer.SystemInfoViewerMain;

/**
 *
 * @author ABDULLAH
 */
public class Server {

    private static volatile CallBackInterface client;

    private static MainFXMLDocumentController fxmlController;
    private static boolean idle = true;
    private static boolean running = false;

    private static Registry registry;
    private static ServerImpl serverImpl;

    public static Hashtable<Integer, createObject> viewers = new Hashtable<>();

    public static void initializeFXML(MainFXMLDocumentController fxmlController) {
        Server.fxmlController = fxmlController;
    }

    public static void setClient(CallBackInterface client) {
        Server.client = client;
    }

    public static void Start() {
        idle = false;
        running = false;
        Config.loadConfiguration();

        System.setProperty("java.rmi.server.hostname", Config.server_address);

        try {
            serverImpl = new ServerImpl();
            registry = LocateRegistry.createRegistry(Config.server_port);
            registry.rebind("ServerImpl", serverImpl);
        } catch (Exception e) {
            e.printStackTrace();
            new DialogBoxes().exceptionBox(
                    new DialogBoxes().createAlert(
                            "Error",
                            "An Exception occured!!",
                            e.getMessage(),
                            Alert.AlertType.ERROR),
                    e
            );
            Stop();
            return;
        }

        System.out.println(getStatus());
        running = true;
    }

    public static void Stop() {
        Set<Integer> keys = viewers.keySet();
        if (running) {
            keys.stream().forEach((key) -> {
                try {
                    viewers.get(key).getClient().serverStoped();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            });
            running = false;
        }
        try {
            if (registry != null) {
                UnicastRemoteObject.unexportObject(registry, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new DialogBoxes().exceptionBox(
                    new DialogBoxes().createAlert(
                            "Error",
                            "An Exception occured!!",
                            e.getMessage(),
                            Alert.AlertType.ERROR),
                    e
            );
        }
        registry = null;
        serverImpl = null;
    }

    public static boolean isRunning() {
        return running;
    }

    public static boolean isIdle() {
        return idle;
    }

    public static String getStatus() {
        String status = "Running ...at:\n\t"
                + Config.server_address + ":" + Config.server_port;
        return status;
    }

    public static synchronized int addViewer(InetAddress inetAddress, CallBackInterface client, String os) {
        int index = viewers.size();
        viewers.put(index, new createObject(inetAddress, client, os));
        try {
            System.out.println("Added: " + index + " : " + inetAddress.toString());
            if (fxmlController != null) {
                fxmlController.addViewer(index, inetAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return index;
    }

    public static void cRemoveViewer(int index) {
        viewers.remove(index);
        fxmlController.clientRemoveViewer(index);
    }

    public static void removeAllClient() {
        viewers.clear();
        fxmlController.removeAllClient();
    }

    public static int getViewersCount() {
        return viewers.size();
    }

    public static boolean isClientAttached() {
        return client != null;
    }

    public static void printH(int index) throws RemoteException {
        viewers.get(index).getClient().printHello();
    }

    public static String getClients() {
        Set<Integer> keys = viewers.keySet();
        String s = "N/A";
        if (keys != null) {
            s = "";
            for (int key : keys) {
                s = s + "(" + key + ")" + viewers.get(key).getInetAddress().toString() + "\n";
            }
        }
        return s;
    }

    public static void sendMessage(String message, int index) throws RemoteException {
        try {
            viewers.get(index).getClient().serverMessage(message);
        } catch (Exception e) {
            new DialogBoxes().createAlert(
                    "Error!!",
                    "There was an error.",
                    "Please select proper remote pc from active"
                    + " connection and try again ?",
                    Alert.AlertType.ERROR).showAndWait();
        }
    }

    public static void sendBroadcastMessage(String sMessage) {
        Set<Integer> keys = viewers.keySet();
        new Thread(new Runnable() {
            @Override
            public void run() {
                keys.stream().forEach((key) -> {
                    try {
                        viewers.get(key).getClient().serverMessage(sMessage);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        }).start();
    }

    public static void getProcess(int index) throws RemoteException, IOException {
        String os = viewers.get(index).getOs();
        if (!os.contains("N/A")) {
            new ProcessViewerMain().start(viewers.get(index).getClient(), viewers.get(index).getInetAddress().toString());
        } else {
            new DialogBoxes().createAlert("Error",
                    "Client Os is not supported",
                    "",
                    Alert.AlertType.ERROR).showAndWait();
        }
    }

    public static void getScreenShot(int index) throws RemoteException, IOException {
        new ScreenShotViewerMain().start(viewers.get(index).getClient(), viewers.get(index).getInetAddress().toString());

    }

    public static void showAbout() throws IOException {
        new AboutMain().start();
    }

    public static void getSystemInfo(int index) throws IOException {
        try{
            new SystemInfoViewerMain().start(viewers.get(index).getClient(), viewers.get(index).getInetAddress().toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
