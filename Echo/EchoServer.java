import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Summer on 1/28/2018.
 * Edited by Kristoffer on 1/29/2018.
 */
public class EchoServer{

    public static void main(String args[]) throws Exception{
        ServerSocket servSock;
        try {
            // defaults to port 8080
            servSock = new ServerSocket(8080);

            while(true){
                EchoThread eThread;

                try{
                    // pass the client socket to the thread
                    eThread = new EchoThread(servSock.accept());
                    Thread thread = new Thread(eThread, "Echo Thread");
                    thread.start();
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

    }
}
