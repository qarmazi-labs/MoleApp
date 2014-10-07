package com.qarmazilabs.mole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qarmazilabs.mole.PushDialog.PushDialogListener;
import com.qarmazilabs.mole.Tower.TowerEvent;

public class MainActivity extends ActionBarActivity implements PushDialogListener {
	Tower tower;
	public static final String MOLE_PREFERENCES = "MolePreferences";
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getSupportActionBar();

		setInfo();		
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
		switch(item.getItemId()){
			case R.id.action_save:
				save();
				return true;
			case R.id.action_push:
				push();
				return true;
			case R.id.action_about:
				openAbout();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/** Called for saving info to disk **/
	public void save(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			if(Environment.isExternalStorageEmulated()){
				Log.d("Mole_em","Indeed it is");
				File path = Environment.getExternalStoragePublicDirectory("MoleApp");
				File file = new File(path,"Data.txt");
				try{
					// Not available on mounting on Desktop
					path.mkdirs();
					if(!file.exists()){
						file.createNewFile();					
					}
					Writer writer = new FileWriter(file);
					writer.write("Boooaaaa");
					writer.close();
				}catch(IOException e){
					Log.d("Mole_io","wiiiiii");
					Log.w("ExternalStorage", "Error writing " + file, e);
				}
			}
		}else{
			// TODO Toast: Error saving...
		}
	}

	/** Called for pushing data to server **/
	public void push(){

		// Restore preferences
		SharedPreferences preferences = getSharedPreferences(MOLE_PREFERENCES,0);
		String pushURL = preferences.getString("pushURL","");		

//		Log.d("Mole_pre", "Hola"+pushURL+" d");

		Bundle args = new Bundle();
		args.putString("pushURL", pushURL);

		DialogFragment pushFragment = new PushDialog();
		pushFragment.setArguments(args);
		pushFragment.show(getSupportFragmentManager(), "PushDialogFragment");
	}
	
	/** From PushDialogListener **/
	@Override
	public void onDialogPositiveClick(DialogFragment dialog,String pushURL) {
		// Restore preferences
		SharedPreferences preferences = getSharedPreferences(MOLE_PREFERENCES,0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("pushURL", pushURL);
		editor.commit();
	};

	/** Called for opening about activity **/
	public void openAbout(){
		Intent intent = new Intent(this,AboutActivity.class);
		startActivity(intent);
	}
		
	/** Setting up views data **/
	protected void setInfo(){

		TelephonyManager telephonyManager  = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		tower = new Tower(telephonyManager, locationManager);
		
		TowerEvent towerEvent = new TowerEvent() {
			@Override
			public void onTowerEvent() {
				refreshActivity();
			}
		};
		
		tower.te = towerEvent;
	}

//	/** From TowerEventListenes **/
//	@Override
//	public void onTowerEvent() {
//		Log.d("Mole_TowerEvent","Aloha");
//	};

	
	
	/** Callback for refresh button **/
	public void refreshButton(View view){		
		refreshActivity();
	}

	/** Callback for refresh activity view **/
	public void refreshActivity(){
		TextView deviceID = (TextView) findViewById(R.id.deviceID);
		deviceID.setText(tower.deviceID);
		
		TextView countryCodeView = (TextView) findViewById(R.id.countryCode);
		countryCodeView.setText(tower.countryCode);

		TextView phoneTypeView = (TextView) findViewById(R.id.phoneType);
		phoneTypeView.setText(tower.phoneType);

		TextView networkTypeView = (TextView) findViewById(R.id.networkType);
		networkTypeView.setText(tower.networkType);

		TextView networkOperatorView = (TextView) findViewById(R.id.networkOperator);
		networkOperatorView.setText(tower.networkOperator);

		TextView simOperatorView = (TextView) findViewById(R.id.simOperator);
		simOperatorView.setText(tower.simOperator);

		TextView cellIdView = (TextView) findViewById(R.id.cID);
		cellIdView.setText(""+tower.cID);
		
		TextView longCellIdView = (TextView) findViewById(R.id.lCID);
		longCellIdView.setText(""+tower.lCID);
		
		TextView lACView = (TextView) findViewById(R.id.lAC);
		lACView.setText(""+tower.lAC);
		
		TextView pSCView = (TextView) findViewById(R.id.pSC);
		pSCView.setText(""+tower.pSC);
		
		TextView sSView = (TextView) findViewById(R.id.sS);
		sSView.setText(""+tower.signalStrength);

		TextView mNCView = (TextView) findViewById(R.id.mNC);
		mNCView.setText(tower.mNC);
		
		TextView mCCView = (TextView) findViewById(R.id.mCC);
		mCCView.setText(tower.mCC);

		TextView latitudeView = (TextView) findViewById(R.id.latitude);
		latitudeView.setText(""+tower.latitude);

		TextView longitudeView = (TextView) findViewById(R.id.longitude);
		longitudeView.setText(""+tower.longitude);
		
		TextView accuracyView = (TextView) findViewById(R.id.accuracy);
		accuracyView.setText(""+tower.accuracy);

	}
}
