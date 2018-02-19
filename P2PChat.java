import java.io.*;
import java.net.*;

/**
 * Created by Kristoffer Schindele & Team on 2/18/2018.
 *
 */
public class P2PChat
{
  public static void main(String args[]) throws Exception
  {
    // create a server socket
    ServerSocket servSock = null;
    Socket userSock = null;
    String hostName = "localhost";
    int portNumber = 8080;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    try {
        // defaults to port 8080
        servSock = new ServerSocket(portNumber);
        // listen for connections
        // TODO: connection thread will be called here
        /*
        NOTE: following code refactored into ConnectionThread
        while(true){
            P2PThread uThread;
            try{
              userSock = servSock.accept();
              dis = new DataInputStream(userSock.getInputStream);
              dos = new DataOutputStream(userSock.getInputStream);
              // pass the client socket to the thread
              uThread = new P2PThread(userSock, dis, dos);
              Thread newParticipant = new Thread(uThread, "P2PThread");
              newParticipant.start();
              System.out.println("Client connected.");
            } catch (IOException e) {
              System.out.println("Accept failed on port 8080");
              System.exit(-1);
            }
        }
        */

    } catch (IOException e) {
        System.out.println("Unable to listen on port 8080");
        System.exit(-1);
    }

    // listen for additional connections
  }
}
