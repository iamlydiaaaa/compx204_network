# compx204_network
Network and Protocols codes 

<h3>Assignment 1 : Introduction to Sockets</h3>
<p style="background-color:#eaeaea">
Task 1: Resolving IPv4 Addresses
In the first part of your practical, you will write a small program to obtain the IPv4 address of a corresponding DNS name.  Create a new source code file in the Terminal by typing gedit resolve.java and pressing enter.  The application gedit will open and you can start working on your program.

Your first task is to create a program that resolves a list of names provided on the command line to their corresponding IPv4 addresses. For example:

$ java resolve
Usage: resolve <name1> <name2> ... <nameN>
$ java resolve www.google.com www.facebook.com invalidname.waikato.ac.nz
www.google.com : 142.250.204.4
www.facebook.com : 31.13.78.35
invalidname.waikato.ac.nz : unknown host
To create your program, you will need to import the java.net library, and use the InetAddress class.  Make sure you catch all exceptions and ensure that your program prints a usage statement if it is not provided with any command line arguments.  The output should look exactly as above (unless the IP addresses for any of these names changes before you complete this part!)

When you are ready to test your code, save and exit the file, and then type javac resolve.java at the Terminal.  If your code is syntactically correct, it will compile without emitting any errors or warning.  If any errors are printed, please correct them.  When your code compiles without emitting warnings or errors, please type java resolve to see that it runs, and then try resolving a few addresses.

Task 2: Reverse Resolving Internet Addresses
In the second part of your practical, you will write a small program to obtain the DNS names corresponding to a list of IP addresses.  Make a copy of your resolve.java file with the command

cp resolve.java reverse.java
and then edit reverse.java to change the program so it now works as follows:

$ java reverse 130.217.250.39 130.217.250.13 127.0.0.2
130.217.250.39 : sorcerer.cms.waikato.ac.nz
130.217.250.13 : voodoo.cms.waikato.ac.nz
127.0.0.2 : no name
Make sure you change the class name of your program so it matches the name of your source code file (reverse), or you will not be able to run your code.  Also, make sure that when you specify an IP address without a name, your code says so, rather than print, for example:

$ java reverse 127.0.0.2
127.0.0.2 : 127.0.0.2
To accomplish this, you will need to use the compareTo method available with the java String class.

Simple network server
Your final task is to create a simple network server which will greet each client that connects and then disconnects them. Name your java file SimpleServer.java.  Create a ServerSocket, allowing it to bind to an available port -- you will need to read the ServerSocket documentation to find out how to do this.  You will also need to have the SimpleServer print out to the console which port it chose, so that a client can connect to it -- you will need to read the Socket documentation to find out which method reports the port it bound to.

$ java SimpleServer
Listening on port 31446
When SimpleServer accepts a new connection, obtain the IP address of the source.  Again, you will need to read the Socket documentation to find out which method reports the IP address of the peer.  Resolve the address to its name and then great the client.

Create a SimpleClient program to go with your server.  All your SimpleClient has to do is connect to the server, print out whatever the server sends back, and then exit.  The exchange should look like this:

$ java SimpleClient lab-rg07-01.cms.waikato.ac.nz 31446
Hello, lab-rg07-02.cms.waikato.ac.nz.
Your IP address is 10.12.8.52
You should be able to login to a neighbour machine using ssh and check that you can communicate with that machine over the network.
</p>

<hr>

<h3>Assignment 2 : HTTP</h3>
<p></p>

<hr>

<h3>Assignment 3</h3>
<p>
  In this assignment, you will implement a client/server system to reliably transfer files using UDP sockets. You will implement a variant of the Trivial File
Transfer Protocol (TFTP), which is an open Internet standard published in
RFC 1350. The protocol you implement will be slightly simplified from the
TFTP protocol. As a guideline, around 200 lines of source Java code are sufficient to implement the ability to send a file from the server, and about the
same number for the client.
This assignment will introduce you using the DatagramSocket and DatagramPacket Java classes. Please take some time to look through the Java documentation provided for these classes. You can find the documentation at
https://devdocs.io/openjdk~8/
</p>

<hr>
