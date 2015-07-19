package com.client.asynctasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.StrictMode;

public class NotificationsPollingTask extends
		AsyncTask<String, String, Boolean> {

	String serverUrl = "";
	String ip = "";
	String port = "";
	String name = "";
	InputStream content = null;
	HttpURLConnection conn = null;
	Boolean serverStatus = false;

	@Override
	protected Boolean doInBackground(String... params) {

		String payloadAddToUrl = "";
		String function = "";
		String server[] = params[0].split(",");
		name = server[0];
		ip = server[1];
		port = server[2];
		serverUrl = "http://" + ip + ":" + port + "/";

		if (params.length > 1 && !params[1].isEmpty())
			function = params[1]; // add httpfunction

		if (params.length > 2 && !params[2].isEmpty()) {

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("payload", params[3]));

			payloadAddToUrl = "?"
					+ URLEncodedUtils.format(nameValuePair, "UTF-8");

		}

		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.permitNetwork().build());
			conn = (HttpURLConnection) new URL(serverUrl + function
					+ payloadAddToUrl).openConnection();

			conn.setRequestMethod("GET");

			conn.connect();

			int response = conn.getResponseCode();

			if (response == -1)
			 serverStatus = false;

			if (response == HttpURLConnection.HTTP_OK) {
				serverStatus = true;
			} else if (response == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
				// son status est off
				serverStatus = false;
			} else if (response == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				// on recommence le polling
				serverStatus = false;
			} else
				serverStatus = false;

		} catch (MalformedURLException e) {
			try {
				Thread.sleep(1000);
				serverStatus = false;
			} catch (InterruptedException e1) {
				serverStatus = false;
				e1.printStackTrace();

			}
			serverStatus = false;
			e.printStackTrace();
			
		} catch (IOException e) {
			serverStatus = false;
		} finally {
			try {
		
				conn.disconnect();
				
			} catch (Throwable t) {
			}

		}
		return serverStatus;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		new NotificationsPollingTask().executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR, name + "," + ip + "," + port,
				"notifications");
	}

}
