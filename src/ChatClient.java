import java.net.*;
import java.io.*;

public class ChatClient
{  private Socket socket              = null;
    private DataInputStream  console   = null;
    private DataOutputStream streamOut = null;

    public ChatClient(InetAddress serverName, int serverPort, String message)
    {  System.out.println("Establishing connection. Please wait ...");
        try
        {  socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        }
        catch(UnknownHostException uhe)
        {  System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch(IOException ioe)
        {  System.out.println("Unexpected exception: " + ioe.getMessage());
        }
        try
        {   streamOut.writeUTF(message);
            streamOut.flush();
        }
        catch(IOException ioe)
        {  System.out.println("Sending error: " + ioe.getMessage());
        }

        System.out.println("message sent...connection finished");
    }
    public void start() throws IOException
    {  console   = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
    }
    public void stop()
    {  try
    {  if (console   != null)  console.close();
        if (streamOut != null)  streamOut.close();
        if (socket    != null)  socket.close();
    }
    catch(IOException ioe)
    {  System.out.println("Error closing ...");
    }
    }
    public static void main(String args[]) throws UnknownHostException {
        ChatClient client = null;
        client = new ChatClient(InetAddress.getLocalHost(), 53871, "hello");
    }
}
