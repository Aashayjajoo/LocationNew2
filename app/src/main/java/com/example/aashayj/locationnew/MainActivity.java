package com.example.aashayj.locationnew;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private TextView addressTextView;
	private Button addressbutton;
	private Button gpsButton;
	AppLocationService appLocationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addressTextView = (TextView) findViewById(R.id.addressText);
		appLocationService = new AppLocationService(
				MainActivity.this);
	}

	public void gpsButton(View view) {
		Location gpsLocation = appLocationService
				.getLocation(LocationManager.GPS_PROVIDER);
		if (gpsLocation != null) {
			double latitude = gpsLocation.getLatitude();
			double longitude = gpsLocation.getLongitude();
			String result = "Latitude: " + gpsLocation.getLatitude() +
					" Longitude: " + gpsLocation.getLongitude();
			addressTextView.setText(result);
		} else {
			showSettingsAlert();
		}
	}

	public void addressButton(View view) {
		Location location = appLocationService
				.getLocation(LocationManager.GPS_PROVIDER);

		//you can hard-code the lat & long if you have issues with getting it
		//remove the below if-condition and use the following couple of lines
		//double latitude = 37.422005;
		//double longitude = -122.084095

		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			LocationAddress locationAddress = new LocationAddress();
			locationAddress.getAddressFromLocation(latitude, longitude,
					getApplicationContext(), new GeocoderHandler());
		} else {
			showSettingsAlert();
		}

	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MainActivity.this);
		alertDialog.setTitle("SETTINGS");
		alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						MainActivity.this.startActivity(intent);
					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog.show();
	}

	private class GeocoderHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			String locationAddress;
			switch (message.what) {
				case 1:
					Bundle bundle = message.getData();
					locationAddress = bundle.getString("address");
					break;
				default:
					locationAddress = null;
			}
			Log.v("aaazsgszh MainActivity" + " ", locationAddress);
			addressTextView.setText(locationAddress);
		}
	}

}
