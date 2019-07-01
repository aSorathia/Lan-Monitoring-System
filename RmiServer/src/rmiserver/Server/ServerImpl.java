/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.Server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.Alert;
import javax.imageio.ImageIO;
import rmiclient.Client.CallBackInterface;
import static rmiserver.RmiServer.SERVER_DIR;
import rmiserver.utility.DialogBoxes;

/**
 *
 * @author ABDULLAH
 */
public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

    public ServerImpl() throws RemoteException {
    }

    @Override
    public void stopViewer(int index) throws RemoteException {
        Server.cRemoveViewer(index);
    }

    @Override
    public int startViewer(InetAddress inetAddress, CallBackInterface client, String Os) throws RemoteException {
        return Server.addViewer(inetAddress, client, Os);
    }

    @Override
    public void sendMessage(int index, String msg) throws RemoteException {
        Server.sendMessage(msg, index);
    }

    @Override
    public void sendBroadCast(String msg) throws RemoteException {
        Server.sendBroadcastMessage(msg);
    }

    @Override
    public String getProcess(int index) throws RemoteException {
        return Server.viewers.get(index).getClient().getProcesses();
    }

    @Override
    public String getSs(int index) throws RemoteException, IOException {
        BufferedImage c = javax.imageio.ImageIO.read(new ByteArrayInputStream(Server.viewers.get(index).getClient().getScreenShot()));
        String f = Server.viewers.get(index).getInetAddress().toString().replace("/", "-") + "-" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        ImageIO.write(c, "jpg", new File(SERVER_DIR + "screenshot/" + f + "." + "jpg"));
        return f + "." + "jpg";
    }

    @Override
    public String getClient() throws RemoteException {
        return Server.getClients();
    }
}
