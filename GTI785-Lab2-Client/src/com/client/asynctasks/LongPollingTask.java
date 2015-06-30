package com.client.asynctasks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.client.servermanager.ServerObject;

import android.os.AsyncTask;
import android.os.StrictMode;

public class LongPollingTask extends AsyncTask<ServerObject, String, String> {

	@Override
	protected String doInBackground(ServerObject... params) {
		// TODO Auto-generated method stub
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

		HttpURLConnection conn;
		
		try {
			conn = (HttpURLConnection) new URL(params[0].getServerIPAdress()).openConnection();

			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(false);				
			conn.connect();	
			
			int response = conn.getResponseCode();
			
			if ( response == HttpURLConnection.HTTP_OK){
				//on fait le traitement de la requete
				//on get la reponse du server 
				
			}
			if (response == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
				//on recommence le polling
				new LongPollingTask().execute(params);
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

}
