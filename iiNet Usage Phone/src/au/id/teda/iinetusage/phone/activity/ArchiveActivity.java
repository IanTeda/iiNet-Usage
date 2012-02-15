package au.id.teda.iinetusage.phone.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.async.RefreshUsageAsync;

public class ArchiveActivity extends ActionbarHelperActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.archive);
	}
	
	/**
	 * Method for calling refresh async task
	 * @param view
	 */
	public void onClickActionbarRefresh (View view){
		new RefreshUsageAsync(this, handler).execute();
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	
        	//loadViews();
        }
    };
	

}
