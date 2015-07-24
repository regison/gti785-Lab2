package com.client.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DeviceLocalisation extends Location implements LocationListener {
	private static Activity activity;
	private Location location;

	public DeviceLocalisation(Location l, Activity activity) {
		super(l);
		DeviceLocalisation.activity = activity;
		this.location = l;

		// TODO Auto-generated constructor stub
	}

	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location){
		this.location = location;
	}
	
	public Activity getActivity(){
		return activity;
	}
	public void setActivity(Activity activity){
		DeviceLocalisation.activity = activity;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(activity, "Provider enabled: " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(activity, "Provider disabled: " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLocationChanged(Location location) {
		// Do work with new location. Implementation of this
		// method will be covered later.
		doWorkWithNewLocation(location);
	}

	/**
	 * Get provider name.
	 * 
	 * @return Name of best suiting provider.
	 * */

	public static String getProviderName( LocationManager locationManager ) {
		 locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your
															// desired power
															// consumption
															// level.
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your
														// accuracy
														// requirement.
		criteria.setSpeedRequired(true); // Chose if speed for first
											// location fix is required.
		criteria.setAltitudeRequired(false); // Choose if you use altitude.
		criteria.setBearingRequired(false); // Choose if you use bearing.
		criteria.setCostAllowed(false); // Choose if this provider can waste
										// money :-)

		// Provide your criteria and flag enabledOnly that tells
		// LocationManager only to return active providers.
		return locationManager.getBestProvider(criteria, true);
	}

	/**
	 * Make use of location after deciding if it is better than previous one.
	 *
	 * @param location
	 *            Newly acquired location.
	 */
	public void doWorkWithNewLocation(Location newlocation) {
		if (isBetterLocation(location, newlocation)) {
			// If location is better, do some user preview.			
			setLocation(newlocation);
			Toast.makeText(activity,
					"Better location found: " ,
					Toast.LENGTH_SHORT).show();
		}

		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		Log.e("GPS", "location changed: lat=" + lat + ", lon=" + lon);

		String label = "Current location";
		String uriBegin = "geo:" + lat + "," + lon;
		String query = lat + "," + lon + "(" + label + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		Uri uri = Uri.parse(uriString);

	}

	// code utilisé du site d'android
	// http://developer.android.com/guide/topics/location/strategies.html

	/**
	 * Time difference threshold set for one minute.
	 */
	static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;

	/**
	 * Decide if new location is better than older by following some basic
	 * criteria. This algorithm can be as simple or complicated as your needs
	 * dictate it. Try experimenting and get your best location strategy
	 * algorithm.
	 * 
	 * @param oldLocation
	 *            Old location used for comparison.
	 * @param newLocation
	 *            Newly acquired location compared to old one.
	 * @return If new location is more accurate and suits your criteria more
	 *         than the old one.
	 */
	boolean isBetterLocation(Location oldLocation, Location newLocation) {
		// If there is no old location, of course the new location is
		// better.
		if (oldLocation == null) {
			return true;
		}

		// Check if new location is newer in time.
		boolean isNewer = newLocation.getTime() > oldLocation.getTime();

		// Check if new location more accurate. Accuracy is radius in
		// meters, so less is better.
		boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation
				.getAccuracy();
		if (isMoreAccurate && isNewer) {
			// More accurate and newer is always better.
			return true;
		} else if (isMoreAccurate && !isNewer) {
			// More accurate but not newer can lead to bad fix because of
			// user movement.
			// Let us set a threshold for the maximum tolerance of time
			// difference.
			long timeDifference = newLocation.getTime() - oldLocation.getTime();

			// If time difference is not greater then allowed threshold we
			// accept it.
			if (timeDifference > -TIME_DIFFERENCE_THRESHOLD) {
				return true;
			}
		}

		return false;
	}

	public void getLocationUpdates(final LocationManager locationManager) {
		final long minTime = 45 * 1000; // Minimum time interval for update
		// in
		// seconds, i.e. 5 seconds.
		final long minDistance = (long) 0.1; // Minimum distance change
		// for
		// update in meters, i.e. 10
		// meters.

		// Assign LocationListener to LocationManager in order to
		// receive location updates.
		// Acquiring provider that is used for location updates will
		// also be covered later.
		// Instead of LocationListener, PendingIntent can be
		// assigned,
		// also instead of
		// provider name, criteria can be used, but we won't use
		// those
		// approaches now.
		activity.runOnUiThread( new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				locationManager.requestLocationUpdates(getProviderName(locationManager), minTime,
						minDistance, DeviceLocalisation.this);
			}
			
		});
		

		// MapView mapView = (MapView)
		// rootView.findViewById(R.id.map_view);

		doWorkWithNewLocation(location);

	}
}
