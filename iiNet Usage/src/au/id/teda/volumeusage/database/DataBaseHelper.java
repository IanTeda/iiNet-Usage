package au.id.teda.volumeusage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import au.id.teda.volumeusage.activity.MainActivity;

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
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create database table for the account information
		final String createDBTable1 = 
				"create table " + AccountInfoDBAdapter.DATABASE_TABLE +
				" (" + AccountInfoDBAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AccountInfoDBAdapter.PLAN + " TEXT NOT NULL, "
				+ AccountInfoDBAdapter.PRODUCT + " TEXT NOT NULL, "
				+ AccountInfoDBAdapter.OFFPEAK_START + " TEXT NOT NULL, " //Need to check how to start times in database i.e text
				+ AccountInfoDBAdapter.OFFPEAK_END  + " TEXT NOT NULL, "
				+ AccountInfoDBAdapter.PEAK_QUOTA + " INTEGER NOT NULL, "
				+ AccountInfoDBAdapter.OFFPEAK_QUOTA + " INTEGER NOT NULL);";		
		Log.d(DEBUG_TAG, "onCreate: Creat database tables" + createDBTable1);
		database.execSQL(createDBTable1);
		
		// Create database for the account status
		final String createDBTable2 = 
				"create table " + AccountStatusDBAdapter.DATABASE_TABLE +
				" (" + AccountStatusDBAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AccountStatusDBAdapter.REFRESH_DATE + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.IP_ADDRESS + " TEXT NOT NULL, "
				+ AccountStatusDBAdapter.UPTIME + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.ANNIVERSARY  + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.DAYS_SO_FARE+ " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.DAYS_TO_GO + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.PEAK_USED + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.OFFPEAK_USED + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.UPLOAD_USED + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.FREEZONE_USED + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.PEAK_SHAPED + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.OFFPEAK_SHAPED + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.PEAK_SHAPING_SPEED + " INTEGER NOT NULL, "
				+ AccountStatusDBAdapter.OFFPEAK_SHAPING_SPEED + " INTEGER NOT NULL);";		
		Log.d(DEBUG_TAG, "onCreate: Creat database tables" + createDBTable2);
		database.execSQL(createDBTable2);
		
		// Create database table for daily usage stats
		final String createDBTable3 = 
				"create table " + DailyDataDBAdapter.DATABASE_TABLE +
				" (" + DailyDataDBAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DailyDataDBAdapter.DATE + " INTEGER UNIQUE, "
				+ DailyDataDBAdapter.PERIOD + " TEXT NOT NULL, "
				+ DailyDataDBAdapter.PEAK + " INTEGER NOT NULL, "
				+ DailyDataDBAdapter.OFFPEAK + " INTEGER NOT NULL, "
				+ DailyDataDBAdapter.UPLOAD + " INTEGER NOT NULL, "
				+ DailyDataDBAdapter.FREEZONE + " INTEGER NOT NULL);";		
		Log.d(DEBUG_TAG, "onCreate: Creat database tables" + createDBTable3);
		database.execSQL(createDBTable3);
		
		// Create database table for hourly usage stats
		final String createDBTable4 = 
				"create table " + HourlyDataDBAdapter.DATABASE_TABLE +
				" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ HourlyDataDBAdapter.DATE_HOUR + " INTEGER UNIQUE, "
				+ HourlyDataDBAdapter.PEAK + " INTEGER NOT NULL, "
				+ HourlyDataDBAdapter.OFFPEAK + " INTEGER NOT NULL, "
				+ HourlyDataDBAdapter.UPLOAD + " INTEGER NOT NULL, "
				+ HourlyDataDBAdapter.FREEZONE + " INTEGER NOT NULL);";		
		Log.d(DEBUG_TAG, "onCreate: Creat database tables" + createDBTable4);
		database.execSQL(createDBTable4);
			
	}
        
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion){
			Log.d(DEBUG_TAG, "Old database version number is less then new version. Do nothing.");
			return;
		} else {
			// TODO: Adding any table mods to this guy here
			Log.d("iiNet Usage", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will ??");
		}
	}
}
