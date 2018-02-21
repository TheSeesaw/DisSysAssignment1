import java.util.Scanner;
import java.io.*;
import java.net.*;

/**
 * Created by Kristoffer Schindele & Team on 2/18/2018.
 *
 */
public class P2PChat
{
  public String[][] knownPeers;
  public ServerSocket servSock;

  public P2PChat(String knownIP, String knownPort, String knownName)
  {
    servSock = null;
    knownPeers = new String[1][3];
    knownPeers[0][0] = knownIP;
    knownPeers[0][1] = knownPort;
    knownPeers[0][2] = knownName;
  }

  public static void updateAddresses(String newAddresses)
  {
    return;
  }

  public static void messageDispatch(String messageType, String payload, String[][] addresses)
  {
    int index = 0;
    InetAddress workingAddress = null;
    int workingPort = 0;
    Socket workingSocket = null;
    PrintWriter outgoing = null;
    BufferedReader incoming = null;
    String response = null;
    boolean terminate = false;
    for (;index < addresses.length; index++)
    {
      try
      {
        workingAddress = InetAddress.getByName(addresses[index][0]);
        workingPort = Integer.parseInt(addresses[index][1]);
        workingSocket = new Socket(workingAddress,workingPort);
        outgoing = new PrintWriter(workingSocket.getOutputStream(), true);
        incoming = new BufferedReader(new InputStreamReader(workingSocket.getInputStream()));
        switch(messageType)
        {
          case "0": // connection request
            outgoing.println(messageType + "#" + payload);
            System.out.println("Connected to " + addresses[index][2]);
            response = incoming.readLine(); // receive the client's address list
            // parse and update the address list
            // TODO, NOTE: should print out addresses added
            break;
          case "1": // message
            outgoing.println(addresses[index][2] + ": " + payload);
            System.out.println("Me: " + payload);
            break;
          case "2": // disconnection request
            outgoing.println(messageType + "#" + payload);
            System.out.println("Disconnected.");
            terminate = true;
            break;
          default:
            System.out.println("Invalid message, please try again.");
        }
        // close the working socket for the next address
        workingSocket.close();
      }
      catch (UnknownHostException e)
      {
        System.out.println("Failed to send message.");
      }
      catch (IOException e)
      {
        System.out.println("Failed to create writer/reader on socket");
      }
      catch (Exception e){
        System.out.println("Failed elsewhere.");
      }
    }
    if (terminate)
    {
      System.exit(0);
    }
    return;
  }

  public static String[] getAddressByName(String[][] addresses, String name)
  {
    int index = 0;
    for (; index < addresses.length; index++)
    {
      if (name.equals(addresses[index][2]));
      return addresses[index];
    }
    return null;
  }

  public static boolean handleUserInput(Scanner userIn, String inputStorage, P2PChat user)
  {
    inputStorage = userIn.nextLine();
    if (inputStorage.equals("quit"))
    {
      return false; // exit case
    }
    String messageType = inputStorage.substring(0,1);
    String protocolCheck = inputStorage.substring(1,2);
    if (protocolCheck.equals("#") &&
       (messageType.equals("0") || messageType.equals("1") || messageType.equals("2")))
    {
      String payload = inputStorage.substring(2);
      messageDispatch(messageType, payload, user.knownPeers);
    }
    else
    {
      System.out.println("Invalid message, please try again.");
    }
    return true;
  }

  public static void main(String args[]) throws Exception
  {

    // NOTE: all of these could be set by command line arguments
    String hostName = "localhost";
    int portNumber = Integer.parseInt(args[0]);
    String knownIP = "localhost";
    String knownPort = args[1];
    String knownName = args[2];
    // initialize known peers with one other address
    P2PChat user = new P2PChat(knownIP, knownPort, knownName);
    try
    {
      System.out.println("Starting server . . .");
      // create a server socket
      user.servSock = new ServerSocket(portNumber);
      // listen for connections
      ConnectionThread connectThread = new ConnectionThread(user);
      Thread thread = new Thread(connectThread, "Connection Thread");
      thread.start();
      // server is now running and listening for connections
      System.out.println("Server started.");
    }
    catch (IOException e)
    {
        System.out.println("Unable to start server listening on port: " + Integer.toString(portNumber));
        System.exit(-1);
    }
    // TODO create user socket, depending
    // listen for input from the user
    Scanner userIn = new Scanner(System.in);
    String inputStorage = null;
    //String outputData = null;
    boolean active = true;
    System.out.println("Now listening for input. Type quit to exit");
    while(active)
    {
      active = handleUserInput(userIn, inputStorage, user);
    }
    System.out.println("Now Exiting . . .");
    return;
  }
}
