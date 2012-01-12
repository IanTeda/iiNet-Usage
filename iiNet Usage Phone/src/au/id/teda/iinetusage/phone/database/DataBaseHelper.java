package au.id.teda.iinetusage.phone.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import au.id.teda.iinetusage.phone.AppGlobals;

public class DataBaseHelper extends SQLiteOpenHelper {

	// Static tags for logging
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = DataBaseHelper.class.getSimpleName();
	
	// Set some variables for the handler
	public static String DATABASE_NAME = "iiusage.db";
	public static final int DATABASE_VERSION = 1;
	
	/**
     * Constructor 
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
	public DataBaseHelper() {
		super(AppGlobals.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create database table for daily usage stats
		final String dailyDataDBTable = 
				"create table " + DailyDataDBAdapter.DATABASE_TABLE +
				" (" + DailyDataDBAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DailyDataDBAdapter.DATE + " INTEGER UNIQUE, "
				+ DailyDataDBAdapter.PERIOD + " TEXT NOT NULL, "
				+ DailyDataDBAdapter.PEAK + " INTEGER NOT NULL, "
				+ DailyDataDBAdapter.OFFPEAK + " INTEGER NOT NULL, "
				+ DailyDataDBAdapter.UPLOAD + " INTEGER NOT NULL, "
				+ DailyDataDBAdapter.FREEZONE + " INTEGER NOT NULL);";		
		Log.d(DEBUG_TAG, "onCreate: Creat database tables" + dailyDataDBTable);
		database.execSQL(dailyDataDBTable);
		
		// Create database table for hourly usage stats
		final String HourlyDataDBTable = 
				"create table " + HourlyDataDBAdapter.DATABASE_TABLE +
				" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ HourlyDataDBAdapter.DATE_HOUR + " INTEGER UNIQUE, "
				+ HourlyDataDBAdapter.PEAK + " INTEGER NOT NULL, "
				+ HourlyDataDBAdapter.OFFPEAK + " INTEGER NOT NULL, "
				+ HourlyDataDBAdapter.UPLOAD + " INTEGER NOT NULL, "
				+ HourlyDataDBAdapter.FREEZONE + " INTEGER NOT NULL);";		
		Log.d(DEBUG_TAG, "onCreate: Creat database tables" + HourlyDataDBTable);
		database.execSQL(HourlyDataDBTable);
			
	}
        
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion){
			Log.i(INFO_TAG, "Old database version number is less then new version. Do nothing.");
			return;
		} else {
			// TODO: Adding any table mods to this guy here
			Log.d("iiNet Usage", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will ??");
		}
	}
}
