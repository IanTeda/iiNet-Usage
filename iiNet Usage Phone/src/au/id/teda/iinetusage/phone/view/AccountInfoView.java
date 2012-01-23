package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import au.id.teda.iinetusage.phone.R;
import au.id.teda.iinetusage.phone.helper.AccountHelper;

public class AccountInfoView extends AccountHelper {
	
	// Static strings for debug tags
	private static final String DEBUG_TAG = "iiNet Usage";
	private static final String INFO_TAG = AccountInfoView.class.getSimpleName();
	
	private final Context myActivityContext;
	private final Activity myActivity;
	
	private final TextView myIpTitle;
	
	public AccountInfoView(Context context) {
		// Construct context
		myActivityContext = context;
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext : null;
		
		// TextView objects
		myIpTitle = (TextView) myActivity.findViewById(R.id.account_info_ip);
	}

	public void hideIpTitle(){
        myIpTitle.setVisibility(View.GONE);
	}
	
	public void showIpTitle(){
        myIpTitle.setVisibility(View.VISIBLE);
	}

}
