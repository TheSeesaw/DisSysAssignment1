import java.io.*;
import java.net.*;

public class P2PChat
{
  public static void main(String args[]) throws Exception
  {
    // create a server socket
    ServerSocket servSock;
    try {
        // defaults to port 8080
        servSock = new ServerSocket(8080);
        while(true){
            P2PThread uThread;
            try{
                // pass the client socket to the thread
                uThread = new P2PThread(servSock.accept());
                Thread newParticipant = new Thread(uThread, "Echo Thread");
                newParticipant.start();
                System.out.println("Client connected.");
            } catch (IOException e) {
                System.out.println("Accept failed on port 8080");
                System.exit(-1);
            }
        }
    } catch (IOException e) {
        System.out.println("Unable to listen on port 8080");
        System.exit(-1);
    }
    // pass the server socket off to the first client (user)
    
    // listen for additional connections
  }
}
