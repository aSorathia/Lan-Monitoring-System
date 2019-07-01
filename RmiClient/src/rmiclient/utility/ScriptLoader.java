/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import rmiclient.RmiClient;
import static rmiclient.RmiClient.CLIENT_DIR;

/**
 *
 * @author ABDULLAH
 */
public class ScriptLoader {

    public static void installProcessScript() throws FileNotFoundException, IOException {
        InputStream is = RmiClient.class.getResourceAsStream("utility/linuxProcess.py");
        try (FileOutputStream fos = new FileOutputStream(new File(CLIENT_DIR + "/linuxProcess.py"))) {
            System.out.println(CLIENT_DIR + "/linuxProcess.py");
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.flush();
        }
    }

    private static void copyCheckerScript() {
        try {
            new File(RmiClient.CHECKER_SCRIPT_FILE).createNewFile();
            String t = "#!/bin/bash\n"
                    + "# Package Checking Script\n"
                    + "\n"
                    + "PKG_OK=$(dpkg-query -W -f='${Status}' wmctrl 2>/dev/null | grep -c \"ok installed\") \n"
                    + "echo $PKG_OK\n"
                    + "";

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(RmiClient.CHECKER_SCRIPT_FILE), "utf-8"))) {
                writer.write(t);
                Process p = Runtime.getRuntime().exec("chmod 755 " + RmiClient.CHECKER_SCRIPT_FILE);
                p.waitFor();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static boolean packageChecker() {
        boolean b = false;
        try {
            File file = new File(RmiClient.CHECKER_SCRIPT_FILE);
            if (file.isFile()) {
                b = runScheckerScript();
            } else {
                copyCheckerScript();
                b = runScheckerScript();
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
        return b;
    }

    public static boolean runScheckerScript() {
        boolean b = false;
        try {
            String line;
            Process p = Runtime.getRuntime().exec(RmiClient.CHECKER_SCRIPT_FILE);
            p.waitFor();
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while ((line = input.readLine()) != null) {
                    if (line.equalsIgnoreCase("0")) {
                        b = false;
                    }
                    if (line.equalsIgnoreCase("1")) {
                        b = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
