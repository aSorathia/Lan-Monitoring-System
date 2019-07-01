/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.Server.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import rmiserver.RmiServer;

/**
 *
 * @author ABDULLAH
 */
public class Config {

    public static String server_address = "127.0.0.1";
    public static int server_port = 6666;
    public static boolean default_address = false;

    public static void loadConfiguration() {
        if (new File(RmiServer.SERVER_CONFIG_FILE).canRead()) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(RmiServer.SERVER_CONFIG_FILE));

                server_address = properties.get("server-address").toString();
                server_port = Integer.valueOf(properties.get("server-port").toString());

                default_address = Boolean.valueOf(properties.getProperty("default-address"));
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            storeConfiguration();
        }
    }

    public static void storeConfiguration() {
        try {
            new File(RmiServer.SERVER_CONFIG_FILE).createNewFile();
            Properties properties = new Properties();
            properties.put("server-address", server_address);
            properties.put("server-port", String.valueOf(server_port));
            properties.put("default-address", String.valueOf(default_address));

            properties.store(new FileOutputStream(RmiServer.SERVER_CONFIG_FILE),
                    "jrdesktop server configuration file");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
