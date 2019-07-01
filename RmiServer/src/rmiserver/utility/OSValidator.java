/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.utility;

/**
 *
 * @author ABDULLAH
 */
public class OSValidator {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static String getOS() {

        String temp = "N/A";
        if (isWindows()) {
            temp = "Windows";
        } else if (isMac()) {
            temp = "Mac";
        } else if (isUnix()) {
            temp = "Linux";
        } else if (isSolaris()) {
            temp = "Solaris";
        } else {
            temp = "N/A";
        }
        return temp;
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }
}
