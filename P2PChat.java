import java.util.*;
import java.io.*;
import java.net.*;

/**
 * Created by Kristoffer Schindele & Team on 2/18/2018.
 *
 */
public class P2PChat
{
  // public String[][] knownPeers;
  public Map<String, String[]> knownPeers;
  public ServerSocket servSock;

  public P2PChat(String knownIP, String knownPort, String knownName)
  {
    servSock = null;
    // knownPeers = new String[1][3];
    // knownPeers[0][0] = knownIP;
    // knownPeers[0][1] = knownPort;
    // knownPeers[0][2] = knownName;
    knownPeers = new Hashmap<String, String[]>();
    String[] address = new String[2];
    address[0] = knownIP;
    address[1] = knownPort;
    knownPeers.put(knownName, portAndName);
  }

  // expected format: e1#e2#e3#e1#e2#e3#e1#e2#e3
  // where e's are strings
  // should output an array object containing the same data, ready to be appended
  // to clien's knownPeers array
  public static String[][] parseAddressString(String addressString)
  {
    String[] addressItems = addressString.split("#");
    int fullLength = addressItems.length;
    String[][] resultAddresses = new String[fullLength / 3][3];
    int nestIndex = 0;
    int outerIndex = 0;
    for (;nestIndex < fullLength; nestIndex+=3)
    {
      resultAddresses[outerIndex][nextIndex] = addressItems[nextIndex];
      resultAddresses[outerIndex][nextIndex+1] = addressItems[nextIndex+1];
      resultAddresses[outerIndex][nextIndex+2] = addressItems[nextIndex+2];
      outerIndex++;
    }
    System.out.println(resultAddresses); //test
    return resultAddresses;
  }

  public static void updateAddresses(String newAddresses)
  {
    return;
  }

  public static void messageDispatch(String messageType, String payload, String[][] addresses)
  {
    //int index = 0;
    InetAddress workingAddress = null;
    int workingPort = 0;
    Socket workingSocket = null;
    PrintWriter outgoing = null;
    BufferedReader incoming = null;
    String response = null;
    boolean terminate = false;

    //for (;index < addresses.length; index++)
    for (String key : addresses.keySet())
    {
      try
      {
        // workingAddress = InetAddress.getByName(addresses[index][0]);
        // workingPort = Integer.parseInt(addresses[index][1]);
        // workingSocket = new Socket(workingAddress,workingPort);
        // outgoing = new PrintWriter(workingSocket.getOutputStream(), true);
        // // TODO handle other cases
        // outgoing.println(addresses[index][2] + ": " + payload);
        // System.out.println("Me: " + payload);
        workingAddress = Integer.parseInt(addresses.get(key)[0]);
        workingPort = Integer.parseInt(addresses.get(key)[1]);
        workingSocket = new Socket(workingAddress,workingPort);
        outgoing = new PrintWriter(workingSocket.getOutputStream(), true);
        incoming = new BufferedReader(new InputStreamReader(workingSocket.getInputStream()));
        outgoing.println(messageType + "#" + payload);
        switch(messageType)
        {
          case "0": // connection request
            System.out.println("Connected to " + key);
            response = incoming.readLine(); // receive the client's address list
            // parse and update the address list
            // TODO, NOTE: should print out addresses added
            break;
          case "1": // message
            System.out.println("Me: " + payload);
            break;
          case "2": // disconnection request
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
