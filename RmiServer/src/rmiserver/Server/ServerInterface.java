/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import rmiclient.Client.CallBackInterface;

/**
 *
 * @author ABDULLAH
 */
public interface ServerInterface extends Remote {

    public void stopViewer(int index) throws RemoteException;

    public int startViewer(InetAddress inetAddress, CallBackInterface client, String Os) throws RemoteException;

    public void sendMessage(int index, String msg) throws RemoteException;

    public void sendBroadCast(String msg) throws RemoteException;

    public String getProcess(int index) throws RemoteException;

    public String getSs(int index) throws RemoteException, IOException;

    public String getClient() throws RemoteException;
}
