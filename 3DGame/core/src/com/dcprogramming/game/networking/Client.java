package com.dcprogramming.game.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.dcprograming.game.managers.PacketManager;

public class Client {

	static String connectableAddress = "";
	static Socket socket;
	static boolean successfullyConnected = false;

	// static Queue<Packet> packets = new LinkedList<Packet>();
	public static PacketManager pm = new PacketManager("");

	static BufferedReader in;
	static PrintWriter out;

	static Connection cnt;

	/*
	 * public static void main(String[] args) { Client c = new Client("127.0.0.1");
	 * 
	 * c.sendPacket(new Packet("Y:45"));
	 * 
	 * Scanner sl = new Scanner(System.in); c.sendPacket(new Packet(sl.nextLine()));
	 * }
	 */

	public Client(String connectableAddress) {
		this.connectableAddress = connectableAddress;
		cnt = new Connection(this, System.getProperty("user.name"));
		cnt.start();
		new Retriever(8, this).start();
	}

	public Client() {
		this.connectableAddress = "127.0.0.1";
		new Connection(this, "BALL").start();
		new Retriever(8, this).start();
	}

	private static class Retriever extends Thread {

		int tickTime;
		Client c;

		public Retriever(int tickTime, Client c) {
			this.tickTime = tickTime;
			this.c = c;
		}

		public void run() {
			while (true) {
				c.serverUpdateRequest();
				try {
					Thread.sleep(tickTime);
				} catch (Exception e) {
				}
			}
		}
	}

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
						System.out.print(name);
						// out.println("Daniel");
						// out.println("Colton");
					} else if (line.startsWith("USERACCEPTED")) {
						successfullyConnected = true;
						// System.out.print("TEST");
					} else if (line != null) {
						System.out.println("Packet Recieved: " + line);
						pm = new PacketManager(line);
						/*
						 * for(String owner : pm.data.keySet()){ for(Packet p : pm.data.get(owner)){
						 * c.handlePacket(owner, p); } }
						 */
						// System.out.println(pm.packageData());
					}
				}
			}
		}

	}

	public void disconnect() {
		try {
			cnt.stop();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void serverUpdateRequest() {
		if (successfullyConnected) {
			out.println("UPDATEREQUEST");
		}
	}

	// public abstract void handlePacket(String owner, Packet p);

	public void sendPacket(Packet p) {
		if (successfullyConnected) {
			out.println(p.repackage());
		}
	}

}