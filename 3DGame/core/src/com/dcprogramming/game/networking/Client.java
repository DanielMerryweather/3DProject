package com.dcprogramming.game.networking;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public abstract class Client {

	static String connectableAddress = "";
	static Socket socket;
	static boolean successfullyConnected = false;
	
	static Queue<Packet> packets = new LinkedList<Packet>();
	
	static BufferedReader in;
    static PrintWriter out;
	
    public static void main(String[] args) {
    	Client c = new Client("127.0.0.1") {
			@Override
			public void handlePacket(Packet p) {
				System.out.println(p.getData());
			}
    	};
    	
    	Scanner sl = new Scanner(System.in);
    	sendPacket(new Packet(sl.nextLine()));
    }
    
	public Client(String connectableAddress) {
		this.connectableAddress = connectableAddress;
		new Connection().start();
		new Retriever(1000, this).start();
	}
	
	private static class Retriever extends Thread{
		
		int tickTime;
		Client c;
		
		public Retriever(int tickTime, Client c) {
			this.tickTime = tickTime;
			this.c = c;
		}
		
		public void run() {
			while(true) {
				c.serverUpdateRequest();
				while(packets.size() > 0) {
					c.handlePacket(packets.poll());
				}
				try {
					Thread.sleep(tickTime);
				}catch(Exception e) {}
			}
		}
	}
	
	private static class Connection extends Thread {
		
		public Connection() {
	        try {
				socket = new Socket(connectableAddress, 9001);
				in = new BufferedReader(new InputStreamReader(
				    socket.getInputStream()));
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
	            if (line.startsWith("USERNAME")) {
	                out.println(System.getProperty("user.name"));
	            } else if (line.startsWith("USERNAMEACCEPTED")) {
	                successfullyConnected = true;
	            } else if(line != null){
	            	//System.out.println(line);
	                packets.add(new Packet(line));
	            }
	        }
		}
		
	}
	
	public void connect() throws UnknownHostException, IOException {
	}
	
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void serverUpdateRequest() {
		out.println("UPDATEREQUEST");
	}
	
	public abstract void handlePacket(Packet p);
	
	public static void sendPacket(Packet p) {
		out.println(p.repackage());
	}

}
