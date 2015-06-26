package com.server.activities;

import java.io.IOException;

import com.server.gti785_lab2.R;
import com.server.servermanager.MyServerNano;
import com.server.servermanager.MyServerQRInfo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MyServerQRInfo msqi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyServerNano msn = new MyServerNano();
		
		try {
			msn.start();
			msqi = new MyServerQRInfo(msn.getListeningPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		showCurrentQrCode();
		Toast.makeText(getApplicationContext(),msqi.getAddress(),10);
	}
	
	private void showCurrentQrCode(){
		Bitmap bitmap = msqi.generateQRCode();
		ImageView qrcode = (ImageView) findViewById(R.id.imageView1);
		qrcode.setImageBitmap(bitmap);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(getApplicationContext(), "You have pressed on settings itms", 5);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
