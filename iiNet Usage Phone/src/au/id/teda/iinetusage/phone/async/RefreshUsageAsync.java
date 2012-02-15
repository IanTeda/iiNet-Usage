package au.id.teda.iinetusage.phone.async;

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
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.AppGlobals;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;
import au.id.teda.iinetusage.phone.helper.ConnectivityHelper;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;
import au.id.teda.iinetusage.phone.sax.DailyUsageSAXHandler;

/**
 *  RefreshUsageData.java
 *  Purpose: AsyncTask for refreshing user data on demand.
 * 
 *  @author Ian Teda
 *  @version Alpha
 *  
 */

public class RefreshUsageAsync extends AsyncTask<Void, Void, Void> {
	
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = RefreshUsageAsync.class.getSimpleName();
	
    private Context myApplicationContext = AppGlobals.getAppContext();
    private Context myActivityContext;
	
	// Create instance of AccountHelper
	AccountHelper myAccount = new AccountHelper();
	
	// Create instance of PreferenceHelper
	PreferenceHelper mySettings = new PreferenceHelper();
	
	// Create instance of ConnectivityHelper
	ConnectivityHelper myConnection = new ConnectivityHelper();

	// Progress dialog object
    private ProgressDialog myProgressDialog;
    private Handler myHandler;
	
    /**
     * Constructor for class. Pass activity context and return handler for update
     * @param context
     * @param myHandler
     */
	public RefreshUsageAsync (Context context, Handler handler) {
		this.myActivityContext = context;
		myHandler = handler;
	}

	/**
	 * Second constructor for auto update with dialog set to false
	 * 
	 * @param context
	 * @param myHandler
	 * @param hideDialog
	 */
	public RefreshUsageAsync(Context context, Handler handler,
			boolean hideDialog) {
	
		this.myActivityContext = context;
		myHandler = handler;
	}

	/**
	 * Before executing background AsynTask
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		if (!mySettings.isUsernamePasswordSet()){
			// Username password not set
			showToastMsg(myActivityContext.getString(R.string.no_username_password));
		}
		// Check if we have connectivity
		else if (!myConnection.isConnected()){
			// No connectivity
			showToastMsg(myActivityContext.getString(R.string.no_internet));
		}
		// Username/Password set, Internet connected so check if we are displaying a progress dialog
		else if (mySettings.showRefreshDialog() == true){
			myProgressDialog = ProgressDialog.show(myActivityContext,
					myApplicationContext.getString(R.string.refresh_progress_dialog_title),
					myApplicationContext.getString(R.string.refresh_progress_dialog_description),
					true);
		}
	}

	/**
	 * Method for displaying toast messages
	 * @param String value of message to be displayed
	 */
	private void showToastMsg(String msg) {
		Toast refreshToast=Toast.makeText(myActivityContext, msg, 2000);
		refreshToast.setGravity(Gravity.CENTER, 0, 250);
		refreshToast.show();
	}
	
	/**
	 * AsyncTask: For retrieving XML feed of data usage 
	 * check if username/password exists if they do update data
	 */
	@Override
	protected Void doInBackground(Void... params) {
		
		// Check username & password exists
		if (mySettings.isUsernamePasswordSet() && myConnection.isConnected()){
	        try {
	        	// Get URL
	        	URL url = new URL(buildXMLPath());
	        	//Log.d(DEBUG_TAG, "URL: " + url);
	        	
	        	// Load xml from our development xml file
				//InputSource is = new InputSource(myApplicationContext.getResources().openRawResource(R.raw.may2011));
	        	
	        	 // Create a SAXParserFactory so we can
	        	SAXParserFactory spf = SAXParserFactory.newInstance();
	        	
	        	// Create a SAXParser so we can
	        	SAXParser sp = spf.newSAXParser();
	        	
	        	// Create a XMLReader
	        	XMLReader xr = sp.getXMLReader();
	        	
	        	// Create a new ContentHandler and apply it to the XML-Reader
	        	DailyUsageSAXHandler myAccountInfoSAXHandler = new DailyUsageSAXHandler();
	        	xr.setContentHandler(myAccountInfoSAXHandler);
	        	
	        	// Parse the xml-data from our development file
	        	//xr.parse(new InputSource(is.getByteStream()));
	        	
	        	// Else parse the xml-data from our URL.
	        	xr.parse(new InputSource(url.openStream()));
	        	
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
		} else {
			// No Internet available or username/password not set
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		if (myProgressDialog != null){
			myProgressDialog.dismiss();
		}
		
		myHandler.sendEmptyMessage(0);
		
		// Check if username/password is set and no Internet is connected
		if (mySettings.isUsernamePasswordSet() && myConnection.isConnected()){
			// It is so we must have updated text
			showToastMsg(myActivityContext.getString(R.string.usage_data_updated));	
		}

		super.onPostExecute(result);
	}
	
	// Build URL to fetch XML
	private String buildXMLPath(){
		String pathString = null;
		
		String myUsername = mySettings.getUsername();
		String myPassword = mySettings.getPassword();
		
		pathString = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?" +
					"username=" + myUsername + 
					"&action=login" +
					"&password=" + myPassword;
		
		return pathString;
	}
}
