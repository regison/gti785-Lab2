package com.client.asynctasks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.client.filemanager.FileManager;
import com.client.servermanager.ServerObject;

import android.os.AsyncTask;
import android.os.StrictMode;

public class LongPollingTask extends AsyncTask<ArrayList<ServerObject>, String, String> {

	ArrayList<ServerObject> currentAddedServers= null;
	@Override
	protected String doInBackground(ArrayList<ServerObject>... params) {	
		
		currentAddedServers = params[0];
		
		try {
			for (ServerObject srvObj : currentAddedServers){			
				
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

				HttpURLConnection conn = (HttpURLConnection) new URL( srvObj.getURL() ).openConnection();

				conn.setRequestMethod("GET");
						
				conn.connect();	
				
				int response = conn.getResponseCode();
				
				if ( response == HttpURLConnection.HTTP_OK){
					//on fait le traitement de la requete
					//on get la reponse du server et on etablit que son status est on
					srvObj.setAvailable(true);				
				}
				if (response == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
					//son status est off				
					srvObj.setAvailable(false);
				}
				if (response == HttpURLConnection.HTTP_INTERNAL_ERROR){
					//on recommence le polling
					srvObj.setAvailable(false);
				}
			}
		
		} 
		catch (MalformedURLException e) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
		
		return null;
	}
	
	  @SuppressWarnings("unchecked")
	@Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        
	        new LongPollingTask().execute(currentAddedServers);
	    }


}
