/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient.Client;

import java.io.Serializable;

/**
 *
 * @author ABDULLAH
 */
public class SystemInfoObject implements Serializable {

    private String processor;
    private String memory;
    private String fileSystem;
    private String networkinterfaces;
    private String usbDevices;

    public String getProcessor() {
        return processor;
    }

    public String getMemory() {
        return memory;
    }

    public String getFileSystem() {
        return fileSystem;
    }

    public String getNetworkinterfaces() {
        return networkinterfaces;
    }

    public String getUsbDevices() {
        return usbDevices;
    }

    public SystemInfoObject() {
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public void setNetworkinterfaces(String networkinterfaces) {
        this.networkinterfaces = networkinterfaces;
    }

    public void setUsbDevices(String usbDevices) {
        this.usbDevices = usbDevices;
    }
}
