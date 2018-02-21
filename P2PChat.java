import java.util.Scanner;
import java.io.*;
import java.net.*;

/**
 * Created by Kristoffer Schindele & Team on 2/18/2018.
 *
 */
public class P2PChat
{
  private String[][] knownPeers;

  public P2PChat(String knownIP, String knownPort, String knownName)
  {
    knownPeers = new String[1][3];
    knownPeers[0][0] = knownIP;
    knownPeers[0][1] = knownPort;
    knownPeers[0][2] = knownName;
  }

  public static int messageDispatch(String messageType, String payload, String[][] addresses)
  {
    int index = 0;
    InetAddress workingAddress = null;
    int workingPort = 0;
    Socket workingSocket = null;
    PrintWriter outgoing = null;
    for (;index < addresses.length; index++)
    {
      try
      {
        workingAddress = InetAddress.getByName(addresses[index][0]);
        workingPort = Integer.parseInt(addresses[index][1]);
        workingSocket = new Socket(workingAddress,workingPort);
        outgoing = new PrintWriter(workingSocket.getOutputStream(), true);
        // TODO handle other cases
        outgoing.println(payload);
      }
      catch (Exception e)
      {
        System.out.println("Failed to send message");
      }
    }
    switch(messageType)
    {
      case "0": // connection request
        break;
      case "1": // message
        break;
      case "2": // disconnection request
        break;
      default:
        System.out.println("Invalid message, please try again.");
    }
    return 1;
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
    System.out.println("Enter a message:");
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
    //System.out.println(messageType);
    return true;
  }

  public static void main(String args[]) throws Exception
  {
    // create a server socket
    // NOTE: all of these could be set by command line arguments
    ServerSocket servSock = null;
    Socket userSock = null;
    String hostName = "localhost";
    int portNumber = 8080;
    String knownIP = "134.114.109.46";
    String knownPort = "8081";
    String knownName = "A Friend";
    // initialize known peers with one other address
    P2PChat user = new P2PChat(knownIP, knownPort, knownName);
    try
    {
      System.out.println("Starting server . . .");
      servSock = new ServerSocket(portNumber);
      // listen for connections
      // TODO: connection thread will be called here
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
  }
}
