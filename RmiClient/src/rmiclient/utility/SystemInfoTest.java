package rmiclient.utility;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.UsbDevice;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import rmiclient.Client.SystemInfoObject;

public class SystemInfoTest {

    private static SystemInfo si;
    private static HardwareAbstractionLayer hal;
    private static OperatingSystem os;

    public static SystemInfoObject getSystemInfo() {
        // Options: ERROR > WARN > INFO > DEBUG > TRACE
        SystemInfoObject sio = new SystemInfoObject();
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
        Logger LOG = LoggerFactory.getLogger(SystemInfoTest.class);
        si = new SystemInfo();
        hal = si.getHardware();
        os = si.getOperatingSystem();
        sio.setProcessor(printProcessor());
        sio.setMemory(printMemory());
        sio.setFileSystem(printFileSystem());
        sio.setNetworkinterfaces(printNetworkInterfaces());
        sio.setUsbDevices(printUsbDevices());
        return sio;
    }

    public static String printProcessor() {
        CentralProcessor processor = hal.getProcessor();
        String t = (processor) + ":"
                + (" " + processor.getPhysicalProcessorCount() + " physical CPU(s)") + ":"
                + (" " + processor.getLogicalProcessorCount() + " logical CPU(s)") + ":"
                + ("Identifier: " + processor.getIdentifier());
        return t;
    }

    public static String printMemory() {
        GlobalMemory memory = hal.getMemory();
        String t = (FormatUtil.formatBytes(memory.getAvailable()) + "/"
                + FormatUtil.formatBytes(memory.getTotal()));
        return t;
    }

    public static String printFileSystem() {
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        String t = "";
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            String letter = fs.getName();
            //String answer = letter.substring(letter.indexOf("(")+1,letter.indexOf(")"));
            t = t + (letter + "," + fs.getType() + "," + FormatUtil.formatBytes(usable) + "," + FormatUtil.formatBytes(fs.getTotalSpace()) + "," + Math.ceil(100d * usable / total) + "!");
            //F:|NTFS|116.8 GiB|298.1 GiB|40.0!            
        }
        return t;
    }

    public static String printNetworkInterfaces() {
        NetworkIF[] networkIFs = hal.getNetworkIFs();
        String t = "";
        for (NetworkIF net : networkIFs) {
            t = t + (" Name: " + net.getName() + " (" + net.getDisplayName() + ")\n")
                    + ("   MAC Address: " + net.getMacaddr() + " \n")
                    + ("   IPv4: " + Arrays.toString(net.getIPv4addr()) + " \n")
                    + ("====================================================\n");
            //Name,mac,ipv4
        }
        return t;
    }

    public static String printUsbDevices() {
        UsbDevice[] usbDevices = hal.getUsbDevices(true);
        String t = "";
        for (UsbDevice usbDevice : usbDevices) {
            t = t + usbDevice.toString() + "\n";
        }
        return t;
    }
}
