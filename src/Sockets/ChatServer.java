package Sockets;

import java.net.*;
import java.io.*;

public class ChatServer {
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream streamIn =  null;
    private DataOutputStream streamOut = null;
    String message = null;
    String clientsPort = null;

    /**
     * Constructor de Sockets.ChatServer, escucha en un puerto determinado hasta iniciar una conexión con un socket y recibir información del mismo.
     * Luego termina la conexión
     * @param port Puerto en el que escuchara el Servidor
     */
    public ChatServer(int port)
    {  try
    {  System.out.println("Binding to port " + port + ", please wait  ...");
        server = new ServerSocket(port);
        System.out.println("Server started: " + server);
        System.out.println("Waiting for a client ...");
        socket = server.accept();
        System.out.println("Client accepted: " + socket);
        open();
        boolean done = false;
        while (!done)
        //{  try
        {  String line = streamIn.readUTF();
            int clientsPortInt = socket.getLocalPort();
            clientsPort = Integer.toString(clientsPortInt);
            System.out.println("Message from "+clientsPort+": "+line);
            done = line.equals(".bye");
            message = line;
        }
        //catch(IOException ioe)
        //{   System.out.println("why u here?");
        //    done = true;
        //}
        //}
        close();
    } catch(IOException ioe)
    {  System.out.println(ioe);
    }
        //sendMessage();
    }

    /**
     * Empieza a escuchar lo que la información del Socket luego de una aceptar una conexion
     * @throws IOException
     */
    public void open() throws IOException
    {  streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    /**
     * Termina de escuchar y reinicia los valores del socket y el inputStream
     * @throws IOException
     */
    public void close() throws IOException
    {  if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        System.out.println("Closing server...");
    }


    public static void main(String args[])
    {  ChatServer server = null;
        server = new ChatServer(999);
    }
}
