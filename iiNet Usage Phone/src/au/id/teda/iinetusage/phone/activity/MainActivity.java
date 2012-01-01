package au.id.teda.iinetusage.phone.activity;

import android.os.Bundle;
import au.id.teda.iinetusage.phone.R;

public class MainActivity extends ActionbarActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }
}