package com.client.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;

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

		// boolean isenable = isLocationEnabled(current);
		LocationManager locationManager = (LocationManager) current
				.getSystemService(Context.LOCATION_SERVICE);

		Location loc = locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (loc == null) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

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

			loc = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}

		DeviceLocalisation deviceLoc = new DeviceLocalisation(loc, current);

		String bestProviderForDevice = DeviceLocalisation
				.getProviderName(locationManager);

		// Location instantLocation = locationManager
		// .getLastKnownLocation(bestProviderForDevice);

		if (loc != null)
			deviceLoc.setLocation(loc);

		deviceLoc.getLocationUpdates(locationManager);

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

}