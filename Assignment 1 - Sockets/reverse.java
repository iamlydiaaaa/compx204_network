/* ID: 1573069  /  Name: Lydia Kim */
import java.net.*;

class reverse{
    static public void main(String[] args){

        //Check if the line has the proper format
        if(args.length < 1){
            System.err.println("Usage: resolve <name1> <name2> ... <nameN>");
            return;
        }

        // Loop through all the arguments
        for(int i = 0; i < args.length; i++){
            InetAddress ia;
            String hostName;

            try{
                // Resolve the HostName to an IP address
                ia = InetAddress.getByName(args[i]);
                hostName = ia.getHostName();

                // Check IF the HostName exists compared to the original argument
                if (hostName.compareTo(args[i]) == 0) {
                    // Print the result if no valid host name exists
                    System.out.println(ia.getHostAddress() + " : " + "no name");
                } else {
                    System.out.println(ia.getHostAddress() + " : " + hostName);
                }

            } catch(UnknownHostException e){
                System.err.println(args[i] + " : " + "unknown host");
                return;
            }
        }

        
    }
}