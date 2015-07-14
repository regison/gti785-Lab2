package com.client.servermanager;

import java.util.ArrayList;

import com.client.filemanager.File;

import android.location.Location;

public class ServerObject {
	
	private String serverName;
	private String serverIPAdress;
	private int serverPort;
	private Location serverLocation;
	private boolean isAvailable;
	private String URL;
	
	private ArrayList<File> serverFiles = new ArrayList<File>();
	
	
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
	public void setUrl(String Url){
		this.URL = Url;
	}
	public String getURL(){
		return URL;
	}
	public ArrayList<File> getServerFiles() {
		return serverFiles;
	}
	public void setServerFiles(ArrayList<File> serverFiles) {
		this.serverFiles = serverFiles;
	}
	
	@Override
	public String toString(){
		return  this.serverName +" URL:" + this.serverIPAdress + ":"+ this.serverPort; 
	}
	
	

}
