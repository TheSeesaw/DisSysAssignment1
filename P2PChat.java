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
  public HashMap<String, String[]> knownPeers;
  public ServerSocket servSock;
  public String userName;

  public P2PChat(String user, String knownIP, String knownPort, String knownName)
  {
    servSock = null;
    userName = user;
    // knownPeers = new String[1][3];
    // knownPeers[0][0] = knownIP;
    // knownPeers[0][1] = knownPort;
    // knownPeers[0][2] = knownName;
    knownPeers = new HashMap<String, String[]>();
    String[] address = new String[2];
    address[0] = knownIP;
    address[1] = knownPort;
    knownPeers.put(knownName, address);
  }

  // expected format: e1#e2#e3#e1#e2#e3#e1#e2#e3
  // where e's are strings
  // should output an array object containing the same data, ready to be appended
  // to clien's knownPeers array
  public static void parseAddressString(String addressString, HashMap addressList)
  {
    String[] addressItems = addressString.split("#");
    int fullLength = addressItems.length;
    //String[][] resultAddresses = new String[fullLength / 3][3];
    int nestIndex = 0;
    int outerIndex = 0;
    for (;nestIndex < fullLength; nestIndex+=3)
    {
      System.out.println("DEBUG: addressItems[nestIndex]");
      String[] address = new String[2];
      address[0] = addressItems[nestIndex+1];
      address[1] = addressItems[nestIndex+2];
      addressList.put(addressItems[nestIndex], address);
      outerIndex++;
    }
  }


  public static void messageDispatch(String messageType, String payload, HashMap addresses)
  {
    //int index = 0;
    InetAddress workingAddress = null;
    int workingPort = 0;
    Socket workingSocket = null;
    PrintWriter outgoing = null;
    BufferedReader incoming = null;
    String response = null;
    boolean terminate = false;

    Iterator it = addresses.entrySet().iterator();

    //for (;index < addresses.length; index++)
    while (it.hasNext())
    {
      try
      {
        Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>)it.next();
        String key = entry.getKey();
        String[] value = entry.getValue();
        // workingAddress = InetAddress.getByName(addresses[index][0]);
        // workingPort = Integer.parseInt(addresses[index][1]);
        // workingSocket = new Socket(workingAddress,workingPort);
        // outgoing = new PrintWriter(workingSocket.getOutputStream(), true);
        // // TODO handle other cases
        // outgoing.println(addresses[index][2] + ": " + payload);
        // System.out.println("Me: " + payload);
        workingAddress = InetAddress.getByName(value[0]);
        workingPort = Integer.parseInt(value[1]);
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
            parseAddressString(response, addresses);
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


  public static boolean handleUserInput(String userName, String inputStorage, P2PChat user)
  {
    if (inputStorage.equals("connectMyServer"))
    {
      messageDispatch("0", user.userName, user.knownPeers);
    }
    else if (!inputStorage.equals("quit"))
    {
      messageDispatch("1", inputStorage, user.knownPeers);
    }
    else
    {
      messageDispatch("2", user.userName, user.knownPeers);
    }
    // String messageType = inputStorage.substring(0,1);
    // String protocolCheck = inputStorage.substring(1,2);
    // if (protocolCheck.equals("#") &&
    //    (messageType.equals("0") || messageType.equals("1") || messageType.equals("2")))
    // {
    //   String payload = inputStorage.substring(2);
    //   messageDispatch(messageType, payload, user.knownPeers);
    // }
    // else
    // {
    //   System.out.println("Invalid message, please try again.");
    // }
    return true;
  }

  public static void main(String args[]) throws Exception
  {

    String userName = args[0];
    String hostName = "localhost";
    int portNumber = Integer.parseInt(args[1]);
    String knownIP = "localhost";
    String knownName = args[2];
    String knownPort = args[3];

    // initialize known peers with one other address
    P2PChat user = new P2PChat(userName, knownIP, knownPort, knownName);
    try
    {
      System.out.println("Starting server . . .");
      // create a server socket
      user.servSock = new ServerSocket(portNumber);
      if (!handleUserInput(userName, "connectMyServer", user))
      {
        System.out.println("Unable to establish a connection");
        return;
      }
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
      inputStorage = userIn.nextLine();
      active = handleUserInput(userName, inputStorage, user);
    }
    System.out.println("Now Exiting . . .");
    return;
  }
}
