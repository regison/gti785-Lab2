package com.client.servermanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.client.asynctasks.LongPollingTask;
import com.client.asynctasks.NotificationsPollingTask;
import com.client.gti785_lab2.R;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ServerAdapter extends ArrayAdapter<ServerObject> {

	private Context serverContext = null;
	private LocationManager locMgr = null;
	private ArrayList<ServerObject> srvs = null;
	private Activity mAct;
	private LayoutInflater inflater = null;
	public ServerAdapter(Activity act, List<ServerObject> servers) {

		super(act, R.layout.server_added, servers);
		this.mAct = act;
		//this.serverContext = context;
		this.srvs = (ArrayList<ServerObject>) servers;

		inflater = (LayoutInflater) mAct.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		
	}

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// to fix when adding a new server
		View serverView = convertView;
		Boolean checkIsOnline = false;
		ServerObject srv2  = getItem(position);
		/*
		ServerObject srv2 = null;
		try {
			srv2 = new LongPollingTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  getItem(position).toString() ).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ViewHolderServer viewHolder;
		
		if (convertView == null) {	
			serverView = inflater.inflate(R.layout.server_added, parent, false);
			
			viewHolder = new ViewHolderServer();
			viewHolder.setServerName(  (TextView) serverView.findViewById(R.id.serverName) );
			viewHolder.setServerURL( (TextView) serverView
						.findViewById(R.id.serverUrl));
			
			
			serverView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolderServer) serverView.getTag();
		}
		
		viewHolder.getServerName().setText( srv2.getServerName() );
		viewHolder.getServerURL().setText( srv2.getURL() );

		try {
			checkIsOnline = new NotificationsPollingTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  srv2.toString(),"status" ).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (checkIsOnline) {
			ToggleButton toggle = (ToggleButton) serverView
					.findViewById(R.id.toggleButton1);
			toggle.setChecked(true);
		}	
		
				
		return serverView;
	}

}
