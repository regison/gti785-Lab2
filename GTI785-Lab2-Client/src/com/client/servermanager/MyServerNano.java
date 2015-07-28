package com.client.servermanager;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.client.activities.MainActivity;
import com.client.utils.Utils;
import com.client.utils.DeviceLocalisation;

import fi.iki.elonen.NanoHTTPD;;

public class MyServerNano extends NanoHTTPD{
	/**
     * logger to log to.
     * 
     * 
     * 
     */
	
	
    private static final Logger LOG = Logger.getLogger(MyServerNano.class.getName());

    public static void main(String[] args) {
      //  ServerRunner.run(MyMediaServer.class);
    }

    public MyServerNano() {
        super(8080);
    }

    public MyServerNano(String hostname, int port){
    	super(hostname, port);
    }
    
    
    @Override
    public Response serve(IHTTPSession session) {
    	
    	String response = "";
        Method method = session.getMethod();
        String uri = session.getUri();
        Map<String, String> param = session.getParms();
        
        MyServerNano.LOG.info(method + " '" + uri + "' ");
        
        /*LinkedBlockingQueue<Object> lbq = new LinkedBlockingQueue<>();
        try {
			lbq.poll(5, TimeUnit.SECONDS);
			lbq.put(method);
		} catch (InterruptedException e) {
		
			MyServerNano.LOG.info( e.getMessage() );
			e.printStackTrace();
		}
		*/
        
        switch(uri){
        
        case HttpFunctions.GetFileList :  
        	String files = Utils.GetFiles();
        	response = files;
        	break;  
        case HttpFunctions.GetGeoPosition:    
        
        	Activity currentAct = MainActivity.getActivity();    
        	Location serverLocation = Utils.GetDeviceLocation( currentAct );
        	response = String.valueOf( serverLocation.getLongitude()) +"," + String.valueOf( serverLocation.getLatitude());
        	
        	break;
        case HttpFunctions.TransferFile : 
        	break;        	
        case HttpFunctions.Pair: 
        	break;
        case HttpFunctions.UnPair : 
        	break;        	
        case "/notifications" : 
        	response = "notifications";
        	break;
        case HttpFunctions.Status : 
        	response = "success";
        	break;
        }        
        return newFixedLengthResponse(response);    
    }

}
