package au.id.teda.volumeusage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import au.id.teda.volumeusage.R;

public class AboutActivity extends Activity {
	//TODO: Add version number to about dialog
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		TextView t2 = (TextView) findViewById(R.id.about_content);
		t2.setMovementMethod(LinkMovementMethod.getInstance());
		
	}
}
