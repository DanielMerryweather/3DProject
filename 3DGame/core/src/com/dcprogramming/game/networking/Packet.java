package com.dcprogramming.game.networking;

public class Packet {

	String identifier = "null";
	String data = "";

	public Packet(String data) {
		String[] splitData = data.split(":");
		this.identifier = splitData[0];
		try {
			this.data = splitData[1];
		} catch (Exception e) {
			this.data = "";
		}
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String repackage() {
		return identifier + ":" + data;
	}

}
