package au.id.teda.iinetusage.phone.cursor;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.activity.DailyDataActivity;
import au.id.teda.iinetusage.phone.database.DailyDataDBAdapter;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class DailyDataCursorAdapter extends CursorAdapter {

	// TODO: Update layout for better scrolling
	// TODO: Add onclick hourly data
	
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = DailyDataCursorAdapter.class.getSimpleName();
	
	// Load object for preferences
	private final PreferenceHelper mySettings;
	
    private final LayoutInflater mInflater;
    boolean showDecimal = false;
    boolean showFuture = false;
    private long dateLong;
	
	public DailyDataCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, true);
		mInflater = LayoutInflater.from(context);
		
		// Set preferences object
		mySettings = new PreferenceHelper();
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		showDecimal = settings.getBoolean("show_decimal", false);
		showFuture = settings.getBoolean("show_future", false);
	}
    
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		//Log.d(DEBUG_TAG, "DailyDataCursorAdapter > bindView");
		
		// Set variables and pull data from database cursor
		// TODO: Change DailyDataDBAdapter to dailyDataDBHelper???
		long dateLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.DATE)); // Pull date time stamp from cursor
		long peakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.PEAK)); // Pull peak usage from cursor
		long offpeakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.OFFPEAK)); // Pull offpeak usage from cursor
		long uploadUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.UPLOAD)); // Pull upload usage from cursor
		long freezoneUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDBAdapter.FREEZONE)); // Pull upload usage from cursor
		long totalUsageLong = (peakUsageLong + offpeakUsageLong); // Add up the total for the day
		
		// Set text for dates
		//TextView tv1 = (TextView) view.findViewById(R.id.list_col_date_row_day_tv);
		//tv1.setText(LongDateToString(longDate, "dayOfWeek")); // Day of the week
		TextView dateTV = (TextView) view.findViewById(R.id.listview_date_tv);
		dateTV.setText(LongDateToString(dateLong, "dateOfMouth")); // Day of the mouth
		//TextView tv3 = (TextView) view.findViewById(R.id.list_col_date_row_mth_tv);
		//tv3.setText(LongDateToString(longDate, "mouthOfYear")); // Mouth of the year
		
		// Set usage textviews
		TextView peakTV = (TextView) view.findViewById(R.id.listview_peak_tv);
		peakTV.setText(IntUsageToString(peakUsageLong)); // Peak usage
		TextView offpeakTV = (TextView) view.findViewById(R.id.listview_offpeak_tv);
		offpeakTV.setText(IntUsageToString(offpeakUsageLong)); // Offpeak usage
		TextView uploadTV = (TextView) view.findViewById(R.id.listview_upload_tv);
		uploadTV.setText(IntUsageToString(uploadUsageLong)); // Upload usage
		TextView freezoneTV = (TextView) view.findViewById(R.id.listview_freezone_tv);
		freezoneTV.setText(IntUsageToString(freezoneUsageLong)); // Freezone usage
		TextView totalTV = (TextView) view.findViewById(R.id.listview_total_tv);
		totalTV.setText(IntUsageToString(totalUsageLong)); // Freezone usage

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Inflate the listview with the changes above
		//Log.d(DEBUG_TAG, "DailyDataCursorAdapter > newView() > Inflate");
		View view = mInflater.inflate(R.layout.usage_data_list_row, parent, false);
		return view;
		//if (showFuture == false && System.currentTimeMillis() > dateLong ){ }

	}
	
	// Return string values for date long millisec stored in db
	private String LongDateToString(long millisecs, String convertTo) {
		DateFormat date_format = null;
		if (convertTo == "dayOfWeek") {
			date_format = new SimpleDateFormat("EEE");
		} else if (convertTo == "dateOfMouth"){
			date_format = new SimpleDateFormat("dd");
		} else if (convertTo == "mouthOfYear"){
			date_format = new SimpleDateFormat("MMM");
		}
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}
	
	// Return formated string value for int stored in db
	private String IntUsageToString (long usageLong){
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

}
