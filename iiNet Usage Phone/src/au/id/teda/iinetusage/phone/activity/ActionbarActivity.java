package au.id.teda.iinetusage.phone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.iinetusage.phone.R;

public class ActionbarActivity extends Activity {

	public void onClickActionBarHome (View view){
		Toast.makeText(this, "Take me home", Toast.LENGTH_SHORT).show();
	}
	
	public void onClickActionbarRefresh (View view){
		Toast.makeText(this, "Refersh data", Toast.LENGTH_SHORT).show();
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
	
	

}
