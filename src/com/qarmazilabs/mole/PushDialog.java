package com.qarmazilabs.mole;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class PushDialog extends DialogFragment {
	
	public interface PushDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog,String pushURL);
	}
	
	PushDialogListener pListener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view  = inflater.inflate(R.layout.push_dialog, null);
		Log.d("Mole_di","Hola "+getArguments().getString("pushURL",""));

		EditText editText = (EditText) view.findViewById(R.id.pushURL);
		editText.setText(getArguments().getString("pushURL"));

		builder.
			setView(view).
			setPositiveButton(R.string.pushMessagePositive_string, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					EditText editText = (EditText) getDialog().findViewById(R.id.pushURL);
					pListener.onDialogPositiveClick(PushDialog.this,editText.getText().toString());
				}
			});
		return builder.create();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try {
			pListener = (PushDialogListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString()+" must implement PushDialogListener");
		}
	}

}
