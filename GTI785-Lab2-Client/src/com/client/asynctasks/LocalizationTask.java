package com.client.asynctasks;

import android.os.AsyncTask;
import android.location.*;

public class LocalizationTask extends AsyncTask<String, String, Location> {

	
	@Override
	protected Location doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void onProgressUpdate(Location... locations) {
	    super.onProgressUpdate(null);
	    //updateWithNewLocation(location);
	}

}
