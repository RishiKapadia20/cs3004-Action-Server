import java.io.*;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;

public class ActionClient2 {
    public static void main(String[] args) throws IOException {

        // Set up the socket, in and out variables

        Socket ActionClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int ActionSocketNumber = 4545;
        String ActionServerName = "localhost";
        String ActionClientID = "ActionClient2";

        try {
            ActionClientSocket = new Socket(ActionServerName, ActionSocketNumber);
            out = new PrintWriter(ActionClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ActionClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ ActionSocketNumber);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        System.out.println("Initialised " + ActionClientID + " client and IO connections");
        
        // This is modified as it's the client that speaks first

        while (true) {            

            String [] possible_functions = {"add_money(","subtract_money(","transfer_money("};
        	
            int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);            

            int randomMoney = ThreadLocalRandom.current().nextInt(1, 1000+1);

            String strRandomMoney = Integer.toString(randomMoney);

            
            if (randomNum == 2) {                

                int randomAccount = ThreadLocalRandom.current().nextInt(1, 4 + 1);

                String strRandomAccount = Integer.toString(randomAccount);

                fromUser = possible_functions[randomNum] + "acc2," + "acc" + strRandomAccount + "," + strRandomMoney + ")" ;

                
            } else{

                fromUser = possible_functions[randomNum] + "acc2," + strRandomMoney + ")" ;

            }                                   
            
            System.out.println(ActionClientID + " sending " + fromUser + " to ActionServer");
            out.println(fromUser);
            
            fromServer = in.readLine();
            System.out.println(ActionClientID + " received " + fromServer + " from ActionServer");

            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                System.out.println("got interrupted!");
            }

                
        }

        // while (true) {
            
        //     fromUser = stdIn.readLine();
        //     if (fromUser != null) {
        //         System.out.println(ActionClientID + " sending " + fromUser + " to ActionServer");
        //         out.println(fromUser);
        //     }
        //     fromServer = in.readLine();
        //     System.out.println(ActionClientID + " received " + fromServer + " from ActionServer");
        // }
            
        
    //    Tidy up - not really needed due to true condition in while loop
    //    out.close();
    //    in.close();
    //    stdIn.close();
    //    ActionClientSocket.close();
    }
}
