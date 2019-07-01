/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import rmiclient.utility.OSValidator;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rmiclient.Client.Settings.Config;
import rmiclient.MainFXMLDocumentController;
import rmiclient.utility.InetAdrUtility;
import rmiserver.Server.ServerInterface;

/**
 *
 * @author ABDULLAH
 */
public class Client {

    public static boolean connected = false;
    private static MainFXMLDocumentController fxmlController;

    private static Registry registry;
    private static ServerInterface rmiServer;

    private static int index = -1;

    public static void initializeFXML(MainFXMLDocumentController fxmlController) {
        System.out.println("called initializer");
        Client.fxmlController = fxmlController;
    }

    public static boolean Start() {
        connect();
        if (!connected) {
            System.out.println("not connected");
            cStop();
        }
        return isConnected();
    }

    public static void connect() {
        connected = false;
        Config.loadConfiguration();
        try {
            registry = LocateRegistry.getRegistry(Config.server_address, Config.server_port);
            rmiServer = (ServerInterface) registry.lookup("ServerImpl");
            index = rmiServer.startViewer(InetAdrUtility.getLocalAdr(), new CallBackInterfaceImpl(), OSValidator.getOS());
            fxmlController.changeIndex(index);
            System.out.println(index);
            displayStatus();
            connected = true;
        } catch (RemoteException | NotBoundException e) {
            System.out.println("Error!! " + e.getMessage());
            e.printStackTrace();
            rmiServer = null;
            registry = null;
        }
    }

    public static void displayStatus() {
        System.out.println("Viewer connected to " + rmiServer);
    }

    public static void cStop() {
        cdisconnect();
    }

    public static void cdisconnect() {
        connected = false;
        try {
            if (rmiServer != null) {
                rmiServer.stopViewer(index);
                UnicastRemoteObject.unexportObject(rmiServer, true);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        rmiServer = null;
        registry = null;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void serverMessage(String serverMessage) {
        fxmlController.displayServerMessage(serverMessage);
    }

    public static void resetClientStatus() {
        index = -1;
        fxmlController.resetStatus();
    }
}
