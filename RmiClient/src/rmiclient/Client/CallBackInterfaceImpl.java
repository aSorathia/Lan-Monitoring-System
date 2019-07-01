/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient.Client;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmiclient.utility.Processes;
import rmiclient.utility.SystemInfoTest;

/**
 *
 * @author ABDULLAH
 */
public class CallBackInterfaceImpl extends UnicastRemoteObject implements CallBackInterface {

    public CallBackInterfaceImpl() throws RemoteException {
    }

    @Override
    public void printHello() throws RemoteException {
        try {
            System.out.println("Hi there am called on client");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getProcesses() throws RemoteException {
        try {
            return Processes.getProcesses();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void serverMessage(String sMessage) throws RemoteException {
        Client.serverMessage(sMessage);
    }

    @Override
    public byte[] getScreenShot() throws RemoteException {
        ByteArrayOutputStream baos = null;
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(capture, "jpg", baos);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    public void serverStoped() throws RemoteException {
        Client.resetClientStatus();
    }

    @Override
    public SystemInfoObject getSystemInfo() throws RemoteException {
        System.out.println("workin@@@g");
        return SystemInfoTest.getSystemInfo();
    }

    @Override
    public String getMemory() throws RemoteException {
        return SystemInfoTest.printMemory();
    }

    @Override
    public String getNi() throws RemoteException {
        return SystemInfoTest.printNetworkInterfaces();
    }

    @Override
    public String getUsb() throws RemoteException {
        return SystemInfoTest.printUsbDevices();
    }

    @Override
    public String getFs() throws RemoteException {
        return SystemInfoTest.printFileSystem();
    }
}
