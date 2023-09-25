/* ID: 1573069  /  Name: Lydia Kim */
import java.net.*;

class resolve{
    static public void main(String[] args){

        //Check if the line has the proper format
        if(args.length < 1){
            System.err.println("Usage: resolve <name1> <name2> ... <nameN>");
            return;
        }

        // Loop through all the arguments
        for (int i = 0; i < args.length; i++) {
            InetAddress ia;
            String ip;

            try {
                // Resolve the HostName to an IP address
                ia = InetAddress.getByName(args[i]);
                ip = ia.getHostAddress();

                // Print the result
                System.out.println(ia.getHostName() + " : " + ip);

            } catch (UnknownHostException e) {
                // If the host name cannot be resolved, handle the UnknownHostException
                System.err.println(args[i] + " : " + "unknown host");
                return;
            }
        }        
    }
}