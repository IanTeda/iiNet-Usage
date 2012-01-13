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
import android.util.Log;
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
	private static final String INFO_TAG = RefreshUsageAsync.class.getSimpleName();
	
    private Context myApplicationContext = AppGlobals.getAppContext();
    private Context myActivityContext;
	
	// Create instance of shared preferences based on app context
	AccountHelper myAccount = new AccountHelper();
	PreferenceHelper mySettings = new PreferenceHelper();

    private ProgressDialog progressDialog;
    private Handler handler;
	
    /**
     * Constructor for class. Pass activity context and return handler for update
     * @param context
     * @param myHandler
     */
	public RefreshUsageAsync (Context context, Handler myHandler) {
		//Log.i(INFO_TAG, "Start constructor");
		
		this.myActivityContext = context;
		handler = myHandler;
	}

	/**
	 * Second constructor for auto update with dialog set to false
	 * 
	 * @param context
	 * @param myHandler
	 * @param hideDialog
	 */
	public RefreshUsageAsync(Context context, Handler myHandler,
			boolean hideDialog) {
		//Log.i(INFO_TAG, "Start constructor");
		
		this.myActivityContext = context;
		handler = myHandler;
	}

	/**
	 * Before executing background asyntask display refresh dialog
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.i(INFO_TAG, "onPreExecute()");
		
		//Log.d(DEBUG_TAG, "RefreshUsageData > Onload update: " + updateRefresh);
		
		// Show dialog if preference set true and it isn't an auto update at load
		if (mySettings.showRefreshDialog() == true){
			
			progressDialog = ProgressDialog.show(myActivityContext,
					myApplicationContext.getString(R.string.refresh_progress_dialog_title),
					myApplicationContext.getString(R.string.refresh_progress_dialog_description),
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
		
		// Check if connectivity is true. If so try to parse xml
		ConnectivityHelper myConnection = new ConnectivityHelper();
		
		// Check username & password exists else toast alert
		
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
			// Load dialog for going to settings
			//DialogHelper dialogHelper = new DialogHelper(context);
			//dialogHelper.dialogUsernamePassword();
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
		if (progressDialog != null){
			progressDialog.dismiss();
		}
		
		handler.sendEmptyMessage(0);
		
		Toast refreshToast=Toast.makeText(myActivityContext, "Usage Updated", 2000);
		refreshToast.setGravity(Gravity.CENTER, 0, 250); // TODO: Not sure if this is the right way to center toast at bottom
		refreshToast.show();	

		super.onPostExecute(result);
	}
	
	// Build URL to fetch XML
	public String buildXMLPath(){
		//Log.i(INFO_TAG, "buildXMLPath()");
		String pathString = null;
		
		String myUsername = mySettings.getUsername();
		String myPassword = mySettings.getPassword();
		
		pathString = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?" +
					"username=" + myUsername + 
					"&action=login" +
					"&password=" + myPassword;
		
		//Log.d(DEBUG_TAG, "buildXMLPath() > pathString: " + pathString);
		return pathString;
	}
}
