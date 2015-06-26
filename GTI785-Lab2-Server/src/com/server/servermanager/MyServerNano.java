package com.server.servermanager;

import java.util.Map;
import java.util.logging.Logger;

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
        
        switch(uri){
        
        case "/getfileslist" :  
        	
        	break;  
        case "/getgeopos":
        		
        	break;
       
        }
        
        return newFixedLengthResponse(response);    
    }

}
