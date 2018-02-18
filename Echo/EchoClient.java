import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try (
                // establish a socket with the server
                Socket echoSocket = new Socket(hostName, portNumber);
                // create a writer to print output
                PrintWriter socketOut = new PrintWriter(echoSocket.getOutputStream(), true);
                // create a reader to take information from the server
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                // create a reader to take information typed into the console
                BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in))
                ) {
            System.out.println("Connection success");
            String consoleInput;
            while ((consoleInput = consoleIn.readLine()) != null) {
                socketOut.println(consoleInput);
                System.out.println("echo: " + socketIn.readLine());
            }


        } catch (UnknownHostException e) {
            System.err.println("Can't connect to host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O error with host: " +
                    hostName);
            System.exit(1);
        }
    }
}