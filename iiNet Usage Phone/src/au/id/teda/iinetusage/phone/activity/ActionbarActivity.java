package au.id.teda.iinetusage.phone.activity;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class ActionbarActivity extends Activity {
	
	public void onClickActionBarHome (View view){
		Toast.makeText(this, "Take me home", Toast.LENGTH_SHORT).show();
	}
	
	public void onClickActionbarRefresh (View view){
		Toast.makeText(this, "Refersh data", Toast.LENGTH_SHORT).show();
	}

}
