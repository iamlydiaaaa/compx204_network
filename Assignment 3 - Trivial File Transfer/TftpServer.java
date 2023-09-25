import java.net.*;
import java.io.*;

///
/// The server receives a file request from the client,
/// and then sends the file to the client, dividing the file to 512bytes
///
class TftpServerWorker extends Thread {
    private DatagramPacket req;
    private static final byte RRQ = 1;
    private static final byte DATA = 2;
    private static final byte ACK = 3;
    private static final byte ERROR = 4;
    private int timeout = 1;
    private int blockNum = 1; // Initialises Block Number
    private int offset = 0;
    private byte[] dataPacket;

    // Send the error packet
    private void sendErrorPacket(int blockNum, String errMessage) {
        try {

            // 4.
            // Create an ERROR packet
            byte[] errorPacket = new byte[2 + errMessage.length()];
            errorPacket[0] = ERROR;
            errorPacket[1] = (byte) blockNum; // BlockNum

            // Copy the ERROR message to the packet
            byte[] errorMessageBytes = errMessage.getBytes("UTF-8");
            System.arraycopy(errorMessageBytes, 0, errorPacket, 2, errorMessageBytes.length);

            // Send the ERROR packet to server.
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket errRequest = new DatagramPacket(errorPacket, errorPacket.length, req.getAddress(),
                    req.getPort());

            socket.send(errRequest);
            System.out.println("4. ----ERROR '" + errMessage + "'' Sent.---->>");
            System.out.println("=================================\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Send the requested file to the client
    private void sendfile(String filename) {

        try {
            // Create a FileInputStream to read the file
            FileInputStream fileInputStream = new FileInputStream(filename);

            int bytesRead;
            byte[] buffer = new byte[512];
            DatagramSocket socket = new DatagramSocket();

            // Read the file 512bytes each until the file reached to the end
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {

                System.out.println(buffer.length + ", offset:" + offset + ", bytesRead: " + bytesRead);

                boolean ackReceived = false;

                // Set the DATA opcode
                dataPacket = new byte[bytesRead + 2];
                dataPacket[0] = DATA;
                dataPacket[1] = (byte) blockNum;

                // arraycopy (source, offset of source, whereToCopy, startingPosition of
                // whereToCopy, numberOfBytesToCopy)
                System.arraycopy(buffer, 0, dataPacket, 2, bytesRead);

                // Send the DATA to client
                DatagramPacket request = new DatagramPacket(dataPacket, dataPacket.length, req.getAddress(),
                        req.getPort());
                socket.send(request);
                System.out.println("2. Sent DATA: Block #" + blockNum + "  <" + request.getLength() + " bytes>");

                // Set a TIMEOUT for receiving ACK packets
                socket.setSoTimeout(1000);

                // 3.
                // Wait for ACK
                while (timeout < 5 && ackReceived == false) {
                    try {
                        System.out.println("\n---------------------------waiting for ACK\n");

                        // Create a new packet to receive a ACK buffer.
                        byte[] ackBuffer = new byte[2];
                        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);

                        // receive the ackPacket from the client.
                        socket.receive(ackPacket);

                        // unpack received ack packet.
                        byte[] ackPacketInfo = ackPacket.getData();
                        byte type = ackPacketInfo[0];
                        byte receivedblockNum = ackPacketInfo[1];

                        // Check IF the received opcode is ACK
                        if (type == ACK) {
                            // Handle the ACK packet
                            System.out.println("3. <<------ACK #" + receivedblockNum + " received.------");
                            System.out.println("=================================");

                            // IF ACK = BLOCK#, we've got right response, so return the ackReceived to true
                            if (receivedblockNum == blockNum) {
                                // IF the block number reaches to 127, reset the block number
                                if (receivedblockNum == 127) {
                                    System.out.println(
                                            "\n---------------------------Reset Block Number to 0-----------\n");
                                    blockNum = 0;
                                }
                                ackReceived = true;

                            } else {
                                // Incorrect ACK block. Restransmission
                                System.out.println("Received incorrect ACK. Expected: " + (blockNum) + ", Received: "
                                        + receivedblockNum);
                            }

                            // Increament block number.
                            blockNum++;
                        }

                    } catch (SocketTimeoutException e) {
                        // Handle timeout (no ACK received)
                        System.out.println("[TimeOut]");

                        // Increament timeout count, and decreament block number to try again.
                        timeout++;
                        blockNum--;

                        // todo retransmission
                        socket.send(request);
                    }
                }
                // IF timeout reaches limit of 5 times, returns ERROR and break.
                if (timeout >= 5) {
                    System.out.println("");
                    System.out.println("[ERROR] Time out limit reached! Please try again.");
                    System.out.println();
                    break;
                }

            }

            // IF the dataPacket length is 514bytes, send error to client.
            // This is used when the last packet length is 514 byte, to let the client to
            // know this is last packet.
            if (dataPacket.length == 514) {
                String errMessage = "Last packet reached";
                int blockNum = 0;
                sendErrorPacket(blockNum, errMessage);
            }

            // Completed, close fileInputStream and socket
            System.out.println("File transfer Complete! ");
            fileInputStream.close();
            socket.close();
            return;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        /*
         * parse the request packet, ensuring that it is a RRQ
         * and then call sendfile
         */
        byte[] data = req.getData();
        byte type = data[0];
        String filename = new String(data, 2, req.getLength() - 2);

        // IF received type from client is RRQ, then execute the code
        if (type == RRQ) {
            File file = new File(filename);

            // Check if file exists
            if (file.exists() && file.isFile()) {
                System.out.println(" RRQ: " + filename + ", " + "type: " + type);
                System.out.println("=================================");
                sendfile(filename);

            } else {
                // IF not, send "File not found" Error to client.
                String errMessage = " ERROR: File not found - " + filename;
                System.out.println(errMessage);
                sendErrorPacket(blockNum, errMessage);
            }

        }

        return;
    }

    public TftpServerWorker(DatagramPacket req) {
        this.req = req;
    }
}

class TftpServer {
    public void start_server() {
        try {
            DatagramSocket socket = new DatagramSocket();
            System.out.println("TftpServer on port " + socket.getLocalPort());

            for (;;) {
                byte[] buf = new byte[1472];
                DatagramPacket p = new DatagramPacket(buf, 1472);
                socket.receive(p);

                System.out.println("=================================");
                System.out.println("1. received RRQ from " + p.getAddress());

                TftpServerWorker worker = new TftpServerWorker(p);
                worker.start();

                // socket.close();
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }

        return;
    }

    public static void main(String args[]) {
        
        // Start a new server with a random port
        TftpServer d = new TftpServer();
        d.start_server();
    }
}