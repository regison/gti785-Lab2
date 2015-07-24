package com.client.servermanager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import com.client.filemanager.File;

import android.location.Location;

public class ServerObject {
	
	private String serverName;
	private String serverIPAdress;
	private int serverPort;
	private Location serverLocation;
	private float distance;	
	private boolean isAvailable;
	private String URL;
	private String lastDateAccess;
	
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
	public void setDistance(Location server, Location device){
		if(server != null && device != null)
			this.distance = server.distanceTo(device);
		else
		this.distance = 0;
	}
	public float getDistance(){
		return distance;
	}
	public ArrayList<File> getServerFiles() {
		return serverFiles;
	}
	public void setServerFiles(ArrayList<File> serverFiles) {
		this.serverFiles = serverFiles;
	}	
	
	public String getLastDateAccess() {
		return lastDateAccess;
	}
	public void setLastDateAccess(String lastDateAccess) {
		if (lastDateAccess != null)
			this.lastDateAccess = lastDateAccess;
		else
			this.lastDateAccess = Calendar.getInstance().getTime().toString();
	}
	@Override
	public String toString(){
		return  this.serverName +"," + this.serverIPAdress + ","+ this.serverPort; 
	}
	
	public static class SortServerByDistance implements Comparator<ServerObject> {		  
		 private int mod = 1;
		    
		    public SortServerByDistance(boolean desc) {
		        if (!desc) mod =-1;        
		    }
		@Override
		public int compare(ServerObject so1, ServerObject so2) {		
			return (int) ((int) mod * (so1.getDistance() - so2.getDistance()));
		} 
	} 

	public static class SortServerByName implements Comparator<ServerObject> {
		@Override
		public int compare(ServerObject lhs, ServerObject rhs) {		
			return lhs.getServerName().compareTo( rhs.getServerName() );
		}
	 
	}
	
	public static class SortServerByLastAccessDate implements Comparator<ServerObject> {
	    private int mod = 1;
	    
	    public SortServerByLastAccessDate(boolean desc) {
	        if (desc) mod =-1;        
	    }
		@Override
		public int compare(ServerObject lhs, ServerObject rhs) {			
			return mod * lhs.getLastDateAccess().compareTo( rhs.getLastDateAccess() );
		} 
	   }
	//Seulement lorsqu'on recupere du json
	public void setDistanceSaved(String dist) {
		// TODO Auto-generated method stub
		this.distance = Float.parseFloat(dist);
		
	}

	
}


