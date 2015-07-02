package com.client.servermanager;

import android.location.Location;

public class ServerObject {
	
	private String serverName;
	private String serverIPAdress;
	private int serverPort;
	private Location serverLocation;
	private boolean isAvailable;
	
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerIPAdress() {
		return serverIPAdress;
	}
	public void setServerIPAdress(String serverIPAdress) {
		this.serverIPAdress = serverIPAdress;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public Location getServerLocation() {
		return serverLocation;
	}
	public void setServerLocation(Location serverLocation) {
		this.serverLocation = serverLocation;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	@Override
	public String toString(){
		return "Server's name" + this.serverName +"\n" + this.serverIPAdress + ":"+ this.serverPort; 
	}
	

}
