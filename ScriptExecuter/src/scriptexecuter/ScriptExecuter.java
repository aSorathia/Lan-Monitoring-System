/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptexecuter;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rmiserver.Server.ServerInterface;

/**
 *
 * @author ABDULLAH
 */
public class ScriptExecuter {

    private static Registry registry; 
    private static ServerInterface rmiServer;

    private static String serverAddress;
    private static int serverPort;
    private static String arg;

    public static void main(String[] args) {
        /*
        args[0]-server add
        args[1]-serverport
        args[2]-arg
        args[3]-subarg
        args[4]-option
                            0       1    2  *    3           4
        java -jar Script 127.0.0.1 6665 arg *clientIndex  option 
        
        */
        
        
        serverAddress = args[0];
        serverPort = Integer.parseInt(args[1]);
        arg = args[2];
        try {
                registry = LocateRegistry.getRegistry(serverAddress, serverPort);        
                rmiServer = (ServerInterface) registry.lookup("ServerImpl");
                if(arg.equalsIgnoreCase("s")){                    
                    getScreenshot(Integer.parseInt(args[3]));
                }
                if(arg.equalsIgnoreCase("m")){                    
                    send_Message(Integer.parseInt(args[3]),args[4]);
                }
                if(arg.equalsIgnoreCase("b")){                       
                    sendBroadcast(args[3]);                    
                }
                if(arg.equalsIgnoreCase("p")){
                    getProcess(Integer.parseInt(args[3]));
                }
                if(arg.equalsIgnoreCase("v")){
                    getClient();
                }
                if(arg==null){
                    System.out.println("something wrong");
                } 
                    
                
            } catch (Exception e) {  
                e.printStackTrace();
                System.out.println("An Error Encountered.\n Please check whether the server is on and proper client is selected"); 
                rmiServer = null;
                registry = null;                
            }
    }
    public static void getClient() throws RemoteException{		        
        System.out.println(rmiServer.getClient());
    }
    public static void send_Message(int index,String msg) throws RemoteException{	
        rmiServer.sendMessage(index,msg);
    }

    public static void getScreenshot(int index) throws RemoteException{    
        System.out.println(rmiServer.getSs(index));
    }

    public static void sendBroadcast(String msg) throws RemoteException{
        rmiServer.sendBroadCast(msg);
    }

    public static void getProcess(int index) throws RemoteException{
        System.out.println(rmiServer.getProcess(index).replace(',', '\n'));
    }    
}
