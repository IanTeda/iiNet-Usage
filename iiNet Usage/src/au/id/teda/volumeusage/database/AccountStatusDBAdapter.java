package au.id.teda.volumeusage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AccountStatusDBAdapter {
	
	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat
	
	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String REFRESH_DATE = "refresh_date"; // When did the application last pull the xml
    public static final String IP_ADDRESS = "ip_address"; // ADSL IP address currently connected on
	public static final String UPTIME = "uptime"; // Uptime on currnet ADSL IP address
    public static final String ANNIVERSARY = "anniversary"; // What date does my usage roll over
    public static final String DAYS_SO_FARE = "days_so_far"; // How long am I into my current period
    public static final String DAYS_TO_GO = "days_to_go"; // How many days to go on my current period
    public static final String PEAK_USED = "peak_used"; // Peak data consumed to date
    public static final String OFFPEAK_USED = "offpeak_used"; // Offpeak data consumed to date
    public static final String UPLOAD_USED = "upload_used"; // Offpeak data consumed to date
    public static final String FREEZONE_USED = "freezone_used"; // Offpeak data consumed to date
    public static final String PEAK_SHAPED= "peak_shaped"; // I'm I currently shaped during peak times
    public static final String OFFPEAK_SHAPED = "offpeak_shaped"; // I'm I currently shaped during offpeak times
	public static final String PEAK_SHAPING_SPEED = "peak_shaping_speed";
	public static final String OFFPEAK_SHAPING_SPEED = "offpeak_shaping_speed";
    public static final String DATABASE_TABLE = "account_status";
    
    private Context context;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	
    // Constructor - takes the context to allow the database to be opened/created
    public AccountStatusDBAdapter(Context context) {
    	this.context = context;
    }

	// Opens database. If it cannot be opened, try to create a new. If it cannot be created, throw an exception to signal the failure
	public AccountStatusDBAdapter open() throws SQLException {
		dbHelper = new DataBaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
	
    public void close() {
		dbHelper.close();
	}
    
 // Create new database table entry
    public long createAccoutStatus(
    		long refresh_date,
			String ip_address,
			long uptime, 
			long anniversary, 
			long days_so_far, 
			long days_to_go, 
			long peak_used,
			long offpeak_used,
			long upload_used,  
			long freezone_used,
			long peak_shaped,
			long offpeak_shaped,
			long peak_shaping_speed,
			long offpeak_shaping_speed){
        ContentValues initialValues = createContentValues(refresh_date, ip_address, uptime, anniversary, days_so_far, days_to_go, peak_used, offpeak_used, upload_used, freezone_used, peak_shaping_speed, offpeak_shaping_speed, peak_shaped, offpeak_shaped);
        //Log.d(DEBUG_TAG, "AccountStatusDBAdapter >  createAccoutStatus() > insert: " + initialValues);
        return database.insert(DATABASE_TABLE, null, initialValues);
    }
    
    // Update daily usage entry
    public boolean updateAccountStatus(
    		long rowId, 
    		long refresh_date, 
    		String ip_address, 
    		long uptime, 
    		long anniversary, 
    		long days_so_far, 
    		long days_to_go, 
    		long peak_used, 
    		long offpeak_used, 
    		long upload_used, 
    		long freezone_used, 
    		long peak_shaped, 
    		long offpeak_shaped, 
    		long peak_shaping_speed, 
    		long offpeak_shaping_speed){
		ContentValues updateValues = createContentValues(refresh_date, ip_address, uptime, anniversary, days_so_far, days_to_go, peak_used, offpeak_used, upload_used, freezone_used, peak_shaped, offpeak_shaped, peak_shaping_speed, offpeak_shaping_speed);
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
    
    // Delete daily usage entry    
    public boolean deleteAccountStatus(int rowId){
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
    
    // Return a Cursor over the list of all dates in the database
	public Cursor fetchAllAccountStatus(){
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID, REFRESH_DATE, IP_ADDRESS, UPTIME, ANNIVERSARY, DAYS_SO_FARE, DAYS_TO_GO, PEAK_USED, OFFPEAK_USED, UPLOAD_USED, FREEZONE_USED, PEAK_SHAPED, OFFPEAK_SHAPED, PEAK_SHAPING_SPEED, OFFPEAK_SHAPING_SPEED }, 
				null, null, null, null, null, null);
	}
    
	// Return a usage for a given date
	public Cursor fetchAccountStatusRowID (long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, REFRESH_DATE, IP_ADDRESS, UPTIME, ANNIVERSARY, DAYS_SO_FARE, DAYS_TO_GO, PEAK_USED, OFFPEAK_USED, UPLOAD_USED, FREEZONE_USED, PEAK_SHAPED, OFFPEAK_SHAPED, PEAK_SHAPING_SPEED, OFFPEAK_SHAPING_SPEED },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	// Check if accoutn info exists
	public boolean checkAccountInfoExists() {
		   Cursor cursor = database.rawQuery("SELECT 1 FROM " + DATABASE_TABLE + " WHERE _id=1", null);
		   boolean exists = (cursor.getCount() > 0);
		   cursor.close();
		   return exists;
		}
	
	// Build string for database entry
    private ContentValues createContentValues(long refresh_date,
			String ip_address,
			long uptime, 
			long anniversary, 
			long days_so_far, 
			long days_to_go, 
			long peak_used,
			long offpeak_used,
			long upload_used,  
			long freezone_used,
			long peak_shaped,
			long offpeak_shaped,
			long peak_shaping_speed,
			long offpeak_shaping_speed) {
		ContentValues values = new ContentValues();
		values.put(REFRESH_DATE, refresh_date);
		values.put(IP_ADDRESS, ip_address);
		values.put(UPTIME, uptime);
		values.put(ANNIVERSARY, anniversary);
		values.put(DAYS_SO_FARE, days_so_far);
		values.put(DAYS_TO_GO, days_to_go);
		values.put(PEAK_USED, peak_used);
		values.put(OFFPEAK_USED, offpeak_used);
		values.put(UPLOAD_USED, upload_used);
		values.put(FREEZONE_USED, freezone_used);
		values.put(PEAK_SHAPED, peak_shaped);
		values.put(OFFPEAK_SHAPED, offpeak_shaped);
		values.put(PEAK_SHAPING_SPEED, peak_shaping_speed);
		values.put(OFFPEAK_SHAPING_SPEED, offpeak_shaping_speed);
		return values;
	}
	
}
