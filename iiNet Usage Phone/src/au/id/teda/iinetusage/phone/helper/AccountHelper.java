package au.id.teda.iinetusage.phone.helper;

import java.text.SimpleDateFormat;

public class AccountHelper extends AccountStatusHelper {
	
	// Static tag strings for logging information and debug
	private static final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	private static final String INFO_TAG = AccountHelper.class.getSimpleName();
	
	// Set data period format
	private final SimpleDateFormat myDataPeriodFormat = new SimpleDateFormat("MMM yyyy");

}
