/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author ABDULLAH
 */
public class Processes {

    public static String getProcesses() throws IOException {

        String os = OSValidator.getOS();
        String processes = "N/A";
        if (os.equalsIgnoreCase("Windows")) {
            String line;
            StringBuilder sb = new StringBuilder();
            String[] pArray = null;
            Process p = Runtime.getRuntime().exec("tasklist /V /FO \"CSV\" /NH");
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while ((line = input.readLine()) != null) {
                    //<-- Parse data here.
                    String[] t = line.split(",");
                    String temp = t[t.length - 1];
                    if (temp.contains("N/A")) {
                        continue;
                    }
                    sb.append(temp);
                    sb.append(",");
                }
            }
            processes = sb.deleteCharAt(sb.length() - 1).toString();

        } else if (os.equalsIgnoreCase("Linux")) {
            try {
                String temp = "";
                String line;
                Process p = Runtime.getRuntime().exec("python " + rmiclient.RmiClient.CLIENT_DIR + "/linuxProcess.py");
                try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                    while ((line = input.readLine()) != null) {
                        temp = temp + line;
                        System.out.println(line);
                    }
                }
                System.out.println(temp);
                processes = temp;
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        return processes;
    }
}
