package au.id.teda.volumeusage.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import au.id.teda.volumeusage.MyApp;

public class NotificationHelper {
	
	private Context context;

	public NotificationHelper(Context context) {
		super();
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = NotificationHelper.class.getSimpleName();
	
	public void usageNotification(CharSequence tickerText, CharSequence notificationTitle, CharSequence notificationText, int alertID){
		Log.i(INFO_TAG, "usageNotification()");
		
		// Setup notification
		int icon = android.R.drawable.stat_notify_error;	// icon from resources
		//CharSequence tickerText = "Hello";              	// ticker-text
		long when = System.currentTimeMillis();         	// notification time
		Context appContext = MyApp.getAppContext();      	// application Context
		//CharSequence contentTitle = "My notification";  	// expanded message title
		//CharSequence contentText = "Hello World!";		   	// expanded message text

		// Load intents for notification
		Intent notificationIntent = new Intent(context, NotificationHelper.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		// the next two lines initialize the Notification, using the configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(appContext, notificationTitle, notificationText, contentIntent);
		
		// Get a reference to the NotificationManager
		NotificationManager notificationManager = (NotificationManager) 
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// Notify user
		notificationManager.notify(alertID, notification);
	}

}
