package au.id.teda.iinetusage.phone.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;

public class ActionbarHelperActivity extends Activity {

	public void onClickActionBarHome (View view){
		
		// Start dashboard activity
		Intent dashboardActivityIntent = new Intent(this, MainActivity.class);
        startActivity(dashboardActivityIntent);
	}
	
	public void hideActionbarHomeIcon(){
		ImageButton myHomeImageButton = (ImageButton) findViewById(R.id.actionbar_home_button);
		ImageView mySeparatorImage = (ImageView) findViewById(R.id.actionbar_separator_1);
		myHomeImageButton.setVisibility(View.GONE);
		mySeparatorImage.setVisibility(View.GONE);
	}
	
	public void showActionbarHomeIcon(){
		ImageButton myHomeImageButton = (ImageButton) findViewById(R.id.actionbar_home_button);
		ImageView mySeparatorImage = (ImageView) findViewById(R.id.actionbar_separator_1);
		myHomeImageButton.setVisibility(View.VISIBLE);
		mySeparatorImage.setVisibility(View.VISIBLE);
	}
	
	public void setActionbarTitle(String actionbarTitle){
		TextView myActionbarTitle = (TextView) findViewById(R.id.actionbar_title);
		myActionbarTitle.setText(actionbarTitle);
	}
	
	public void hideActionbarRefersh(){
		ImageButton myRefereshImageButton = (ImageButton) findViewById(R.id.actionbar_refresh_button);
		myRefereshImageButton.setVisibility(View.GONE);
	}
	
	public void showActionbarRefersh(){
		ImageButton myRefereshImageButton = (ImageButton) findViewById(R.id.actionbar_refresh_button);
		myRefereshImageButton.setVisibility(View.VISIBLE);
	}
	
	public void onActionbarMenuClick(View button){
		switch (button.getId()) {
		case R.id.actionbar_menu_stats:
			//Intent statsIntent = new Intent(this, MainActivity.class);
			//startActivity(statsIntent);
			Toast.makeText(this, "Stats Activity", Toast.LENGTH_SHORT).show();
			break;
		case R.id.actionbar_menu_graph:
			Toast.makeText(this, "Graph Activity", Toast.LENGTH_SHORT).show();
			break;
		case R.id.actionbar_menu_data:
			Toast.makeText(this, "Data Activity", Toast.LENGTH_SHORT).show();
			break;
		case R.id.actoionbar_menu_archive:
			Toast.makeText(this, "Archive Activity", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(this, "Button not recognised", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	

}
