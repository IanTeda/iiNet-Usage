package au.id.teda.volumeusage.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.SharedPreferences;
import android.util.Log;
import au.id.teda.volumeusage.MyApp;
import au.id.teda.volumeusage.activity.UserPassActivity;

public class CheckUserPassSAXHandler extends DefaultHandler {
	
	/**
	 *  Static tag strings for logging information and debug
	 */
	// Debug tag for LogCat
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = CheckUserPassSAXHandler.class.getSimpleName();
	
	// Set tag variables
	public static final String II_FEED = "ii_feed"; // Man xml parent tag
	public static final String ERROR = "error"; // Error tag
	
	// Set inTag variables
    private boolean inFeed = false;
    private boolean inError = false;
    
    private String errorText = null;
    
	/**
	 * This method is called when the parser reaches an xml start tag
	 */
	@Override
	public void startElement(String uri, String myTag, String qName,
			Attributes myAtt) throws SAXException {
		super.startElement(uri, myTag, qName, myAtt); //TODO: Do I need this super?
		
		Log.d(DEBUG_TAG, "startElement is: " + myTag.trim());
		if (myTag.trim().equalsIgnoreCase(II_FEED)){
			inFeed = true;
		} else if (myTag.trim().equalsIgnoreCase(ERROR)){
			inError = true;
		}
	}

	/**
	 * This method is called when the parser reaches an xml end tag
	 */
	@Override
	public void endElement(String uri, String myTag, String qName)
			throws SAXException {
		super.endElement(uri, myTag, qName);
		
		Log.d(DEBUG_TAG, "endElement is: " + myTag.trim());
		if (myTag.trim().equalsIgnoreCase(II_FEED)){
			inFeed = false;
		} else if (myTag.trim().equalsIgnoreCase(ERROR)){
			inError = false;
		}
		
		//UserPassActivity myError = new UserPassActivity();
		// Check to see if we have all the data required for a dailyDB entry
		if (errorText != null){
			Log.d(DEBUG_TAG, "Error is: " + errorText);
			
			SharedPreferences settings = getSharedPreferences("au.id.teda.prefs.Preferences", 0);
			Editor editor = sharedPreferences.edit();

			editor.putString("your_optionname", "newValue");
			// Save
			editor.commit();
			
			 SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
			
			//myError.
			//credentialChk = false;
			// Clear objects for next pass over xml
			errorText = null;
		} else {
			//myError.credentialChk = true;
			//Log.d(DEBUG_TAG, "credentialChk is: true");
			MyApp globalVariables = new MyApp();
			globalVariables.setIsChecked(true);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		
		// Set string value for xml tag currently in
		String chars = (new String(ch).substring(start, start + length));
		
		// if we are in the feed tag and in an error tag set error string
		if (inFeed && inError){
			errorText = chars;
		}
		
		
	}



}
