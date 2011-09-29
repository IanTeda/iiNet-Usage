package au.id.teda.volumeusage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AccountInfoDBAdapter {
	
	private final String DEBUG_TAG = "iiNet Usage"; // Debug tag for LogCat

	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String PLAN = "plan";
	public static final String PRODUCT = "product";
	public static final String OFFPEAK_START = "offpeak_start";
	public static final String OFFPEAK_END = "offpeak_end";
	public static final String PEAK_QUOTA = "peak_quota";
	public static final String OFFPEAK_QUOTA = "offpeak_quota";
	public static final String DATABASE_TABLE = "account_information";
	private Context context;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	
    // Constructor - takes the context to allow the database to be opened/created
    public AccountInfoDBAdapter(Context context) {
    	this.context = context;
    }
		
	// Opens database. If it cannot be opened, try to create a new. If it cannot be created, throw an exception to signal the failure
	public AccountInfoDBAdapter open() throws SQLException {
		dbHelper = new DataBaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
	    
	// Close database and return type: void
	public void close() {
		dbHelper.close();
	}
	
    // Create new database table entry
    public long createAccoutInfo(String plan, String product, String offpeak_start, String offpeak_end, long peak_quota, long offpeak_quota){
        ContentValues initialValues = createContentValues(plan, product, offpeak_start, offpeak_end, peak_quota, offpeak_quota);
        //Log.d(DEBUG_TAG, "AccoutInfoDBAdapter > createAccoutInfo > insert entry: " + initialValues );
        return database.insert(DATABASE_TABLE, null, initialValues);
    }
    
    // Update daily usage entry
    public boolean updateAccountInfo(long rowId, String plan, String product, String offpeak_start, String offpeak_end, long peak_quota, long offpeak_quota){
		ContentValues updateValues = createContentValues(plan, product, offpeak_start, offpeak_end, peak_quota, offpeak_quota);
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
    
    // Delete daily usage entry    
    public boolean deleteAccountInfo(int rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
    
    // Return a Cursor over the list of all dates in the database
	public Cursor fetchAllAccountInfo() {
		return database.query(DATABASE_TABLE,
				new String[] { KEY_ROWID, PLAN, PRODUCT, OFFPEAK_START, OFFPEAK_END, PEAK_QUOTA, OFFPEAK_QUOTA }, 
				null, null, null, null, null, null);
	}
    
	// Return a usage for a given date
	public Cursor fetchAccountInfo (int rowId) throws SQLException {
		Cursor cursor = database.query(true, DATABASE_TABLE,
				new String[] {KEY_ROWID, PLAN, PRODUCT, OFFPEAK_START, OFFPEAK_END, PEAK_QUOTA, OFFPEAK_QUOTA },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean checkAccountInfoExists() {
		   Cursor cursor = database.rawQuery("SELECT 1 FROM " + DATABASE_TABLE + " WHERE _id=1", null);
		   boolean exists = (cursor.getCount() > 0);
		   cursor.close();
		   return exists;
		}
    
	private ContentValues createContentValues(String plan, String product, String offpeak_start, String offpeak_end, long peak_quota, long offpeak_quota) {
		ContentValues values = new ContentValues();
		values.put(PLAN, plan);
		values.put(PRODUCT, product);
		values.put(OFFPEAK_START, offpeak_start);
		values.put(OFFPEAK_END, offpeak_end);
		values.put(PEAK_QUOTA, peak_quota);
		values.put(OFFPEAK_QUOTA, offpeak_quota);
		return values;
	}
	
}
