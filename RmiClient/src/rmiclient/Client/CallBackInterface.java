/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author ABDULLAH
 */
public interface CallBackInterface extends Remote {

    public void printHello() throws RemoteException;

    public void serverMessage(String sMessage) throws RemoteException;

    public String getProcesses() throws RemoteException;

    public byte[] getScreenShot() throws RemoteException;

    public void serverStoped() throws RemoteException;

    public SystemInfoObject getSystemInfo() throws RemoteException;

    public String getMemory() throws RemoteException;

    public String getNi() throws RemoteException;

    public String getUsb() throws RemoteException;

    public String getFs() throws RemoteException;
}
