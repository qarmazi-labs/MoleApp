package com.qarmazilabs.mole;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

public class MolePhoneStateListener extends PhoneStateListener {

	public MolePhoneStateListener() {
		super();
	}

	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength){
		super.onSignalStrengthsChanged(signalStrength);
		int sS = signalStrength.getGsmSignalStrength();
		Log.d("Mole_PSL","Blip: "+sS+" "+(2 * sS - 113));
	}

	@Override
	public void onCellLocationChanged(CellLocation location){
		super.onCellLocationChanged(location);
		Log.d("Mole_CLC","Blap");
	}
}
