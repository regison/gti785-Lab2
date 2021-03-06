package com.client.filemanager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

public class FileManager {
	// We have a list of server for a client
	private ArrayList<File> files;

	// Lets make a singleton of this class. per client there is only one server
	// manager
	private static FileManager instance = null;

	public static FileManager getInstance() {
		if (instance == null)
			instance = new FileManager();

		return instance;
	}

	public FileManager() {
		files = new ArrayList<File>();
	}

	public void setFiles(ArrayList<File> fileList) {
		files = fileList;
	}

	public void setFilesFromUnencodedString(String fileList) {
		String[] list = fileList.split(",");

		for (int i = 0; i < list.length; i++) {
			files.add(new File(list[i]));
		}
	}

	public void setFilesFromEncodedString(String fileList)
			throws UnsupportedEncodingException {
		setFilesFromUnencodedString(DecodeString(fileList));
	}

	public void AddFile(String file) {
		files.add(new File(file));
	}

	public void AddFile(File file) {
		files.add(file);
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	private String DecodeString(String files)
			throws UnsupportedEncodingException {
		byte[] octets = Base64.decode(files, Base64.URL_SAFE);
		return new String(octets, "UTF-8");
	}

	public File getFileAt(int position) {
		return files.get(position);
	}

	public static boolean TransferFile(String urlSource, File source,
			String urlDestination) {

		if (urlSource != null && source != null && urlDestination != null) {

			try {
				String url = urlDestination + "transferfile?payload="
						+ urlSource + source.getFilePath();

				StrictMode
						.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
								.permitNetwork().build());

				HttpURLConnection conn = (HttpURLConnection) new URL(url)
						.openConnection();

				conn.setRequestMethod("GET");

				conn.connect();

				int response = conn.getResponseCode();

				if (response == HttpURLConnection.HTTP_OK)
					return true;

			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
				return false;
			}
		}
		return false;
	}

	public ArrayList<File> getFileFromServer(String url) {
		try {

			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.permitNetwork().build());

			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();

			conn.setRequestMethod("GET");

			conn.connect();

			int response = conn.getResponseCode();

			if (response == HttpURLConnection.HTTP_OK) {

				InputStream content = conn.getInputStream();

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(content));

				String sResponse = "";
				String files = "";

				while ((sResponse = buffer.readLine()) != null) {
					files += sResponse;
				}

				setFilesFromUnencodedString(files);

				return getFiles();
			}

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return null;
	}
}
