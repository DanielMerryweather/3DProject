package com.dcprogramming.game.networking;

/**
 * The Packet class does all the information packaging so it is correct for
 * transfer over the socket connection
 * 
 * @author 50018003
 * @dateCreated May 28, 2018
 * @dateCompleted June 5, 2018
 * @version 1.1
 */
public class Packet {

	String identifier = "null";
	String data = "";

	/**
	 * Constructor from data, parses out the identifier and data from the packet
	 * 
	 * @param data - String packet
	 */
	public Packet(String data) {
		String[] splitData = data.split(":");
		this.identifier = splitData[0];
		try {
			this.data = splitData[1];
		} catch (Exception e) {
			this.data = "";
		}
	}

	/**
	 * Returns the identifier for this packet
	 * 
	 * @return String identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier for this packet
	 * 
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the data for this packet
	 * 
	 * @return String data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets the data for this packet
	 * 
	 * @param data
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * Converts this packets information into string format
	 * 
	 * @return packet string
	 */
	public String repackage() {
		return identifier + ":" + data;
	}

}
