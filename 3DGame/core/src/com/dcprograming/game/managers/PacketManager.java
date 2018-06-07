package com.dcprograming.game.managers;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import com.dcprogramming.game.networking.Packet;

/**
 * The PacketManager class dynamicly manages all the packets, replacing update
 * packets and adding initialization packets
 * 
 * @author 50018003
 * @dateCreated May 28, 2018
 * @dateCompleted June 29, 2018
 * @version 1.0
 */
public class PacketManager {

	public HashMap<String, ArrayList<Packet>> data = new HashMap<String, ArrayList<Packet>>();

	/**
	 * Constructor parses out data from packetmanager in string form
	 * 
	 * @param data
	 */
	public PacketManager(String data) {
		if (data.equals("")) {
			return;
		}
		String[] ownerPackets = data.split("/");
		for (String op : ownerPackets) {
			String owner = op.substring(0, op.indexOf(">"));
			this.data.put(owner, new ArrayList<Packet>());
			String[] packets = op.substring(op.indexOf(">") + 1).split(";");
			for (String p : packets) {
				this.data.get(owner).add(new Packet(p));
			}
		}
	}

	/**
	 * Removes a packet owner from the packet manager
	 * 
	 * @param owner
	 */
	public void removeOwner(String owner) {
		data.remove(owner);
	}

	/**
	 * Dynamically adds new packets
	 * 
	 * @param owner - Username of packet owner
	 * @param p - Packet being pushed
	 */
	public void pushPacket(String owner, Packet p) {
		if (data.keySet().contains(owner)) {
			for (int i = 0; i < data.get(owner).size(); i++) {
				if (data.get(owner).get(i).getIdentifier().equals(p.getIdentifier())) {
					data.get(owner).remove(i);
					data.get(owner).add(p);
					return;
				}
			}
			data.get(owner).add(p);
		} else {
			data.put(owner, new ArrayList<Packet>());
			data.get(owner).add(p);
		}
	}

	/**
	 * Converts this packetmanager into a sendable string
	 * 
	 * @return String of data
	 */
	public String packageData() {
		String result = "";
		try {
			for (String s : data.keySet()) {
				result += s + ">";
				for (Packet p : data.get(s)) {
					result += p.repackage() + ";";
				}
				result += "/";
			}
		} catch (ConcurrentModificationException e) {

		}
		return result;
	}

}
