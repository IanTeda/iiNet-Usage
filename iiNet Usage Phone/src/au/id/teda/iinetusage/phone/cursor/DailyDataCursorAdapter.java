package au.id.teda.iinetusage.phone.cursor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.AccountHelper;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class DailyDataCursorAdapter extends CursorAdapter {

	// TODO: Update layout for better scrolling
	// TODO: Add onclick hourly data
	
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = DailyDataCursorAdapter.class.getSimpleName();
	
	// Set objects for TextViews
	private LinearLayout myRow;
	private TextView myDate;
	private TextView myPeak;
	private TextView myOffpeak;
	private TextView myUpload;
	private TextView myFreezone;
	private TextView myTotal;
	private TextView myAccum;
	
	// Load object for preferences
	private final PreferenceHelper mySettings;
	
	// Account
	private final AccountHelper myAccount;
	
	// Context
	private final Context myActivityContext;
	
    private final LayoutInflater mInflater;
    boolean showDecimal = false;
    boolean showFuture = false;
    
    // Integer used to determine row number and background color
    private int rowNum;
    private int oldRowNum;
    
    // Integer for accumulative total
    private long accumLong = 0;
    
    // Multidimensional array
    private long[] accumArray;
	
	public DailyDataCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, true);
		mInflater = LayoutInflater.from(context);
		
		// Set preferences object
		mySettings = new PreferenceHelper();
		
		// Set context
		myActivityContext = context;
		
		// Set myAccount reference
		myAccount = new AccountHelper();
		
		// Intialise array
		int days = (int) (myAccount.getCurrentDaysToGo() + myAccount.getCurrentDaysSoFar());
		accumArray = new long[days];
	}
    
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		// Increase row number by one
		rowNum = cursor.getPosition();
		
		// Set reference to TextViews
		myDate = (TextView) view.findViewById(R.id.daily_data_row_date);
		myPeak = (TextView) view.findViewById(R.id.daily_data_row_peak);
		myOffpeak = (TextView) view.findViewById(R.id.daily_data_row_offpeak);
		myUpload = (TextView) view.findViewById(R.id.daily_data_row_upload);
		myTotal =(TextView) view.findViewById(R.id.daily_data_row_total);
		myRow = (LinearLayout) view.findViewById(R.id.daily_data_row);
		
		
		// Get long values from database cursor
		// TODO: Change DailyDataDBAdapter to dailyDataDBHelper???
		long dateLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.DATE));
		long peakLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.PEAK));
		long offpeakLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.OFFPEAK));
		long uploadLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.UPLOAD));
		long totalLong = (peakLong + offpeakLong);
		
		// Set TextView string values
		myDate.setText(String.valueOf(getDayOfMonth(dateLong)));
		myPeak.setText(getUsageString(peakLong));
		myOffpeak.setText(getUsageString(offpeakLong));
		myUpload.setText(getUsageString(uploadLong));
		myTotal.setText(getUsageString(totalLong));
		
		setRowBackground();
		
		if (!isPortrait()){
		
			// Set reference to TextViews
			myFreezone = (TextView) view.findViewById(R.id.daily_data_row_freezone);
			myAccum = (TextView) view.findViewById(R.id.daily_data_row_accum);
			
			// Get long values from database cursor
			long freezoneLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.FREEZONE));
			
			// Need to use an array to store accumulative values because views are destroyed
			// Check to see if rows are increasing
			if (oldRowNum < rowNum || rowNum == 0){
				// Check to see if row number in array does not have a value
				if (accumArray[rowNum] == 0){
					// Add new total to accumulative total
					accumLong = accumLong + totalLong;
					// Add accumulative total to array based on position
					accumArray[rowNum] = accumLong;
				}
			}
			
			// Set TextView string values
			myFreezone.setText(getUsageString(freezoneLong));
			myAccum.setText(getUsageString(accumArray[rowNum]));
			
			// Change old row number to new
			oldRowNum = rowNum;
		}
	}

	/**
	 * Set row background based on row number
	 */
	private void setRowBackground() {
		// Set row background color based on row number
		if (isRowEven(rowNum)){
			// Looks like it is an even row number so set background color
			myRow.setBackgroundResource(R.color.application_background_color);
		} 
		// Else it must be an odd number
		else {
			// Looks like it is an odd row number so set alternate background color
			myRow.setBackgroundResource(R.color.application_background_alt_color);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Inflate the listview with the changes above
		View view = mInflater.inflate(R.layout.daily_data_row, parent, false);
		return view;
	}
	
	private boolean isRowEven(int row){
		// Check if number is even
		if (row % 2 == 0) {
			// Looks even to me
			return true;
		}
		// Else it must be odd
		else {
			//Looks odd
			return false;
		}
	}
	
	private int getDayOfMonth(long milliSec){
		// Create instance of calendar
		Calendar date = Calendar.getInstance();
		
		// Set date of calendar
		date.setTimeInMillis(milliSec);
		
		// return day of the week from calendar
		return date.get(Calendar.DAY_OF_MONTH);
	}
	
	// Return formated string value for int stored in db
	private String getUsageString (long usageLong){
		if (showDecimal){
			NumberFormat numberFormat = new DecimalFormat("#,###.00");
			Double usage = (double)usageLong/1000000;
			return numberFormat.format(usage);
		} else {
			NumberFormat numberFormat = new DecimalFormat("#,###");
			long usage = usageLong/1000000;
			return numberFormat.format(usage);
		}
		
	}
	
	/**
	 * Get int value of screen orientation
	 * @return int
	 */
	public int getScreenOrientation() {
	        return myActivityContext.getResources().getConfiguration().orientation;
	}
	
	/**
	 * Check if in portrait mode
	 * @return true if device is in portrait
	 */
	public boolean isPortrait() {
		 if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
			 //App is in Portrait mode
			 return true;
		 }
		 else{
		     //App is in LandScape mode
			 return false;
		}
	}

}
