package au.id.teda.iinetusage.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.UserPassHelper;
import au.id.teda.iinetusage.phone.view.AlertboxView;

public class MainActivity extends ActionbarActivity {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = MainActivity.class.getSimpleName();
	
	private Button myAlertboxButton;
	private AlertboxView myAlertboxView;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        
        userPassCheck();
        
		myAlertboxButton = (Button) findViewById(R.id.alertbox_button);
		myAlertboxView = new AlertboxView();
		
        myAlertboxView.setAlert(myAlertboxButton);
        
    }

	private void userPassCheck() {
		UserPassHelper UserPassHelper = new UserPassHelper();
		if (!UserPassHelper.isUsernamePasswordSet()){
			Log.d(DEBUG_TAG, "User password not set, open settings");
		}
		
	}
	
	/**
	 * Method for managing onClick events of the Alertbox based on current alert text
	 * @param view
	 */
	public void onClickAlertboxButton (View view){
		// Get current string value of the alert box
		String alertboxButtonText =  (String) myAlertboxButton.getText();
		Log.i(INFO_TAG, "onClickAlertboxButton() > Button: " + alertboxButtonText);
		
		// Set action based on value of alertbox string
    	if ( alertboxButtonText == this.getString(R.string.alertbox_no_data)){
    		Toast.makeText(this, "Refersh data", Toast.LENGTH_SHORT).show();
    		
    	} else if (alertboxButtonText == this.getString(R.string.alertbox_no_userpass)
    			|| alertboxButtonText == this.getString(R.string.alertbox_no_user)
    			|| alertboxButtonText == this.getString(R.string.alertbox_no_pass)){
    		Intent myUserPassIntent = new Intent(MainActivity.this, UserPassActivity.class);
			startActivity(myUserPassIntent);
    	}
		
	}
	
	/**
	 * onCreate method for options menu
	 * What happens when we press the menu button
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(INFO_TAG, "onCreateOptionsMenu()");
		super.onCreateOptionsMenu(menu);
		
		// Menu inflator object
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
}