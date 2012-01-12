package au.id.teda.iinetusage.phone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DailyDataDBAdapter {
		
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = DailyDataDBAdapter.class.getSimpleName();
	
	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String DATE = "date";
	public static final String PERIOD ="period";
    public static final String PEAK = "peak";
    public static final String OFFPEAK = "offpeak";
    public static final String UPLOAD = "upload";
    public static final String FREEZONE = "freezone";
    public static final String DATABASE_TABLE = "daily_data_usage";
    private Context myContext;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	
    // Constructor - takes the context to allow the database to be opened/created
    public DailyDataDBAdapter(Context context) {
    	context = myContext;
    }
	
    // Opens database. If it cannot be opened, try to create a new. If it cannot be created, throw an exception to signal the failure
    public DailyDataDBAdapter open() throws SQLException {
		dbHelper = new DataBaseHelper(myContext);
		database = dbHelper.getWritableDatabase();
		return this;
	}
    
    // Close database and return type: void
    public void close() {
		dbHelper.close();
	}

    // Create new database table entry
    public long createDailyUsage(long date, String period, long peak, long offpeak, long upload, long freezone){        
    	long retLong = 0L;
    	SQLiteStatement insertStmt = null;
    	
    	String comma = ", ";
        //String bracketsComma = "', '";
        String INSERT = "INSERT OR REPLACE INTO " + DATABASE_TABLE +
        		" (" + DATE + comma + PERIOD + comma + PEAK + comma + OFFPEAK + comma + UPLOAD + comma + FREEZONE + ")" +
        		" VALUES (?,?,?,?,?,?)";
        
        //Log.d(DEBUG_TAG, "DailyDataDBAdapter > createDailyUsage > dbQuery: " + INSERT);
        try {
            insertStmt = this.database.compileStatement(INSERT);
            insertStmt.bindString(1, Long.toString(date));
            insertStmt.bindString(2, period);
            insertStmt.bindString(3, Long.toString(peak));
            insertStmt.bindString(4, Long.toString(offpeak));
            insertStmt.bindString(5, Long.toString(upload));
            insertStmt.bindString(6, Long.toString(freezone));
            retLong = insertStmt.executeInsert();
            
            /**
            Log.d(DEBUG_TAG, "DB Insert: " + Long.toString(date) 
            		+ period + Long.toString(peak) + Long.toString(offpeak)
            		+ Long.toString(upload) + Long.toString(freezone));
            **/
            
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "opps");
        } finally {
            if (insertStmt != null) {
                insertStmt.close();
            }
        }
        return retLong;
    }
    
    // Update daily usage entry
    public boolean updateDailyUsage(long rowId, long date, String period, long peak, long offpeak, long upload, long freezone){
		ContentValues updateValues = createContentValues(date, period, peak, offpeak, upload, freezone);
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
    
    // Delete daily usage entry    
    public boolean deleteDailyUsage(int rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
    
    // Return a Cursor over the list of all dates in the database
	public Cursor fetchAllDailyUsage() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID, DATE, PERIOD, PEAK, OFFPEAK, UPLOAD, FREEZONE }, 
				null, null, null, null, null, null);
	}
    
	// Return a usage for a given date
	public Cursor fetchDailyUsage (long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, DATE, PEAK, OFFPEAK, UPLOAD, FREEZONE },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		return mCursor;
	}
	
	// Return a cursor for a given period
	public Cursor fetchPeriodUsage (String period) throws SQLException {
		Log.d(DEBUG_TAG, "DailyDataDBAdapter > fetchPeriodUsage(): " + period);
		String dbQuery = "SELECT * FROM " + DATABASE_TABLE
				+ " WHERE " + PERIOD
				+ " = '" + period +"';";
		Cursor cursor = database.rawQuery(dbQuery, null);
		return cursor;
	}
	
	public Cursor fetchDailyUsageDate (long date) throws SQLException {
		Cursor mCursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + DATE + "= '" + date + "'", null);
		//Log.d(DEBUG_TAG, "SQL statement is: " + mCursor);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
    
	private ContentValues createContentValues(long date, String period, long peak, long offpeak, long upload, long freezone) {
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(PERIOD, period);
		values.put(PEAK, peak);
		values.put(OFFPEAK, offpeak);
		values.put(UPLOAD, upload);
		values.put(FREEZONE, freezone);
		return values;
	}
}
