/* ID: 1573069  /  Name: Lydia Kim */

import java.io.*;
import java.net.*;

class SimpleServer{
    static public void main(String[] args){
        try{
            //Create a server socket using an available port.
            ServerSocket ss = new ServerSocket(0);

            System.out.println("Listening on port " + ss.getLocalPort());

            //Loop around, accepting new connections as they arrive
            while(true){
                //Wait for a client
                Socket client = ss.accept();

                //Get an IP address and a hostName of the client
                InetAddress ia = client.getInetAddress();
                String ip = ia.getHostAddress();
                String hostName = ia.getHostName();

                System.out.println("Connection from :" + ip);

                // Get the information from client server then print them out
                PrintWriter write = new PrintWriter(client.getOutputStream(),true);

                write.println("Hello, " + hostName);
                write.println("Your IP address is " + ip);

                //Close the socket
                client.close();
            }
        } catch(Exception e){
            System.err.println(e);
        }
        
    }
}