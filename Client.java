package com.dcprogramming.game.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.dcprograming.game.managers.PacketManager;

/**
 * @author 50018003
 * By: Daniel Merryweather
 * The Client class, establishes a connection to a desired server and recieves all packets in its packet manager
 * @dateCreated May 28, 2018
 * @dateCompleted June 6, 2018
 * @version 1.5
 */
public class Client {

	static String connectableAddress = "";
	static Socket socket;
	static boolean successfullyConnected = false;
	public static PacketManager pm = new PacketManager("");

	static BufferedReader in;
	static PrintWriter out;

	static Connection cnct;

	/**
	 * Constructor for connecting to a specific server
	 * @param connectableAddress - String containing an ip
	 */
	public Client(String connectableAddress) {
		this.connectableAddress = connectableAddress;
		cnct = new Connection(this, System.getProperty("user.name"));
		cnct.start();
	}

	/**
	 * The connection thread establishes a connection separate from the main application so updates can be done asynchronously, after the connection has been made, basic information such as the username and team are exchanged initally
	 */
	private static class Connection extends Thread {

		Client c;
		String name;

		public Connection(Client c, String name) {
			this.c = c;
			this.name = name;
			try {
				socket = new Socket(connectableAddress, 9001);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {
				String line = null;
				try {
					line = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!(line == null)) {
					if (line.startsWith("USERNAME")) {
						out.println(name);
					} else if (line.startsWith("USERACCEPTED")) {
						successfullyConnected = true;
					} else if (line != null) {
						System.out.println("Packet Recieved: " + line);
						pm = new PacketManager(line);
					}
				}
			}
		}

	}

	/**
	 * Disconnects the current connection for safe exit of the application
	 */
	public void disconnect() {
		try {
			cnct.stop();
			if(socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Asks the server to send all the most recent packets back to the client
	 */
	public void serverUpdateRequest() {
		if (successfullyConnected) {
			out.println("UPDATEREQUEST");
		}
	}

	/**
	 * Using this connection sends a packet to the server
	 * @param p - Packet to be sent
	 */
	public void sendPacket(Packet p) {
		if (successfullyConnected) {
			out.println(p.repackage());
		}
	}

}