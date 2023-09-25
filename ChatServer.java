import java.io.*;
import java.net.*;
import java.util.*;

class ChatServerSession extends Thread {
	private ChatServer cs;
	private Socket s;
	private BufferedReader read;
	private PrintWriter write;

	public ChatServerSession(ChatServer cs, Socket s) {
		this.s = s;
		this.cs = cs;
	}

	public void send(String s) {
		try {
			write.println(s);
		} catch (Exception e) {
			System.err.println("Exception " + e);
		}
	}

	public void run() { /* entry point into the ChatServerSession */
		try {
			read = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
			write = new PrintWriter(s.getOutputStream(), true);

			while (true) {
				/* read a line of text, and then echo it back */
				String in = read.readLine();
				cs.sayToAll(this, in);
			}
			/* close the socket */
			//s.close();
		} catch (Exception e) {
			System.err.println("Exception: " + e);
		}
	}
}

class ChatServer {
	private ArrayList<ChatServerSession> sessions;
	
	public void sayToAll(ChatServerSession from, String text) {
		int len = sessions.size();
		for(int i=0; i<len; i++) {
				ChatServerSession session = sessions.get(i);
				if(session != from)
			session.send(text);
		}
	}

	public void start_server() {
		try {
			/* listen on port 51234 for incoming connections */
			ServerSocket ss = new ServerSocket(51234);
 			sessions = new ArrayList<ChatServerSession>();

			/* loop around, accepting new connections as they arrive */
			while(true) {
				Socket s = ss.accept();
				ChatServerSession session = new ChatServerSession(this, s);
				sessions.add(session);

				/* the start method causes the thread to run */
				session.start();
			}
		} catch(Exception e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start_server();
	}
}