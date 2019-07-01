package rmiclient1;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/


/**
 *
 * @author ABDULLAH
 */
public class RmiClient1 {

    public static void main(String[] args) throws IOException {
        
        System.out.println(new File(RmiClient1.class.getResource("utility2/linuxProcess.py").getFile()).getAbsolutePath());
           /*     Path source = Paths.get(new File(RmiClient1.class.getResource("utility2/linuxProcess.py").getFile()).getAbsolutePath());
		Path destination = Paths.get(getCurrentDirectory () + "/linuxProcess.py");
            try {
			Files.copy(source, destination, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
            File file = new File(Processes.class.getResource("linuxProcess.py").getFile());
            Files.copy(file, getCurrentDirectory (), REPLACE_EXISTING);      
                System.out.println(file.getAbsolutePath());
                System.out.println(file.getCanonicalPath());*/
    }
    public static String getCurrentDirectory () {
        String currentDirectory = null;
        try {
            currentDirectory = new File(".").getCanonicalPath() + File.separatorChar;            
        } catch (IOException e) {
            e.getStackTrace();
        }
        return currentDirectory;
    } 
    
}
