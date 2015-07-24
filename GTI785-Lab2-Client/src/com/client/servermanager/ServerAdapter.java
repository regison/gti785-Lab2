package com.client.servermanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.client.asynctasks.GeoPositionPollingTask;

import com.client.gti785_lab2.R;


import android.app.Activity;
import android.content.Context;
import android.location.Location;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ServerAdapter extends ArrayAdapter<ServerObject> {

	private ArrayList<ServerObject> srvs = null;
	private Activity mAct;
	private LayoutInflater inflater = null;
	private Location deviceLocation;

	public ServerAdapter(Activity act, Location currentLocation,
			List<ServerObject> servers) {

		super(act, R.layout.server_added, servers);
		this.mAct = act;
		// this.serverContext = context;
		this.srvs = (ArrayList<ServerObject>) servers;
		this.deviceLocation = currentLocation;

		inflater = (LayoutInflater) mAct
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// setDistanceBetweenClientAndServer(currentLocation, srvs);

	}

	// @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// to fix when adding a new server
		View serverView = convertView;
		
		ServerObject srv2 = getItem(position);

		ViewHolderServer viewHolder;

		if (convertView == null) {
			serverView = inflater.inflate(R.layout.server_added, parent, false);

			viewHolder = new ViewHolderServer();
			viewHolder.setServerName((TextView) serverView
					.findViewById(R.id.serverName));
			viewHolder.setServerURL((TextView) serverView
					.findViewById(R.id.serverUrl));
			viewHolder.setServerDistance((TextView) serverView
					.findViewById(R.id.serverDistance));

			serverView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderServer) serverView.getTag();
		}

		viewHolder.getServerName().setText(srv2.getServerName());
		viewHolder.getServerURL().setText(srv2.getURL());
		viewHolder.getServerDistance().setText(
				String.valueOf((float) Math.round(srv2.getDistance()) / 1000 + " km"));

		// On vérifie le status
		String url = "http://" + srv2.getServerIPAdress() + ":"
				+ srv2.getServerPort() + "/" + "status";

		srv2.setAvailable(ServerManager.getStatusOfServer(url));

		if (srv2.isAvailable()) {

			ToggleButton toggle = (ToggleButton) serverView
					.findViewById(R.id.toggleButton1);
			toggle.setChecked(true);
			
			srv2.setLastDateAccess( Calendar.getInstance().getTime().toString() );

			TextView distance = (TextView) serverView
					.findViewById(R.id.serverDistance);
			String distanceTo = null;
			
			if (srv2.getServerLocation() == null) {
				
				// get the current location of the server
				Location loc = null;
				try {
					loc = new GeoPositionPollingTask().executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, srv2.toString(),
							"getgeopos").get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
				if (loc != null)
					srv2.setServerLocation(loc);		
			
				 distanceTo = (float) Math
						.round(getDistanceBetweenClientAndServer(deviceLocation,
								srv2))
						/ 1000 + " km";
				distance.setText(distanceTo);
			}
			else{
				distanceTo  = (float) Math.round(srv2.getDistance()) / 1000 + " km";
				distance.setText( distanceTo );
			}				
		}
		

//		notifyDataSetChanged();
		
		return serverView;
	}

	public Float getDistanceBetweenClientAndServer(Location instantLocation,
			ServerObject srv) {

		srv.setDistance(srv.getServerLocation(), instantLocation);

		return srv.getDistance();

	}
}
