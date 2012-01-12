package au.id.teda.iinetusage.phone.helper;

import android.content.Context;

/**
 * Method for getting and setting application preferences
 * @author Ian
 *
 */
public class PreferenceHelper extends ConnectivityHelper {
	
	private Context myActivityContext;

	public PreferenceHelper(Context activityContext) {
		super(activityContext);
		
		// Set activity context
		myActivityContext = activityContext;
	}
	

}
