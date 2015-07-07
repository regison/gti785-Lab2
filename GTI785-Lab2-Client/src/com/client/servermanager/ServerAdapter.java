package com.client.servermanager;

import java.util.ArrayList;
import java.util.List;

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

	public ServerAdapter(Context context, List<ServerObject> servers) {

		super(context, R.layout.server_added, R.id.serverName, servers);
		this.serverContext = context;
		//this.srvs = (ArrayList<ServerObject>) servers;

		locMgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// TODO Auto-generated constructor stub
	}

	//@Override
	/*public View getView(int position, View convertView, ViewGroup parent) {

		// to fix when adding a new server
		View serverView = null;

		if (convertView == null) {

			LayoutInflater inflator = ((Activity) serverContext)
					.getLayoutInflater();

			serverView = inflator.inflate(R.layout.server_added, null);
			if (srvs.size() != 0) {
				for (int i = 0; i < srvs.size(); i++) {
					TextView serverName = (TextView) serverView
							.findViewById(R.id.serverName);
					serverName.setText(srvs.get(i).getServerName());
					TextView servUrl = (TextView) serverView
							.findViewById(R.id.serverUrl);
					servUrl.setText(srvs.get(i).getURL());
					if (srvs.get(i).isAvailable()) {

						ToggleButton toggle = (ToggleButton) serverView
								.findViewById(R.id.toggleButton1);
						toggle.setChecked(true);

					}
				}
			}
		}
		return serverView;

	}
*/
}
