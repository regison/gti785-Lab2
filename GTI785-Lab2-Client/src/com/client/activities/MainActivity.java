package com.client.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.client.gti785_lab2.R;
import com.client.servermanager.ServerAdapter;
import com.client.servermanager.ServerManager;
import com.client.servermanager.ServerObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	private static final Logger LOG = Logger.getLogger(MainActivity.class.getName());
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	//identite de la section àa afficher dans le fragement
	private int sectionId;
	
	private static ListView generalListView;
	private static final String PREFERENCES_APP_SERVEURS = "MyServerList";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
				
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		//Restauration des informations des serveurs pris en photo	
		SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFERENCES_APP_SERVEURS, 0);
	    settings = getApplicationContext().getSharedPreferences(PREFERENCES_APP_SERVEURS, 0);
	    String value = settings.getString("servers", "");
	   
	    if (value != null){
	    	String[] serversFromPersistentValue = value.split(";");
	    	
	    	for(String savedServer : serversFromPersistentValue)
	    		ServerManager.getInstance().addServerFromCode(savedServer);
	    }
	    
	}

	static final int REQUEST_IMAGE_CAPTURE = 1;

	private void getQrCodeServerPicture() {
				
	    Intent takePictureIntent = new Intent("com.google.zxing.client.android.SCAN");

	    takePictureIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, 0);
	    }
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Bitmap mBitmap = null;
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Toast.makeText(this, contents,Toast.LENGTH_LONG).show();
				
				// We need an Editor object to make preference changes.
			      // All objects are from android.context.Context
			      SharedPreferences settings = getSharedPreferences(PREFERENCES_APP_SERVEURS, 0);
			      SharedPreferences.Editor editor = settings.edit();
			      
			      //Will help when we re-run the app
			      String addserver = contents + ";";
			      editor.putString("servers", addserver);

			      // Commit the edits!
			      editor.commit();				
								
				 ServerManager.getInstance().addServerFromCode(contents);
				// Handle successful scan
			} 
			else if (resultCode == RESULT_CANCELED) {
		//Handle cancel
			}
		}
	}
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		sectionId = number;
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);			
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			
			restoreActionBar();
			//show add server button on the view for add server
			if (sectionId == 1){
				final Button btn = (Button) findViewById(R.id.add_server);
				btn.setOnClickListener(new OnClickListener() {
		            public void onClick(View v) {
		                // Perform action on click
		            	getQrCodeServerPicture();
		            }
		        });
			}
			
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment  {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		static int section = 0;
		TextView  pos = null; 
		Location instantLocation = null;
		
		public static PlaceholderFragment newInstance(int sectionNumber) {
			section = sectionNumber;
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null ;
			
			switch (section){
			case 1:
				rootView = inflater.inflate(R.layout.fragment_servers, container,
						false);		
				
				ArrayList<ServerObject> srvs =  ServerManager.getInstance().getServers();
				
				MainActivity.LOG.info("Servers available:" + srvs.size());
				
				ServerAdapter adapter = new ServerAdapter(rootView.getContext(),
						                                  ServerManager.getInstance().getServers());
				
				generalListView = (ListView) rootView.findViewById(R.id.serversView);
				generalListView.setAdapter(adapter);				
				
				
				generalListView.setOnItemClickListener(new OnItemClickListener() {
				
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Log.d("Item clicked", generalListView.getAdapter().getItem(position) + " has been selected!");
					}
				});
				
				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_files, container,
						false);				
				break;
			case 3:
				rootView = inflater.inflate(R.layout.fragment_phone_position, container,
						false);		
				
				// Don't initialize location manager, retrieve it from system services.
				LocationManager locationManager = (LocationManager) getActivity()
				        .getSystemService(Context.LOCATION_SERVICE);
				 
				instantLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				
				pos = (TextView) rootView.findViewById(R.id.phoneLocation);
				
				LocationListener locationListener = new LocationListener() {
				 
					
				    @Override
				    public void onStatusChanged(String provider, int status, Bundle extras) {
				    }
				 
				    @Override
				    public void onProviderEnabled(String provider) {
				        Toast.makeText(getActivity(),
				                "Provider enabled: " + provider, Toast.LENGTH_SHORT)
				                .show();
				    }
				 
				    @Override
				    public void onProviderDisabled(String provider) {
				        Toast.makeText(getActivity(),
				                "Provider disabled: " + provider, Toast.LENGTH_SHORT)
				                .show();
				    }
				 
				    @Override
				    public void onLocationChanged(Location location) {
				        // Do work with new location. Implementation of this method will be covered later.
				        doWorkWithNewLocation(location);
				    }
				};
				 
				long minTime = 5 * 1000; // Minimum time interval for update in seconds, i.e. 5 seconds.
				long minDistance = 10; // Minimum distance change for update in meters, i.e. 10 meters.
				 
				// Assign LocationListener to LocationManager in order to receive location updates.
				// Acquiring provider that is used for location updates will also be covered later.
				// Instead of LocationListener, PendingIntent can be assigned, also instead of 
				// provider name, criteria can be used, but we won't use those approaches now.
				locationManager.requestLocationUpdates(getProviderName(), minTime,
				        minDistance, locationListener);		
				doWorkWithNewLocation(instantLocation);
				
				//MapView mapView = (MapView) rootView.findViewById(R.id.map_view);
				break;
			}			
			
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

		
		/**
		 * Get provider name.
		 * @return Name of best suiting provider.
		 * */
		String getProviderName() {
		    LocationManager locationManager = (LocationManager) getActivity()
		            .getSystemService(Context.LOCATION_SERVICE);
		 
		    Criteria criteria = new Criteria();
		    criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
		    criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
		    criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
		    criteria.setAltitudeRequired(false); // Choose if you use altitude.
		    criteria.setBearingRequired(false); // Choose if you use bearing.
		    criteria.setCostAllowed(false); // Choose if this provider can waste money :-)
		 
		    // Provide your criteria and flag enabledOnly that tells
		    // LocationManager only to return active providers.
		    return locationManager.getBestProvider(criteria, true);
		}
		/**
		* Make use of location after deciding if it is better than previous one.
		*
		* @param location Newly acquired location.
		*/
		void doWorkWithNewLocation(Location location) {
		    if(isBetterLocation(instantLocation, location)) {
		        // If location is better, do some user preview.
		        Toast.makeText(getActivity(),
		                        "Better location found: " + getProviderName(), Toast.LENGTH_SHORT)
		                        .show();				
		    }
		    
		    String lat = String.valueOf(instantLocation.getLatitude());
		    String lon = String.valueOf(instantLocation.getLongitude());
		    Log.e("GPS", "location changed: lat="+lat+", lon="+lon);	    
		    
		    pos.setText("Latitude: "+ (double) Math.round(instantLocation.getLatitude() *10000) / 10000 + " Longitude: "+ (double) Math.round(instantLocation.getLongitude() * 10000) / 10000);
		    
		    String label = "Current location";
		    String uriBegin = "geo:" + lat + "," + lon;
		    String query = lat + "," + lon + "(" + label + ")";
		    String encodedQuery = Uri.encode(query);
		    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		    Uri uri = Uri.parse(uriString);
		    
		    Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
		  
		    startActivity(mapIntent);
		 
		
		}
		 
		/**
		* Time difference threshold set for one minute.
		*/
		static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;
		 
		/**
		* Decide if new location is better than older by following some basic criteria.
		* This algorithm can be as simple or complicated as your needs dictate it.
		* Try experimenting and get your best location strategy algorithm.
		* 
		* @param oldLocation Old location used for comparison.
		* @param newLocation Newly acquired location compared to old one.
		* @return If new location is more accurate and suits your criteria more than the old one.
		*/
		boolean isBetterLocation(Location oldLocation, Location newLocation) {
		    // If there is no old location, of course the new location is better.
		    if(oldLocation == null) {
		        return true;
		    }
		 
		    // Check if new location is newer in time.
		    boolean isNewer = newLocation.getTime() > oldLocation.getTime();
		 
		    // Check if new location more accurate. Accuracy is radius in meters, so less is better.
		    boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation.getAccuracy();       
		    if(isMoreAccurate && isNewer) {         
		        // More accurate and newer is always better.         
		        return true;     
		    } else if(isMoreAccurate && !isNewer) {         
		        // More accurate but not newer can lead to bad fix because of user movement.         
		        // Let us set a threshold for the maximum tolerance of time difference.         
		        long timeDifference = newLocation.getTime() - oldLocation.getTime(); 
		 
		        // If time difference is not greater then allowed threshold we accept it.         
		        if(timeDifference > -TIME_DIFFERENCE_THRESHOLD) {
		            return true;
		        }
		    }
		 
		    return false;
		}
	}

}
