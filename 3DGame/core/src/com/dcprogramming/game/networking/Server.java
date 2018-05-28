package com.dcprogramming.game.networking;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static final int PORT = 9001;

    private static ArrayList<String> usernames = new ArrayList<String>();

    private static ArrayList<PrintWriter> userwriters = new ArrayList<PrintWriter>();
    
    private static ArrayList<String> packets = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server started on port " + PORT);
        ServerSocket servSocket = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(servSocket.accept()).start();
            }
        } finally {
        	servSocket.close();
        }
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
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
                    }synchronized(usernames) {
                        if (!usernames.contains(name)) {
                        	usernames.add(name);
                            break;
                        }
                    }
                }

                out.println("USERNAMEACCEPTED");
                userwriters.add(out);

                while (true) {
                	String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    if(input.equals("UPDATEREQUEST")) {
                    	//System.out.println("<updatePacket>");
                    	out.println("name:data");
                    	/*for (PrintWriter writer : userwriters) {
                            writer.println("MESSAGE " + name + ": " + input);
                        }*/
                    }else {
                    	System.out.println(input);
                    	// Client sent a packet
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
            	System.out.println("NEW DISCONNECTION!");
                if (name != null) {
                	usernames.remove(name);
                }
                if (out != null) {
                	userwriters.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}