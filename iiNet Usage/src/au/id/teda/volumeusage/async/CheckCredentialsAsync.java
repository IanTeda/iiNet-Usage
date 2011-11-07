package au.id.teda.volumeusage.async;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.R;
import au.id.teda.volumeusage.sax.CheckUserPassSAXHandler;

public class CheckCredentialsAsync extends AsyncTask<Void, Void, Void> {
	
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = CheckCredentialsAsync.class.getSimpleName();
	
	private SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MyApp.getAppContext());
	
	private Context context;
	private Handler myHandler;
	private ProgressDialog myProgressDialog;
	
	private URL myUrl;
	
	private static final String ISPASSED = "isPassedChk";
	private static final String ERRORTXT = "errorTxt";
	private static final String WIFI_ONLY = "wifi_only";
	
    /**
     * Constructor for class. Pass activity context and return handler for update
     * @param context
     * @param myHandler
     */
	public CheckCredentialsAsync (Context context, Handler handler, URL url) {
		Log.i(INFO_TAG, "Start constructor");
		
		this.context = context;
		myHandler = handler;
		myUrl = url;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		myProgressDialog = ProgressDialog.show(context,
				MyApp.getAppContext().getString(R.string.refresh_progress_dialog_title),
				MyApp.getAppContext().getString(R.string.refresh_progress_dialog_description),
				true);
	}
	
	@Override
	protected Void doInBackground(Void... params) {

		Log.d(DEBUG_TAG, "Connection is: " + isConnected());
		
		// Check if connectivity is true. If so try to parse xml
		if (isConnected()){
			try {
				Log.d(DEBUG_TAG, "checkCredentials() > URL: " + myUrl);
				
				// Load xml from our developement xml file
				InputSource is = new InputSource(MyApp.getAppContext().getResources().openRawResource(R.raw.auth_fail));
				
				// Create a SAXParserFactory so we can
				SAXParserFactory spf = SAXParserFactory.newInstance();
				
				// Create a SAXParser so we can
				SAXParser sp = spf.newSAXParser();
				
				// Create a XMLReader
				XMLReader xr = sp.getXMLReader();
				
				// Create a new ContentHandler and apply it to the XML-Reader
				CheckUserPassSAXHandler myUserPassSAXHandler = new CheckUserPassSAXHandler();
				xr.setContentHandler(myUserPassSAXHandler);
				
				// Parse the xml-data from our development file
				xr.parse(new InputSource(is.getByteStream()));
				
				// Parse the xml-data from our URL.
				//xr.parse(new InputSource(myUrl.openStream()));
				
				//Log.d(DEBUG_TAG, "checkCredentials() > Checking username / password > try");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		myProgressDialog.dismiss();
		
		checkStatus();
		
		myHandler.sendEmptyMessage(0);
	}

	private void checkStatus() {
		if (!settings.getBoolean("isPassedChk", false)){
			popup(settings.getString(ERRORTXT, "Error"));
		} else {
			popup("Looking good");
		}
	}
	
	public void popup(String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}
	
	/**
	 * See if we are able to pull the XML
	 * @return
	 */
	public boolean isConnected(){
		
		// Set connectivity manager object
		ConnectivityManager myConMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Get connection information
		NetworkInfo info = myConMan.getActiveNetworkInfo();
		
		// Skip if no connection, or background data disabled
		if (info == null || !myConMan.getBackgroundDataSetting()) { // TODO: Check for roaming settings
			return false;
		} else {
			 // Confirm 3G connectivity
		    Boolean is3g = myConMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		     // Confirm wifi connectivity
		    Boolean isWifi = myConMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
			
		    Log.d(DEBUG_TAG, "is3g: " + is3g);
		    Log.d(DEBUG_TAG, "isWifi: " + isWifi);
		    Log.d(DEBUG_TAG, "Wifi Only: " + settings.getBoolean(WIFI_ONLY, false));
		    
		    if (is3g && !settings.getBoolean(WIFI_ONLY, false)) {
		    	Log.d(DEBUG_TAG, "3G and non wifi connectin allowed");
		    	return true;
		    	
		    } else if(isWifi && settings.getBoolean(WIFI_ONLY, false)) {
		    	Log.d(DEBUG_TAG, "3G and non wifi connectin allowed");
		    	return true;
		    	
		    } else {
		    	
		    	return false;
		    }
		}
		
	}

}
