package com.client.localization;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class DeviceLocalisation extends Location implements LocationListener {
	private Activity activity;
	private Location location;
	private Object device;

	public DeviceLocalisation(Location l, Activity activity, Object device) {
		super(l);
		this.activity = activity;
		this.location =  l;
		this.device = device;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}
