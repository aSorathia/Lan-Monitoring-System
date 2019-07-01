/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ABDULLAH
 */
public class ConnectionModel {

    private final SimpleIntegerProperty index;
    private final SimpleStringProperty address;
    private final SimpleStringProperty port;

    public ConnectionModel(int index, String address, String port) {
        this.index = new SimpleIntegerProperty(index);
        this.address = new SimpleStringProperty(address);
        this.port = new SimpleStringProperty(port);
    }

    public int getIndex() {
        return index.get();
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPort() {
        return port.get();
    }

    public void setPort(String port) {
        this.port.set(port);
    }
}
