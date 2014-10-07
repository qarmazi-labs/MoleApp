package com.qarmazilabs.mole;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class Tower {
	protected String deviceID = "";
	protected String phoneType = "";
	protected String networkType = "";
	protected String networkOperator = "";
	protected String simOperator = "";
	protected int cID = 0;
	protected int lCID = 0;
	protected int lAC = 0;
	protected int pSC = 0;
	protected String signalStrength = "";
	protected String mNC = "";
	protected String mCC = "";
	protected String countryCode = "";
	protected double latitude = 0;
	protected double longitude = 0;
	protected double altitude = 0;
	protected float accuracy = 0;
	
	public interface TowerEvent {
		public void onTowerEvent();
	}
	
	protected TowerEvent te;
	
	public Tower(TelephonyManager tm, LocationManager lm) {
		super();
	
		PhoneStateListener phoneStateListener = new PhoneStateListener(){

			@Override
			public void onSignalStrengthsChanged(SignalStrength sStrength){
				super.onSignalStrengthsChanged(sStrength);
				int sS = sStrength.getGsmSignalStrength();
				signalStrength =sS+" ASU -"+(2 * sS - 113)+" dBm";
			}

			@Override
			public void onCellLocationChanged(CellLocation location){
				super.onCellLocationChanged(location);
				Log.d("Mole_CLC","Blap");
			}
			
		};
		
		tm.listen(phoneStateListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		tm.listen(phoneStateListener,PhoneStateListener.LISTEN_CELL_LOCATION);

        LocationListener locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				if(location.hasAltitude())
					altitude = location.getAltitude();
				if(location.hasAccuracy())
					accuracy = location.getAccuracy();
				
				te.onTowerEvent();
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
        };
        
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

		// Gets the type of radio used to transmit voice calls
		// TODO Consider the other options
		switch(tm.getPhoneType()){
			case TelephonyManager.PHONE_TYPE_GSM:
				this.phoneType = "GSM";
				break;
			case TelephonyManager.PHONE_TYPE_CDMA:
				this.phoneType = "CDMA";
				break;
			default:
				this.phoneType = ""+tm.getPhoneType();
		}
		
		// Gets the type of network used for the data connection
		// TODO Consider the other options
		// TODO Add network speed
		switch(tm.getNetworkType()){
			case TelephonyManager.NETWORK_TYPE_UMTS:
				this.networkType = "UMTS";
				break;
			case TelephonyManager.NETWORK_TYPE_LTE:
				this.networkType = "LTE";
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				this.networkType = "GPRS";
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				this.networkType = "EDGE";
				break;
			default:
				this.networkType = ""+tm.getNetworkType();
		}
		
		GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();

		this.deviceID = tm.getDeviceId();
		this.countryCode = tm.getSimCountryIso();
		this.networkOperator = tm.getNetworkOperatorName();
		this.simOperator = tm.getSimOperatorName();
		this.lCID = cellLocation.getCid();
		this.cID = calculateCID(this.lCID);
		this.lAC = cellLocation.getLac();
		this.pSC = cellLocation.getPsc();
		this.mNC = tm.getNetworkOperator().substring(3, 6);
		this.mCC = tm.getNetworkOperator().substring(0, 3);
		
	}
	
	public String getDeviceID() {
		return deviceID;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public String getNetworkType() {
		return networkType;
	}
	public String getNetworkOperator() {
		return networkOperator;
	}
	public String getSimOperator() {
		return simOperator;
	}
	public int getCID() {
		return cID;
	}
	public int getLCID() {
		return lCID;
	}
	public int getLAC() {
		return lAC;
	}
	public int getPSC() {
		return pSC;
	}
	public String getMNC() {
		return mNC;
	}
	public String getMCC() {
		return mCC;
	}
	public String getCountryCode() {
		return countryCode;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public int calculateCID(int lCID) {
		return 0xffff & lCID; // Could be used (short) lCID
	}
}
