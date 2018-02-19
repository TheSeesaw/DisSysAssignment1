import java.io.*;
import java.net.*;

/**
 * Created by Kristoffer Schindele & Team on 2/18/2018.
 *
 */
public class P2PThread implements Runnable {
    // initialize variables
    private Socket clientSocket;
    final DataInputStream t_dis;
    final DataOutputStream t_dos;
    BufferedReader toConsole = null;
    BufferedReader toServer = null;
    PrintWriter toClient = null;
    String returned;

    // instantiate a thread instance and pass the EchoThread to it
    P2PThread(Socket socket, DataInputStream dis, DataOutputStream, dos) {
        this.clientSocket = socket;
    }

    public void run() {

        try {
            // thread attempts to create readers and writers for the client socket
            // reads data from client's console
            toConsole = new BufferedReader(new InputStreamReader(System.in));
            // sends data to the server
            toServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // writes data to client's console
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            boolean remainOpen = true;
            toClient.write("Connection opened, please enter some text");

            while(remainOpen) {
              // TODO all logic
              // End of text input loop
            }
        } catch (Exception e) {
            System.out.println("Failed to read or write input, or client connection was severed.");
        }
        finally {
            close();
        }
    }

    public void close() {
        try {
            toClient.write("Closing connection...");
            toConsole.close();
            toServer.close();
            toClient.close();
            clientSocket.close();
        } catch(IOException e) {
            System.out.println("Connection did not close properly.");
        }
    }
}
