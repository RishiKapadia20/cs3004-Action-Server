import java.net.*;
import java.io.*;
import java.util.regex.*;

public class SharedActionState{
	
	private SharedActionState mySharedObj;
	private String myThreadName;
	private double [] mySharedVariable;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers

// Constructor	
	
	SharedActionState(double [] SharedVariable) {
		mySharedVariable  = SharedVariable;
	}

//Attempt to aquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    /* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput,String account) {
    		System.out.println(myThreadName + " received " + theInput);
    		String theOutput = null;
    		theInput = theInput.toLowerCase();
			// Check what the client said
			//Correct request
			if (myThreadName.equals("ActionServerThread1") || myThreadName.equals("ActionServerThread2") || myThreadName.equals("ActionServerThread3") || myThreadName.equals("ActionServerThread4")) {

				if (theInput.matches("add_money\\([a-z]+\\d,[\\d]+\\)")) {
					
						theInput = theInput.replace("add_money(","");
						theInput = theInput.replace(")", "");
						String [] input = theInput.split(",");				
						String accountName = input[0];
						int accountNumber = Integer.parseInt(accountName.replace("acc", ""));
						double money = Double.parseDouble((input[1]));
						double balance = mySharedVariable[accountNumber-1];

						if (account.equals(accountName)){					
							
							mySharedVariable[accountNumber - 1] =  balance + money;
							
							System.out.println(myThreadName + " made " + accountName + " balance"  + mySharedVariable[accountNumber - 1]);
							theOutput = "Add_money completed.  " + accountName + " has gone from " + balance + " to " + mySharedVariable[accountNumber - 1];

						}else{

							System.out.println(account);
							System.out.println(accountName);

							System.out.println("Wrong account");
							theOutput = "Wrong account";

						}
						
					
				}
				else if (theInput.matches("subtract_money\\([a-z]+\\d,[\\d]+\\)")) {   			
					
					theInput = theInput.replace("subtract_money(","");
					theInput = theInput.replace(")", "");
					String [] input = theInput.split(",");				
					String accountName = input[0];
					int accountNumber = Integer.parseInt(accountName.replace("acc", ""));
					double money = Double.parseDouble((input[1]));
					double balance = mySharedVariable[accountNumber-1];

					if (account.equals(accountName)){						
						
						mySharedVariable[accountNumber - 1] =  balance - money;
						
						System.out.println(myThreadName + " made " + accountName + " balance"  + mySharedVariable[accountNumber - 1]);
						theOutput = "Subtract_money completed. " + accountName + "  has gone from " + balance + " to " + mySharedVariable[accountNumber - 1];

					}else{

						System.out.println("Wrong account");
						theOutput = "Wrong account";

					}    			    			
				}
				
				else if (theInput.matches("transfer_money\\([a-z]+\\d,[a-z]+\\d,[\\d]+\\)")) {
					
					theInput = theInput.replace("transfer_money(","");
					theInput = theInput.replace(")", "");
					String [] input = theInput.split(",");
					String accountName = input[0];
					String targetAccountName = input[1];
					double money = Double.parseDouble((input[2]));
					int accountNumber = Integer.parseInt(accountName.replace("acc", ""));
					int targetAccountNumber = Integer.parseInt(targetAccountName.replace("acc", ""));
					double myBalance = mySharedVariable[accountNumber-1];
					double targetBalance = mySharedVariable[targetAccountNumber-1];

					if (account.equals(accountName)){					

						mySharedVariable[accountNumber - 1] =  myBalance - money;
						
						mySharedVariable[targetAccountNumber - 1] =  targetBalance + money;					

						System.out.println(myThreadName + " made " + accountName + " balance " + mySharedVariable[accountNumber - 1] + " & " + targetAccountName + " balance " + mySharedVariable[targetAccountNumber - 1] );
						theOutput = "Transfer_money completed."  + accountName + " has gone from " + myBalance + " to " + mySharedVariable[accountNumber - 1] + " & " +  targetAccountName + " has gone from " + targetBalance + " to " + mySharedVariable[targetAccountNumber - 1] ;

					}else{

						System.out.println("Wrong account");
						theOutput = "Wrong account";

					}			

				} else { //incorrect request
					theOutput = myThreadName + " received incorrect request";
			
				}			
				
			} else {
				System.out.println("Error - Unrecognised client");
				theOutput = "Error - Unrecognised client";
			}
			
			//Return the output message to the ActionServer

			System.out.println(theOutput);
				return theOutput;
		}
	
}

