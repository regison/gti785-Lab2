package com.client.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import com.client.gti785_lab2.R;
import com.client.servermanager.ServerAdapter;
import com.client.servermanager.ServerManager;
import com.client.servermanager.ServerObject;
import com.client.utils.Constants;
import com.client.utils.Utils;
import com.client.utils.DeviceLocalisation;
import com.google.gson.Gson;
import com.client.servermanager.MyServerNano;
import com.client.servermanager.MyServerQRInfo;

import fi.iki.elonen.NanoHTTPD;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	private static final Logger LOG = Logger.getLogger(MainActivity.class
			.getName());
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

	// Fragment section id (1 = list of servers, 2= files for a servers, 3=
	// localization)
	private int sectionId;
	private static SharedPreferences settings;
	private static ListView generalListView;


	private static Activity generalActivity = null;
	ProgressBar prog;
	ProgressDialog progressBar;
	int progressBarStatus = 0;
	Handler progressBarHandler = new Handler();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActivity( this );
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		// Restauration des informations des serveurs pris en photo
		settings = getSharedPreferences(Constants.PREFERENCES_APP_SERVEURS, 0);

		//A 1, ca indique qu'il y a au moins un serveur de sauvegardé dans les preferences
		if ( Utils.GetScannedDevicesFromSharedPreferences(settings) == 1)
			startProgressBarOnServersLoading();		
	}

	private void setActivity(MainActivity mainActivity) {
		MainActivity.generalActivity = mainActivity;		
	}
	
	public static Activity getActivity(){
		return generalActivity;
	}

	private void startProgressBarOnServersLoading() {
		prog = (ProgressBar) this.findViewById(R.id.progBar);
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Chargement des serveurs sauvegardés ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		// reset progress bar status
		progressBarStatus = 0;

		(new Thread(){
			@Override
			public void run() {
				while (progressBarStatus < 100) {

					try {
						sleep(200);
						
						MainActivity.this.runOnUiThread((new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								progressBarStatus += 5;
							
							}}));
				
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Update the progress bar
					progressBarHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressBarStatus);
						}
					});
				}

				// ok, file is downloaded,
				if (progressBarStatus >= 100) {

					// sleep 2 seconds, so that you can see the 100%
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// close the progress bar dialog
					progressBar.dismiss();
				}
			}
		}).start();

	}

	
	private void getQrCodeServerPicture() {

		Intent takePictureIntent = new Intent(
				"com.google.zxing.client.android.SCAN");

		takePictureIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, 0);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == 0) {
			// Handle successfull scan
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
				//Add the last taken picture to the server
				ServerManager.getInstance().addServerFromCode(contents);

			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
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
		case 4:
			mTitle = getString(R.string.title_section4);
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
			// show add server button on the view for add server
			if (sectionId == 1) {
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
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    
	    if (ServerManager.getInstance().getServers().size() > 0)
	    	Utils.SaveCurrentServers(settings);
	}




	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		static int section = 0;
		TextView pos = null;
		Location instantLocation = null;
		View rootView = null;
		ArrayList<ServerObject> srvs = null;

		Activity fragmentActivity = null;
		 int positionDansListe;
		
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

			fragmentActivity =  this.getActivity();
			
			switch (section) {
			case 1:

				rootView = inflater.inflate(R.layout.fragment_servers,
						container, false);		
				

				srvs = ServerManager.getInstance().getServers();

				generalListView = (ListView) rootView
						.findViewById(R.id.serversView);
				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_files, 
						container,	false);
				break;
			case 3:
				rootView = inflater.inflate(R.layout.fragment_phone_position,
						container, false);
				break;
			case 4:
				rootView = inflater.inflate(R.layout.fragment_qrcode,
						container, false);

				break;
			}

			return rootView;
		}

		private void showCurrentQrCode(MyServerQRInfo msqi, View view) {
			Bitmap bitmap = msqi.generateQRCode();

			ImageView qrcode = (ImageView) view.findViewById(R.id.imageView2);
			TextView txtQrCode = (TextView) view.findViewById(R.id.ipadress);
			txtQrCode.setText(msqi.getAddress());
			qrcode.setImageBitmap(bitmap);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
		

			switch (section) {
			case 1:				
				//Load device Location
				instantLocation = Utils.GetDeviceLocation(fragmentActivity);
			
				 final ServerAdapter adapter = new ServerAdapter(
							fragmentActivity, instantLocation,
							srvs);

			final Button btnSortUp = (Button) rootView.findViewById(R.id.redbtn);
			btnSortUp.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Collections.sort(srvs, new ServerObject.SortServerByDistance(true));		
					  
					adapter.notifyDataSetChanged(); 
				}
			});
			final Button btnSortDown = (Button) rootView.findViewById(R.id.btnSortDown);
			btnSortDown.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Collections.sort(srvs, new ServerObject.SortServerByDistance(false));	
					
					  adapter.notifyDataSetChanged(); 
				}
			});	
				
				generalListView.setAdapter(adapter);			

				generalListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {

								positionDansListe = position;
								ServerObject currentSrv = (ServerObject) generalListView
										.getAdapter().getItem(position);

								AlertDialog.Builder builder = new Builder(view
										.getContext());

								if (currentSrv != null
										&& currentSrv.isAvailable()) {

									builder.setTitle("Choisissez une action")
											.setNegativeButton(
													R.string.cancel,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub

														}
													})
											.setItems(
													new String[] { "Paired",
															"Supprimer" },
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															
													switch (which){
													case 0:														
														// open bluetooth if not open
														break;
													case  1:
														//Message confiramtion
														 new AlertDialog.Builder(generalListView.getContext())
													        .setIcon(android.R.drawable.ic_dialog_alert)
													        .setTitle("Supprimer serveur")
													        .setMessage("Voulex-vous vraiment supprimer ce serveur?")
													        .setPositiveButton("Oui", new DialogInterface.OnClickListener()
													    { 
													        @Override 
													        public void onClick(DialogInterface dialog, int which) {
													           // finish();    
													        	if( which == -1){
													        	
													        	srvs.remove( positionDansListe );
													        	adapter.notifyDataSetChanged();													     
													     	    	
													        	Utils.SaveCurrentServers(settings);
													        	 
													        	}
													        } 
													 
													    }) 
													    .setNegativeButton("Non", null)
													    .show();
														//delete servers from listView
														//refresh adapater
														break;
													}
															// TODO
															// Auto-generated
															// method stub
															Toast.makeText(

																	fragmentActivity,
																	dialog.toString()
																			+ " has been click!",
																	Toast.LENGTH_LONG)
																	.show();

														}
													});
								} else {
									builder.setTitle("Impossible de pairer")
											.setMessage(
													"Ce serveur ne peut être pairé,\n car il est hors ligne")
											.setNegativeButton(
													R.string.cancel,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
														}

													});
								}

								builder.create().show();

								Log.d("Item clicked",
										" His available"
												+ generalListView.getAdapter()
														.getItem(position)
														.toString());
								Toast.makeText(
										view.getContext(),
										generalListView.getAdapter()
												.getItem(position).toString()
												+ " has been selected!",
										Toast.LENGTH_LONG).show();
							}
						});

				break;
			case 2:

				break;
			case 3:
				//Load Location
				instantLocation = Utils.GetDeviceLocation(fragmentActivity);
		
				pos = (TextView) rootView.findViewById(R.id.phoneLocation);			

				pos.setText("Latitude: "
						+ (double) Math.round(instantLocation.getLatitude() * 10000)
						/ 10000
						+ " Longitude: "
						+ (double) Math.round(instantLocation
								.getLongitude() * 10000) / 10000);
				break;
			case 4:

				MyServerNano msn = new MyServerNano();
				MyServerQRInfo msqi = new MyServerQRInfo();
				try {
					msn.start();
					msqi.setPort(msn.getListeningPort());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				showCurrentQrCode(msqi, rootView);
				break;
			}
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
		
		 // check network connection
/*		public boolean isConnected() {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected())
				return true;
			else
				return false;
		}
*/
	}
}
