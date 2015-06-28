package com.client.servermanager;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
		
		String [] token =  qrCodeString.split(";");
		
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
			servertoAddtoList.setServerName( token[2] );
			
			servers.add( servertoAddtoList );
			
			new LongPollingTask().execute(servertoAddtoList);
		}
		
	}

	public ArrayList<ServerObject> getServers(){
		return servers;
	}
	
}
