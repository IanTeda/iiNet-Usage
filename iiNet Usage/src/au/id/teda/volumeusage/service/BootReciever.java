package au.id.teda.volumeusage.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author Ian
 * 
 * Used to start the service at boot time
 *
 */

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(DataCollectionService.class.getName());
        context.startService(serviceIntent); 

	}

}
