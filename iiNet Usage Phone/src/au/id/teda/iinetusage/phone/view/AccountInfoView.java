package au.id.teda.iinetusage.phone.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

	// TextView objects for IP & Up-time block
	private final TextView myIpTitle;
	private final TextView myIpUpSlashTitle;
	private final TextView myUpTitle;
	private final TextView myIpUpdata;

	// TextView objects for quota block
	private final TextView myPeakTitle;
	private final TextView myPeakOffpeakTitle;
	private final TextView myOffPeakTitle;
	private final TextView myPeakOffpeakData;

	public AccountInfoView(Context context) {
		// Construct context
		myActivityContext = context;
		// Construct activity from context
		myActivity = (myActivityContext instanceof Activity) ? (Activity) myActivityContext	: null;

		// IP & Up Time TextView objects
		myIpTitle = (TextView) myActivity.findViewById(R.id.account_info_ip);
		myIpUpSlashTitle = (TextView) myActivity.findViewById(R.id.account_info_ip_up_slash);
		myUpTitle = (TextView) myActivity.findViewById(R.id.account_info_up);
		myIpUpdata = (TextView) myActivity.findViewById(R.id.account_info_ip_up_data);

		// Quota TextView objects
		myPeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_peak);
		myPeakOffpeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_slash);
		myOffPeakTitle = (TextView) myActivity.findViewById(R.id.account_info_quota_offpeak);
		myPeakOffpeakData = (TextView) myActivity.findViewById(R.id.account_info_quota_data);
	}

	/**
	 * Method for hiding IP Up-time block
	 */
	public void hideIpUpBlock() {
		myIpTitle.setVisibility(View.GONE);
		myIpUpSlashTitle.setVisibility(View.GONE);
		myUpTitle.setVisibility(View.GONE);
		myIpUpdata.setVisibility(View.GONE);
	}

	/**
	 * Method for showing IP Up-time block
	 */
	public void showIpUpBlock() {
		myIpTitle.setVisibility(View.VISIBLE);
		myIpUpSlashTitle.setVisibility(View.VISIBLE);
		myUpTitle.setVisibility(View.VISIBLE);
		myIpUpdata.setVisibility(View.VISIBLE);
	}

	/**
	 * Method for checking if IP Up-time block is hidden
	 * 
	 * @return true if block hidden
	 */
	public boolean isIpUpBlockHidden() {
		// Check if views are set to gone
		if (myIpTitle.getVisibility() == View.GONE
				&& myIpUpSlashTitle.getVisibility() == View.GONE
				&& myUpTitle.getVisibility() == View.GONE
				&& myIpUpdata.getVisibility() == View.GONE) {
			// They are so lets return true
			return true;
		}
		// Else it must be visible
		else {
			// So lets return false
			return false;
		}
	}

	/**
	 * Method for hiding Quota block
	 */
	public void hideQuotaBlock() {
		myPeakTitle.setVisibility(View.GONE);
		myPeakOffpeakTitle.setVisibility(View.GONE);
		myOffPeakTitle.setVisibility(View.GONE);
		myPeakOffpeakData.setVisibility(View.GONE);
	}

	/**
	 * Method for showing Quota block
	 */
	public void showQuotaBlock() {
		myPeakTitle.setVisibility(View.VISIBLE);
		myPeakOffpeakTitle.setVisibility(View.VISIBLE);
		myOffPeakTitle.setVisibility(View.VISIBLE);
		myPeakOffpeakData.setVisibility(View.VISIBLE);
	}

	/**
	 * Method for checking if Quota block is hidden
	 * 
	 * @return true if block hidden
	 */
	public boolean isQuotaBlockHidden() {
		// Check if views are set to gone
		if (myPeakTitle.getVisibility() == View.GONE
				&& myPeakOffpeakTitle.getVisibility() == View.GONE
				&& myOffPeakTitle.getVisibility() == View.GONE
				&& myPeakOffpeakData.getVisibility() == View.GONE) {
			// They are so lets return true
			return true;
		}
		// Else it must be visible
		else {
			// So lets return false
			return false;
		}
	}

}
