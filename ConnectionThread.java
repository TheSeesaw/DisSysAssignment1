import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Summer on 1/28/2018.
 * Edited by Kristoffer on 1/29/2018.
 */
public class ConnectionThread implements Runnable {
    // initialize variables
    BufferedReader fromClient = null;
    PrintWriter toClient = null;
    String returned;
    P2PChat user;

    // instantiate a thread instance and pass the EchoThread to it
    public ConnectionThread(P2PChat user) {
      this.user = user;
    }

    public void run() {

        while(true){
            try{
                // pass the client socket to the thread
                Socket peerSock = null;
                peerSock = user.servSock.accept();
                fromClient = new BufferedReader(new InputStreamReader(peerSock.getInputStream()));
                String message = fromClient.readLine();
                String messageType = message.substring(0,1);
                String protocolCheck = message.substring(1,2);
                System.out.println(message);
                // user.knownPeers[][]
            } catch (IOException e) {
                System.out.println("Accept failed on port 8080");
                System.exit(-1);
            }
        }

    }
}
