package au.id.teda.volumeusage.async;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.helper.AccountHelper;
import au.id.teda.volumeusage.notification.DialogHelper;
import au.id.teda.volumeusage.sax.DailyUsageSAXHandler;
import au.id.teda.volumeusage.service.ServiceHelper;

/**
 *  RefreshUsageData.java
 *  Purpose: AsyncTask for refreshing user data on demand.
 * 
 *  @author Ian Teda
 *  @version Alpha
 *  
 */

//TODO: Rename to async
public class RefreshUsageData extends AsyncTask<Void, Void, Void> {
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = RefreshUsageData.class.getSimpleName();
	
    private ProgressDialog progressDialog;
    private Context context;
    private Handler handler;
    private boolean showRefreshDialog;
    private boolean updateRefresh = false;
	
    /**
     * Constructor for class. Pass activity context and return handler for update
     * @param context
     * @param myHandler
     */
	public RefreshUsageData (Context context, Handler myHandler) {
		Log.i(INFO_TAG, "Start constructor");
		
		this.context = context;
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		showRefreshDialog = settings.getBoolean("hide_refresh_dialog", false);
		handler = myHandler;
	}

	/**
	 * Second constructor for auto update with dialog set to false
	 * 
	 * @param context
	 * @param myHandler
	 * @param hideDialog
	 */
	public RefreshUsageData(Context context, Handler myHandler,
			boolean hideDialog) {
		Log.i(INFO_TAG, "Start constructor");
		
		this.context = context;
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		showRefreshDialog = settings.getBoolean("hide_refresh_dialog", false);
		handler = myHandler;
		updateRefresh = hideDialog;
	}

	/**
	 * Before executing background asyntask display refresh dialog
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.i(INFO_TAG, "onPreExecute()");
		
		Log.d(DEBUG_TAG, "RefreshUsageData > Onload update: " + updateRefresh);
		
		// Show dialog if preference set true and it isn't an auto update at load
		if (showRefreshDialog == false && updateRefresh == false){
			
			progressDialog = ProgressDialog.show(context,
					MyApp.getAppContext().getString(R.string.refresh_progress_dialog_title),
					MyApp.getAppContext().getString(R.string.refresh_progress_dialog_description),
					true);
		}
	}
	
	/**
	 * AsyncTask: For retrieving XML feed of data usage 
	 * check if username/password exists if they do update data
	 */
	@Override
	protected Void doInBackground(Void... params) {
		Log.i(INFO_TAG, "doInBackground()");
		
		// Check username & password exists else toast alert
		AccountHelper accountHelper = new AccountHelper(context);
		if (accountHelper.checkUsernamePassword()){
	        try {
	        	ServiceHelper serviceHelper = new ServiceHelper(context);
	        	URL url = new URL(serviceHelper.buildXMLPath());
	        	Log.d(DEBUG_TAG, "URL: " + url);
	        	//URL url = new URL("http://www.anddev.org/images/tut/basic/parsingxml/example.xml"); // Create a URL we want to load some xml-data from.
	        	//InputSource is = new InputSource(MyApp.getAppContext().getResources().openRawResource(R.raw.adsl2  )); // Our developement xml file
	        	SAXParserFactory spf = SAXParserFactory.newInstance(); // Create a SAXParserFactory so we can
	        	SAXParser sp = spf.newSAXParser(); // Create a SAXParser so we can
	        	XMLReader xr = sp.getXMLReader(); // Create a XMLReader
	        	DailyUsageSAXHandler myAccountInfoSAXHandler = new DailyUsageSAXHandler(); // Create a new ContentHandler and apply it to the XML-Reader
	        	xr.setContentHandler(myAccountInfoSAXHandler);
	        	//xr.parse(new InputSource(is.getByteStream())); // Parse the xml-data from our development file
	        	xr.parse(new InputSource(url.openStream())); // Parse the xml-data from our URL.
	        } catch (Exception e) {
	        	// Display any Error to catLog
	        	Log.d("iiNet Usage", "XML Querry error: " + e.getMessage());
	        }
		} else {
			// Load dialog for going to settings
			DialogHelper dialogHelper = new DialogHelper(context);
			dialogHelper.dialogUsernamePassword();
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.i(INFO_TAG, "onPostExecute()");
		if (showRefreshDialog == false && updateRefresh == false){
			progressDialog.dismiss();
		}
		handler.sendEmptyMessage(0);
		
		Toast refreshToast=Toast.makeText(context, "Usage Updated", 2000);
		refreshToast.setGravity(Gravity.CENTER, 0, 250); // TODO: Not sure if this is the right way to center toast at bottom
		refreshToast.show();	

		super.onPostExecute(result);
	}
}
