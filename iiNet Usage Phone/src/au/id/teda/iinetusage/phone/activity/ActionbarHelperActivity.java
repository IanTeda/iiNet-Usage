package au.id.teda.iinetusage.phone.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.PreferenceHelper;

public class ActionbarHelperActivity extends Activity {
	
	// Static strings for debug tags
	//private static final String DEBUG_TAG = "iiNet Usage";
	//private static final String INFO_TAG = ActionbarHelperActivity.class.getSimpleName();

	@Override
	protected void onResume() {

		// Show hide phone status bar
		setPhoneStatusBar();

		super.onResume();
	}

	/**
	 * Handle onClick events from the actionbar
	 * @param view
	 */
	public void onClickActionBarHome(View view) {

		// Start dashboard activity
		Intent dashboardActivityIntent = new Intent(this, MainActivity.class);
		startActivity(dashboardActivityIntent);
	}

	/**
	 * Hide actionbar home icon
	 */
	public void hideActionbarHomeIcon() {
		ImageButton myHomeImageButton = (ImageButton) findViewById(R.id.actionbar_logo);
		//ImageView mySeparatorImage = (ImageView) findViewById(R.id.actionbar_separator_1);
		myHomeImageButton.setVisibility(View.GONE);
		//mySeparatorImage.setVisibility(View.GONE);
	}

	/**
	 * Show actionbar home icon
	 */
	public void showActionbarHomeIcon() {
		ImageButton myHomeImageButton = (ImageButton) findViewById(R.id.actionbar_logo);
		//ImageView mySeparatorImage = (ImageView) findViewById(R.id.actionbar_separator_1);
		myHomeImageButton.setVisibility(View.VISIBLE);
		//mySeparatorImage.setVisibility(View.VISIBLE);
	}

	/**
	 * Set the actionbar title with actionbarTitle string
	 * @param actionbarTitle
	 */
	public void setActionbarTitle(String actionbarTitle) {
		// Set custom font
		//Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "OSP-DIN.ttf");
		
		// Set TextView Reference
		TextView myActionbarTitle = (TextView) findViewById(R.id.actionbar_title);
		
		// Set TextView text
		myActionbarTitle.setText(actionbarTitle);
		
		// Set TextView custom font
		//myActionbarTitle.setTypeface(myCustomFont);
	}

	/**
	 * Hide actionbar refresh icon
	 */
	public void hideActionbarRefersh() {
		ImageButton myRefereshImageButton = (ImageButton) findViewById(R.id.actionbar_refresh_button);
		myRefereshImageButton.setVisibility(View.GONE);
	}

	/**
	 * Show actionbar refresh icon
	 */
	public void showActionbarRefersh() {
		ImageButton myRefereshImageButton = (ImageButton) findViewById(R.id.actionbar_refresh_button);
		myRefereshImageButton.setVisibility(View.VISIBLE);
	}

	/**
	 * Action bar menu onClick method
	 * @param button
	 */
	public void onActionbarMenuClick(View button) {
		switch (button.getId()) {
		case R.id.actionbar_menu_dash:
			Intent dashIntent = new Intent(this, MainActivity.class);
			startActivity(dashIntent);
			break;
		case R.id.actionbar_menu_graph:
			Intent graphsIntent = new Intent(this, GraphsActivity.class);
			startActivity(graphsIntent);
			break;
		case R.id.actionbar_menu_data:
			Intent dataIntent = new Intent(this, DailyDataActivity.class);
			startActivity(dataIntent);
			break;
		case R.id.actoionbar_menu_archive:
			Intent archiveIntent = new Intent(this, ArchiveActivity.class);
			startActivity(archiveIntent);
			break;
		default:
			Toast.makeText(this, "Button not recognised", Toast.LENGTH_SHORT)
					.show();

		}
	}

	/**
	 * Show hide phone status based on preference setting
	 */
	public void setPhoneStatusBar() {
		PreferenceHelper mySetttings = new PreferenceHelper();

		if (mySetttings.hidePhoneStatusBar()) {
			((Activity) this).getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			((Activity) this).getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	/**
	 * Get int value of screen orientation
	 * @return int
	 */
	public int getScreenOrientation() {
	        return getResources().getConfiguration().orientation;
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
