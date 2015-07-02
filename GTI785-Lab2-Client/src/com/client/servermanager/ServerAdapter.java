package com.client.servermanager;

import java.util.List;

import com.client.gti785_lab2.R;

import android.content.Context;
import android.location.LocationManager;
import android.widget.ArrayAdapter;

public class ServerAdapter extends ArrayAdapter<ServerObject> {

	private Context serverContext = null;
	private LocationManager locMgr = null;
	
	public ServerAdapter(Context context, 
			List<ServerObject> servers) {
		
		super(context, R.layout.server_added, R.id.serverName, servers);
		this.serverContext = context;
		
		locMgr = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
		
		// TODO Auto-generated constructor stub
	}


}
