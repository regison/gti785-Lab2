package com.client.servermanager;

import android.widget.TextView;

public class ViewHolderServer {

	private TextView serverName;
	private TextView serverURL;
	private TextView serverDistance;
	public TextView getServerName() {
		return serverName;
	}
	public void setServerName(TextView serverName) {
		this.serverName = serverName;
	}
	public TextView getServerURL() {
		return serverURL;
	}
	public void setServerURL(TextView serverURL) {
		this.serverURL = serverURL;
	}
	public TextView getServerDistance() {
		return serverDistance;
	}
	public void setServerDistance(TextView serverDistance) {
		this.serverDistance = serverDistance;
	}
}
