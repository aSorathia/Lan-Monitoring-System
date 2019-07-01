/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.Server;

import java.net.InetAddress;
import rmiclient.Client.CallBackInterface;

/**
 *
 * @author ABDULLAH
 */
public class createObject {

    private InetAddress inetAddress;
    private CallBackInterface client;
    private String os;

    public createObject(InetAddress inetAddress, CallBackInterface client, String Os) {
        this.inetAddress = inetAddress;
        this.client = client;
        this.os = Os;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setClient(CallBackInterface client) {
        this.client = client;
    }

    public CallBackInterface getClient() {
        return client;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOs() {
        return os;
    }
}
