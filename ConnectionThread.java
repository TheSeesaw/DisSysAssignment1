import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


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


    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
              System.out.println(entry.getKey());
                return entry.getKey();
            }
        }
        return null;
    }


    public void run() {

        while(true){
            try{
                Socket peerSock = null;
                peerSock = user.servSock.accept();

                fromClient = new BufferedReader(new InputStreamReader(peerSock.getInputStream()));
                toClient = new PrintWriter(peerSock.getOutputStream(), true);

                String input = fromClient.readLine();
                String messageType = input.substring(0,1);
                System.out.println(messageType);
                String protocolCheck = input.substring(1,2);
                String message = input.substring(2);

                String[] address = new String[2];
                address[0] = peerSock.getInetAddress().toString();
                address[1] = Integer.toString(peerSock.getPort());

                switch(messageType)
                {
                  case "0": // connection request
                  //TODO This need to reflect the new datatype
                    // toClient.println(Arrays.toString(user.knownPeers));
                    user.knownPeers.put(message, address);
                  case "1":
                    String name = getKeyByValue(user.knownPeers, address);
                    System.out.println(name + ": " + message);
                  case "2": // disconnection request
                    user.knownPeers.remove(message);
                    System.out.println(message + " has left the conversation.");
                  default:
                    System.out.println("uh-oh");
                }
                peerSock.close();
              }
              catch (IOException e) {
              System.out.println("Accept failed on port 8080");
              System.exit(-1);
            }
        }
    }
}
