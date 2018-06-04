package com.dcprogramming.game.networking;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.dcprograming.game.managers.PacketManager;

public class Server {

    private static final int PORT = 9001;

    private static ArrayList<String> usernames = new ArrayList<String>();

    private static ArrayList<PrintWriter> userwriters = new ArrayList<PrintWriter>();
    
    private static PacketManager pm = new PacketManager("");

    public static void main(String[] args) throws Exception {
        System.out.println("Server started on port " + PORT);
        
        ServerSocket servSocket = new ServerSocket(PORT);
        try {

            new Ball().start();
            while (true) {
                new Handler(servSocket.accept()).start();
            }
        } finally {
        	servSocket.close();
        }
    }
    
    private static class Ball extends Thread {
    	
    	float bx = 0;
    	float by = 0;
    	float bz = 0;
    	
    	float bxv = 0.05f;
    	float byv = 0.1f;
    	float bzv = 0.025f;
    	
    	float grav = -9.81f;
    	
    	Client c;
    	
    	public void run(){
    		c = new Client();
    		while(true){
    			bx += bxv;
    			by += byv;
    			bz += bzv;
    			
    			if(by < 0){
    				by = 0;
    				byv = -byv;
    			}
    			
				byv += grav/1000;
    			
    			if(bx < -5){
    				bx = -5;
    				bxv = -bxv;
    			}else if(bx > 5){
    				bx = 5;
    				bxv = -bxv;
    			}
    			
    			if(bz < -5){
    				bz = -5;
    				bzv = -bzv;
    			}else if(bz > 5){
    				bz = 5;
    				bzv = -bzv;
    			}
    			
    			c.sendPacket(new Packet("X:"+bx));
    			c.sendPacket(new Packet("Y:"+by));
    			c.sendPacket(new Packet("Z:"+bz));
    			
    			ArrayList<Vector3> players = new ArrayList<Vector3>();
    			for(String plyr : c.pm.data.keySet()){
    				
    				if(plyr.equals("BALL")){
    					continue;
    				}
    				
    				float x = 0;
    				float y = 0;
    				float z = 0;
    				float pitch = 0;
    				float yaw = 0;
    				for(Packet p : c.pm.data.get(plyr)){
    					if(p.getIdentifier().equals("X")){
    						x = Float.parseFloat(p.getData());
    					}else if(p.getIdentifier().equals("Y")){
    						y = Float.parseFloat(p.getData());
    					}else if(p.getIdentifier().equals("Z")){
    						z = Float.parseFloat(p.getData());
    					}
    				}
    				players.add(new Vector3(x,y,z));
    			}
    			
    			try{
    				Thread.sleep(16);
    			}catch(Exception e){}
    		}
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

                out.println("USERACCEPTED");
                userwriters.add(out);

                while (true) {
                	String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                	//System.out.println(input);
                    if(input.equals("UPDATEREQUEST")) {
                    	out.println(pm.packageData());
                    	/*for (PrintWriter writer : userwriters) {
                            writer.println("MESSAGE " + name + ": " + input);
                        }*/
                    }else if(!input.equals(name)){
                    	//System.out.println(input);
                    	pm.pushPacket(name, new Packet(input));
                    	// Client sent a packet
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
            	System.out.println("NEW DISCONNECTION!");
            	pm.removeOwner(name);
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