package com.client.servermanager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.location.Location;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.client.asynctasks.LongPollingTask;

public class ServerManager  {
	
	//We have a list of server for a client
	private ArrayList<ServerObject> servers;
	
	//Lets make a singleton of this class. per client there is only one server manager
	private static ServerManager instance = null;
	
	public static ServerManager getInstance(){
		if (instance == null)
			instance = new ServerManager();
		
		return instance;
	}
	
	public ServerManager (){
		servers =  new ArrayList<ServerObject>();		
	}	

	public void addServerFromCode(String qrCodeString){
		
		String [] token =  qrCodeString.split(",");
		
		//The tokens should be 
		//token[0] = ip
		//token[1] = port
		//token[2] = name
		
		// Lets create a new serverObject from those informations received
		if (token.length != 3)
			return;
		else
		{
			ServerObject servertoAddtoList = new ServerObject();
			servertoAddtoList.setServerIPAdress( token[0] );
			servertoAddtoList.setServerPort( Integer.parseInt( token[1] ) );
			servertoAddtoList.setServerName( token[2].trim() );
			servertoAddtoList.setUrl( "http://" + servertoAddtoList.getServerIPAdress() +":" + servertoAddtoList.getServerPort()+"/" );
			
			servers.add( servertoAddtoList );						
		}
		
	}

	public ArrayList<ServerObject> getServers(){
		return servers;
	}
	//Add Server from deserialize object from 
	public void addServer( ServerObject server ){
		 getServers().add(server);
	}
	
	public Location getServerLocation(int position){
		return  getServers().get(position).getServerLocation();
	}
	
	public void setLocation(int serverPos){
		
		

	}	
	public ServerObject getServerAt(int position){
		return servers.get(position);
	}
	
	public static Boolean getStatusOfServer(String url){
	        try {	 
	  
	        	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitNetwork().build());
	        	
	        	HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

				conn.setRequestMethod("GET");
				
				conn.connect();

				int response = conn.getResponseCode();
	
	            if(response == HttpURLConnection.HTTP_OK)
	               return true;
	            else
	               return false;
	 
	        } catch (Exception e) {
	            Log.d("InputStream", e.getLocalizedMessage());
	        }
	 
	        return false;
	}

}
