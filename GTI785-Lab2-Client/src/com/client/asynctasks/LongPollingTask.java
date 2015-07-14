package com.client.asynctasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.client.activities.MainActivity;
import com.client.filemanager.FileManager;
import com.client.servermanager.ServerObject;

import android.os.AsyncTask;
import android.os.StrictMode;

public class LongPollingTask extends AsyncTask<String, String, String> {

	ServerObject srvObj = new ServerObject();
	InputStream content = null;
	HttpURLConnection conn = null;
	
	@Override
	protected String doInBackground(String... params) {

		
		String payload = "";
		String test = "";
		String payloadAddToUrl="";
		String url = params[0];
		String function = "";
		
		if (params.length > 2){
		function = params[1]; // httpfunction
		 payload = params[2];//payload
		 test = "";	
		
		}
		
		srvObj.setUrl(url.concat(function) );

		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.permitNetwork().build());

			if (payload != "") {
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
						1);
				nameValuePair.add(new BasicNameValuePair("payload", payload));
				
				payloadAddToUrl =  function + "?" + URLEncodedUtils.format( nameValuePair, "UTF-8");
				srvObj.setUrl( srvObj.getURL() + payloadAddToUrl );
			}
			conn = (HttpURLConnection) new URL(
					srvObj.getURL()).openConnection();

			conn.setRequestMethod("GET");

			conn.connect();

			int response = conn.getResponseCode();

			if (response == HttpURLConnection.HTTP_OK) {
				// on fait le traitement de la requete
				// on get la reponse du server et on etablit que son status est
				// on
				srvObj.setAvailable(true);

				content = conn.getInputStream();

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(content));

				String sResponse = "";

				while ((sResponse = buffer.readLine()) != null) {
					test +=  sResponse;
				}

			}
			if (response == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
				// son status est off
				srvObj.setAvailable(false);
			}
			if (response == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				// on recommence le polling
				srvObj.setAvailable(false);
			}

		} catch (MalformedURLException e) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally { 
            try { content.close(); } catch(Throwable t) {}
            try { conn.disconnect(); } catch(Throwable t) {}
    } 


		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

	//	new LongPollingTask().execute(srvObj.getURL());
	}

}
