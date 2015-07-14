package com.client.servermanager;

import java.util.ArrayList;
import java.util.List;

import com.client.asynctasks.LongPollingTask;
import com.client.gti785_lab2.R;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
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
		
		ServerObject srv = getItem(position);
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
		
		viewHolder.getServerName().setText( srv.getServerName() );
		viewHolder.getServerURL().setText( srv.getURL() );

		if (srv.isAvailable()) {
			ToggleButton toggle = (ToggleButton) serverView
					.findViewById(R.id.toggleButton1);
			toggle.setChecked(true);
		}		
		
		/*
				TextView serverName = (TextView) serverView
						.findViewById(R.id.serverName);				
				TextView servUrl = (TextView) serverView
						.findViewById(R.id.serverUrl);
				
				serverName.setText(srv.getServerName());
				servUrl.setText(srv.getURL());
				
				//new LongPollingTask().execute( srvs.get(i) );
				
		*/		
				
		return serverView;
	}

}
