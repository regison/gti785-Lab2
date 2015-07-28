package com.client.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.client.servermanager.ServerManager;
import com.client.servermanager.ServerObject;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class Utils {

	@SuppressWarnings("rawtypes")
	public static int GetScannedDevicesFromSharedPreferences(
			SharedPreferences settings) {
		if (ServerManager.getInstance().getServers().size() == 0) {
			if (settings.getAll().size() > 0) {
				@SuppressWarnings({ "unchecked" })
				ArrayList<LinkedTreeMap> servers = (ArrayList<LinkedTreeMap>) new Gson()
						.fromJson(settings.getString("servers", ""),
								ArrayList.class).get(0);

				if (servers != null && servers.size() > 0) {

					LinkedTreeMap splitserver = null;

					String name = "";
					String ip = "";
					String port = "";

					String[] w = null;

					// more than one server (device) scanned
					for (LinkedTreeMap test : servers) {

						ServerObject server = new ServerObject();
						String test2 = test
								.get("serverPort")
								.toString()
								.substring(
										0,
										test.get("serverPort").toString()
												.length() - 2);
						server.setUrl(test.get("URL").toString());
						server.setAvailable((boolean) test.get("isAvailable"));
						server.setServerName(test.get("serverName").toString());
						server.setServerPort(Integer.parseInt(test2));
						server.setServerIPAdress(test.get("serverIPAdress")
								.toString());
						server.setLastDateAccess(test.get("lastDateAccess") != null ? test
								.get("lastDateAccess").toString() : "");
						server.setDistanceSaved(test.get("distance").toString());

						if (test.get("serverLocation") != null) {
							// Get Location from sub-list
							LinkedTreeMap location = (LinkedTreeMap) test
									.get("serverLocation");

							double latitude = Double.parseDouble(location.get(
									"mLatitude").toString());
							double longitude = Double.parseDouble(location.get(
									"mLongitude").toString());

							Location locServer = new Location("serverSaved");
							locServer.setLatitude(latitude);
							locServer.setLongitude(longitude);

							server.setServerLocation(locServer);
						}
						ServerManager.getInstance().addServer(server);
					}
				}
				return 1;
			}
		}
		return 0;
	}

	public static Location GetDeviceLocation(Activity current) {

		boolean isGPSEnabled, isNetworkEnabled;
		// boolean isenable = isLocationEnabled(current);
		LocationManager locationManager = (LocationManager) current
				.getSystemService(Context.LOCATION_SERVICE);		
	
		// getting GPS status 
        isGPSEnabled = locationManager 
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
        // getting network status 
        isNetworkEnabled = locationManager 
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        


		Location loc =  locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        
        if (!isGPSEnabled && !isNetworkEnabled) { 
            // no network provider is enabled 
        } else {             
            if (isNetworkEnabled) { 
            	
            	if (loc == null){
                locationManager.requestLocationUpdates( 
                        LocationManager.NETWORK_PROVIDER,
                        5, 
                        5, new LocationListener() {

    						@Override
    						public void onStatusChanged(String provider,
    								int status, Bundle extras) {
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

    						@Override
    						public void onLocationChanged(Location location) {
    							// TODO Auto-generated method stub

    						}
    					});
            	}
                //MainActivity.Log.d("Network", "Network Enabled");
                if (locationManager != null) { 
                    loc = locationManager 
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (loc != null) { 
                    	DeviceLocalisation deviceLoc = new DeviceLocalisation(loc, current);
                    	deviceLoc.setLocation(loc);

                		deviceLoc.getLocationUpdates(locationManager);
                		
                		return loc;
                    } 
                } 
            } 
            // if GPS Enabled get lat/long using GPS Services 
            if (isGPSEnabled) { 
                if (loc == null) { 
                	locationManager.requestLocationUpdates( 
                            LocationManager.GPS_PROVIDER,
                            5, 
                            5, new LocationListener() {

        						@Override
        						public void onStatusChanged(String provider,
        								int status, Bundle extras) {
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

        						@Override
        						public void onLocationChanged(Location location) {
        							// TODO Auto-generated method stub

        						}
        					});
                    Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) { 
                        loc = locationManager 
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) { 
                        	DeviceLocalisation deviceLoc = new DeviceLocalisation(loc, current);
                        	deviceLoc.setLocation(loc);

                    		deviceLoc.getLocationUpdates(locationManager);
                    		return loc;
                        } 
                    } 
                } 
            } 
        }
 
		
		return loc;
	}

	public static boolean isLocationEnabled(Context context) {
		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				locationMode = Settings.Secure.getInt(
						context.getContentResolver(),
						Settings.Secure.LOCATION_MODE);

			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}

			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		} else {
			locationProviders = Settings.Secure.getString(
					context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}
	}

	public static void SaveCurrentServers(SharedPreferences settings) {

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context

		SharedPreferences.Editor editor = settings.edit();

		editor.putString("servers", new Gson().toJson(Arrays
				.asList(ServerManager.getInstance().getServers())));

		// Commit the edits!
		editor.commit();
	}

	public static String GetFiles() {
		// TODO Auto-generated method stub
		File root = new File(Environment.DIRECTORY_DOWNLOADS);
		String sFiles = null;
		final File[] files =  root.listFiles();
		
		for(File f : files){

			if (f.isFile()){
				if (files.length == 1)
				sFiles = f.getAbsolutePath();
				else
					sFiles += f.getAbsolutePath() +",";			
			}
		}
			
		
		return sFiles;
	}

}
