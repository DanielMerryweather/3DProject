package com.dcprogramming.game.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.dcprograming.game.managers.PacketManager;

/**
 * @author 50018003 By: Daniel Merryweather The Server class starts up a server
 *         that is able to receive and distribute packets based on its connected
 *         users
 * @dateCreated May 28, 2018
 * @dateCompleted June 5, 2018
 * @version 1.3
 */
public class Server extends Thread {

	private static final int PORT = 9001;

	private ArrayList<String> usernames = new ArrayList<String>();

	private ArrayList<PrintWriter> userwriters = new ArrayList<PrintWriter>();

	private PacketManager pm = new PacketManager("");

	public ServerSocket servSocket;

	/**
	 * The initialization of this server thread, actively tries to accept new
	 * connections to the server
	 */
	public void run() {
		System.out.println("Server started on port " + PORT);
		try {
			servSocket = new ServerSocket(PORT);
			try {
				while (true) {
					new Handler(servSocket.accept(), this).start();
				}
			} finally {
				servSocket.close();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * The handler class establishes a connection to a new client, requests and
	 * receives data from that client, while giving it back the data it needs
	 */
	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private Server s;

		public Handler(Socket socket, Server s) {
			this.socket = socket;
			this.s = s;
		}

		public void run() {
			try {

				System.out.println("NEW CONNECTION!");

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("USERNAME");
					name = in.readLine();
					if (name == null) {
						return;
					}
					synchronized (s.usernames) {
						if (!s.usernames.contains(name)) {
							s.usernames.add(name);
							break;
						}
					}
				}

				out.println("USERACCEPTED");
				s.pm.pushPacket(name, new Packet("TEAM:" + (s.usernames.size() % 2 == 0 ? "RED" : "BLUE")));
				s.userwriters.add(out);

				while (true) {
					String input = in.readLine();
					if (input == null) {
						return;
					}
					if (input.equals("UPDATEREQUEST")) {
						out.println(s.pm.packageData());
					} else if (!input.equals(name)) {
						s.pm.pushPacket(name, new Packet(input));
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				System.out.println("NEW DISCONNECTION!");
				s.pm.removeOwner(name);
				if (name != null) {
					s.usernames.remove(name);
				}
				if (out != null) {
					s.userwriters.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}