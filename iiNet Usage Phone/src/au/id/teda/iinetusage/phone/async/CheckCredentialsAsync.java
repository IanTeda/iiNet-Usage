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
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.ConnectivityHelper;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;
import au.id.teda.iinetusage.phone.sax.CheckUserPassSAXHandler;

/**
 * Async class for checking user credentials
 * 
 * @author Ian
 */

// TODO: Can this be incorporated with the refresh async task?
public class CheckCredentialsAsync extends AsyncTask<Void, Void, Void> {

	// Static strings used for debugging
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = CheckCredentialsAsync.class.getSimpleName();

	// Set shared preference helper object
	private PreferenceHelper mySettings = new PreferenceHelper();

	// Class objects
	private Context myActivityContext;
	private Handler myHandler;
	private ProgressDialog myProgressDialog;
	private URL myUrl;
	
	/**
	 *  Constructor for class. Pass activity context and return handler for
	 *  update
	 * 
	 * @param activityContext
	 * @param handler
	 * @param url
	 */
	public CheckCredentialsAsync(Context activityContext, Handler handler, URL url) {
		//Log.i(INFO_TAG, "Start constructor");

		// Set context for class
		this.myActivityContext = activityContext;

		// Set handler object
		myHandler = handler;

		// Set URL to check
		myUrl = url;

	}

	/**
	 * What code should I process before execution
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		showProgressDialog();
	}

	private void showProgressDialog() {
		// Display progress dialog
		myProgressDialog = ProgressDialog.show(myActivityContext,
				myActivityContext.getString(R.string.user_pass_checking_title),
				myActivityContext.getString(R.string.user_pass_checking_description),
				true);
	}

	/**
	 * Execution code for async task
	 * 
	 * @param params
	 * @return
	 */
	@Override
	protected Void doInBackground(Void... params) {

		// Log.d(DEBUG_TAG, "Connection is: " + isConnected());

		// Check if connectivity is true. If so try to parse xml
		ConnectivityHelper myConnection = new ConnectivityHelper();
		if (myConnection.isConnected()) {
			try {
				// Log.d(DEBUG_TAG, "checkCredentials() > URLl: " + myUrl);

				// Load xml from our developement xml file
				//InputSource is = new InputSource(AppGlobals.getAppContext().getResources().openRawResource(R.raw.may2011));

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
				// xr.parse(new InputSource(is.getByteStream()));

				// Parse the xml-data from our URL.
				xr.parse(new InputSource(myUrl.openStream()));

				// Log.d(DEBUG_TAG,
				// "checkCredentials() > Checking username / password > try");
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
			Log.d(DEBUG_TAG, "doInBackground() > No connection");
		}

		return null;
	}

	/**
	 * Code to execute during async task. Typcially return to activity handler
	 * 
	 * @param values
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		// Implemented method. Nothing to do here
		super.onProgressUpdate(values);
	}

	/**
	 * Code to execute post async task running
	 * 
	 * @param result
	 */
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		// Close progress dialog
		if (myProgressDialog != null) {
			myProgressDialog.dismiss();
		}

		// Check status and display toast messages
		checkStatus();

		// Send empty message to activity executing task (typical reload of
		// view)
		myHandler.sendEmptyMessage(0);
	}

	/**
	 * Retrieve status of check from preferences and display toast dialog
	 * accordingly
	 */
	private void checkStatus() {
		if (!mySettings.isPassed()) {
			popup(mySettings.getErrorTxt());
		} else {
			popup("Looking good");
		}
	}

	/**
	 * Simple method for displaying toast messages. Saves code duplication.
	 * 
	 * @param msg
	 */
	public void popup(String msg) {
		Toast.makeText(myActivityContext, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCancelled() {
		// Implemented method. Nothing to do here.
		super.onCancelled();
	}

}
