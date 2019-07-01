/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import rmiserver.RmiServer;
import static rmiserver.RmiServer.SERVER_DIR;

/**
 *
 * @author ABDULLAH
 */
public class ScriptLoader {

    public static void CopyScripts() throws IOException, InterruptedException {
        preScript(checkScript());
        runScheckerScript();
        InputStream is = RmiServer.class.getResourceAsStream("utility/mserver");
        try (FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.home") + "/bin/mserver"))) {
            String temp = "#!/bin/bash \n installDir=\"" + SERVER_DIR + "\"\n";
            byte[] contentInBytes = temp.getBytes();
            fos.write(contentInBytes);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.flush();
            fos.close();
        }
        postScript();
        runScheckerScript();
        copyCmdScriptJar();
    }

    private static boolean checkScript() {
        String p = System.getProperty("user.home") + "/bin";
        BufferedReader br = null;
        boolean b = false;
        try {
            String sCurrentLine;
            int i = 1;
            br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/.bashrc"));
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains(p)) {
                    b = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return b;
    }

    private static void preScript(boolean b) {
        String t;
        if (b) {
            t = "#!/bin/bash\n"
                    +"mkdir $HOME/bin\n";
        } else {
            t = "#!/bin/bash\n"
                    + "mkdir $HOME/bin\n"
                    + "pathenv=\"if [ -d \"$HOME/bin\" ] ; then\n"
                    + "PATH=\"$HOME/bin:$PATH\"\n"
                    + "fi\"\n"
                    + "echo \"$pathenv\">> ~/.bashrc \n"
                    + "echo \"$pathenv\">> ~/.profile \n"
                    + ". ~/.profile";
        }

        try {
            new File(SERVER_DIR + "initScript.sh").createNewFile();

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(SERVER_DIR + "initScript.sh"), "utf-8"))) {
                writer.write(t);
                Process p = Runtime.getRuntime().exec("chmod 755 " + SERVER_DIR + "initScript.sh");
                p.waitFor();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private static void postScript() {
        try {
            new File(SERVER_DIR + "initScript.sh").createNewFile();
            String t = "#!/bin/bash\n"
                    + "chmod +x ~/bin/mserver\n"
                    + "";
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(SERVER_DIR + "initScript.sh"), "utf-8"))) {
                writer.write(t);
                Process p = Runtime.getRuntime().exec("chmod 755 " + SERVER_DIR + "initScript.sh");
                p.waitFor();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void runScheckerScript() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec(SERVER_DIR + "initScript.sh");
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyCmdScriptJar() throws IOException {
        InputStream is = RmiServer.class.getResourceAsStream("utility/ScriptExecuter.jar");
        try (FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.home") + "/bin/ScriptExecuter.jar"))) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.flush();
        }
    }
}
