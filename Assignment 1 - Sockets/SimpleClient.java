/* ID: 1573069  /  Name: Lydia Kim */
import java.net.*;
import java.io.*;

class SimpleClient{
    static public void main(String[] args){
        if (args.length < 2) {
            System.err.println("Usage: java SimpleClient <server_name_or_IP> <port_number>");
            return;
        }
        try{
            InetAddress IP = InetAddress.getByName(args[0]); //Server name or IP 
            int port = Integer.parseInt(args[1]);

            //Now connect to the server
            try{
                Socket sock = new Socket(IP, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //Read the lines from client server's response
                String line;
                while((line = reader.readLine()) != null){
                    System.out.println(line);
                }

                //Close the socket
                sock.close();
                

            } catch (IOException e){
                System.err.println("IO Exception " + e);
                return;
            }
        

        } catch(UnknownHostException e){
             // If the host name cannot be resolved, handle the UnknownHostException
            System.err.println("Unknown host " + e);
            return;
        }
        
    }
}