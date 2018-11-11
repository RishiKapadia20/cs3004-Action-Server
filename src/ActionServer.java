import java.net.*;
import java.io.*;





public class ActionServer {
  public static void main(String[] args) throws IOException {

	ServerSocket ActionServerSocket = null;
    boolean listening = true;
    String ActionServerName = "ActionServer";
    int ActionServerNumber = 4545;
    
    double [] SharedVariable = {1000,1000,1000,1000};

    //Create the shared object in the global scope...
    
    SharedActionState ourSharedActionStateObject = new SharedActionState(SharedVariable);
    //SharedActionState ourSharedActionStateObject2 = new SharedActionState(SharedVariable);
    
        
    // Make the server socket

    try {
      ActionServerSocket = new ServerSocket(ActionServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + ActionServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(ActionServerName + " started");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerThread1", ourSharedActionStateObject,"acc1").start();
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerThread2", ourSharedActionStateObject,"acc2").start();
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerThread3", ourSharedActionStateObject,"acc3").start();
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerThread4", ourSharedActionStateObject,"acc4").start();
      System.out.println("New " + ActionServerName + " thread started.");
    }
    ActionServerSocket.close();
  }
}