package com.client.asynctasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.client.servermanager.ServerObject;

import android.location.Location;
import android.os.AsyncTask;
import android.os.StrictMode;

public class GeoPositionPollingTask extends AsyncTask<String, String, Location> {


	ServerObject srvObj = new ServerObject();
	InputStream content = null;
	HttpURLConnection conn = null;
	@Override
	protected Location doInBackground(String... params) {
		String payload = "";
		String locationFromResponse = "";
		String payloadAddToUrl="";
		
		String server[] = params[0].split(",");
		
		srvObj.setServerName(server[0]);
		srvObj.setServerIPAdress(server[1]);
		srvObj.setServerPort( Integer.parseInt(server[2]) );
		
		String function = "";
		srvObj.setUrl("http://" + server[1] +":" + server[2] +"/");
		
		if ( params.length > 1 && !params[1].isEmpty()  )
			srvObj.setUrl( srvObj.getURL() + params[1] ); // add httpfunction

		if (params.length > 2 && !params[2].isEmpty()) {

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
					1);
			nameValuePair.add(new BasicNameValuePair("payload", params[3]));
			
			payloadAddToUrl =  "?" + URLEncodedUtils.format( nameValuePair, "UTF-8");
			
			srvObj.setUrl( srvObj.getURL() + payloadAddToUrl ); // payload + 
		}

		
		
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.permitNetwork().build());
			conn = (HttpURLConnection) new URL(srvObj.getURL()).openConnection();

			conn.setRequestMethod("GET");
			
			conn.connect();

			int response = conn.getResponseCode();
		
			
			if (response == HttpURLConnection.HTTP_OK) {
			
				content = conn.getInputStream();

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(content));

				String sResponse = "";

				while ((sResponse = buffer.readLine()) != null) {
					locationFromResponse +=  sResponse;
				}
				
				double longitude  = Double.parseDouble( locationFromResponse.trim().split(",")[0] );
				double latitude = Double.parseDouble( locationFromResponse.trim().split(",")[1] );
				
				Location locServer =  new Location("server");
				locServer.setLatitude( latitude );
				locServer.setLongitude( longitude );
				
				return locServer;
				

			}
			else if (response == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
				// son status est off
				srvObj.setAvailable(false);
			}
			else if (response == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				// on recommence le polling
				srvObj.setAvailable(false);
			}
			else
				srvObj.setAvailable(false);

		} catch (MalformedURLException e) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			try {
				content.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	conn.disconnect();
				srvObj.setAvailable(false);
			e.printStackTrace();
		}
		finally { 
            try { 
            	content.close();
            	conn.disconnect();
            } catch(Throwable t) {}
            
    } 


		return null;
	}
	protected void onProgressUpdate(Location result) {
		//super.onProgressUpdate(result);

	//	new LongPollingTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, srvObj.toString());
	}

}
