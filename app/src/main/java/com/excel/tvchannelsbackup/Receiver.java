package com.excel.tvchannelsbackup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(null, "Receiver called");
		// Toast.makeText( context, "Receiver called", Toast.LENGTH_LONG ).show();
		
		final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		/*
        // Device is started and wifi is also available, then only start Restore Service
		if ( intent.getAction().equals( Intent.ACTION_BOOT_COMPLETED ) && wifi.isAvailable() ) {
			Intent inn = new Intent();
			inn.setClass( context, RestoreService.class );
			context.startService( inn );
			// Toast.makeText( context, "Start On Boot Receiver Called", Toast.LENGTH_LONG ).show();
			Log.i( null, "Start On Boot Receiver Called" );
		}
		// When WiFi is connected, this part will be called, so we start the service
		else if ( wifi.isAvailable() ){
			// we have to call this RestoreService class only once [So, make shared preference and store
			// the bit --> executed ]
			Intent inn = new Intent();
			inn.setClass( context, RestoreService.class );
			context.startService( inn );
			// Toast.makeText( context, "WiFi is available now", Toast.LENGTH_LONG ).show();
			Log.i( null, "WiFi is available now" );
		}
	    */
	}

}
