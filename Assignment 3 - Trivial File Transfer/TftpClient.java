import java.net.*;
import java.io.*;


///
///  The client server requests a file to the server to get the file
///
public class TftpClient {
    private static final byte RRQ = 1;
    private static final byte DATA = 2;
    private static final byte ACK = 3;
    private static final byte ERROR = 4;

    private DatagramSocket socket;
    ByteArrayOutputStream receivedData;

    public TftpClient(String serverIp, int serverPort, String filename) {
        receivedData = new ByteArrayOutputStream();

        try {
            // Initialize socket and get client IP and port
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIp);

            InetAddress clientIp = socket.getInetAddress();
            int clientPort = socket.getLocalPort();

            System.out.println("server IP: " + serverIp + ", server port: " + serverPort + ", address: " + serverAddress);
            System.out.println("client IP: " + clientIp + ", client port: " + clientPort);

            // 1.
            // Create a new RRQ packet
            byte[] rrqPacket = new byte[2 + filename.length()];
            rrqPacket[0] = RRQ;
            rrqPacket[1] = 0;
            System.arraycopy(filename.getBytes(), 0, rrqPacket, 2, filename.length());

            // Send the prepared RRQ packet to server.
            DatagramPacket rrqRequest = new DatagramPacket(rrqPacket, rrqPacket.length, serverAddress, serverPort);
            socket.send(rrqRequest);
            System.out.println("1. RRQ Request with filename=" + filename + " Sent.");
            System.out.println("=================================");

            byte[] receivedDataPacket;
            // 2.
            // Receive DATA packets
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("copyOf" + filename);

                while (true) {
                    // Receive Data packet from the SERVER
                    DatagramPacket dataPacket = new DatagramPacket(new byte[514], 514);
                    socket.receive(dataPacket);

                    // Unpack the Data packet
                    receivedDataPacket = dataPacket.getData();
                    int type = receivedDataPacket[0];
                    byte blockNum = receivedDataPacket[1];

                    System.out.println("2. DATA Received(type=" + receivedDataPacket[0] + "), BLOCK#" + blockNum + ", <"
                            + dataPacket.getLength() + " bytes>");
                    System.out.println("=================================");

                    // Process DATA packet
                    byte[] dataBytes = new byte[dataPacket.getLength() - 2];
                    System.out.println("databyte length 1: " + dataBytes.length);

                    // arraycopy (source, offset of source, whereToCopy, startingPosition of
                    // whereToCopy, numberOfBytesToCopy)
                    System.arraycopy(receivedDataPacket, 2, dataBytes, 0, dataPacket.getLength() - 2);

                    // Check IF the received type is DATA
                    if (type == DATA) {

                        // Write DATA to the file (excluding the first 2 bytes which is type and
                        // blockNum)
                        fileOutputStream.write(dataBytes);

                        // 3.
                        // Create an ACK packet
                        byte[] ackPacket = new byte[2];
                        ackPacket[0] = ACK;
                        ackPacket[1] = blockNum; // BlockNum

                        System.out.println("3. ----ACK #" + blockNum + " Sent.---->>");
                        System.out.println("=================================\n\n");

                        // Send the ACK packet to server.
                        DatagramPacket ackRequest = new DatagramPacket(ackPacket, ackPacket.length, serverAddress,
                                dataPacket.getPort());
                        socket.send(ackRequest);

                        System.out.println("databytes length= " + dataBytes.length);

                        // Check IF this is the last DATA packet (less than 512 bytes)
                        if (dataBytes.length < 512) {
                            System.out.println("Received last DATA packet. File transfer complete!");
                            break;
                        }

                        // IF receive ERROR type, printout ERROR and break;
                    } else if (type == ERROR) {
                        System.out.println(new String(receivedDataPacket, 2, dataPacket.getLength() - 2));
                        break;
                    }

                }

                // Close the File manager and the socket
                fileOutputStream.close();
                socket.close();

            } catch (IOException e) {
                System.out.println("Error occured during receiving DATA packets.");
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: TftpClient <file name> <server port> ");
            return;
        }
        // Parse the port number
        String filename = args[0];
        int port = Integer.parseInt(args[1]);

        new TftpClient("localhost", port, filename);
    }
}