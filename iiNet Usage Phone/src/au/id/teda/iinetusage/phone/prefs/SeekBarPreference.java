package au.id.teda.iinetusage.phone.prefs;

// http://www.codeproject.com/KB/android/seekbar_preference.aspx

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.preference.DialogPreference;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import au.id.teda.iinetusage.phone.R;

public final class SeekBarPreference extends DialogPreference implements OnSeekBarChangeListener {

    // Name spaces to read attributes
    private static final String PREFERENCE_NS = "http://schemas.android.com/apk/res/au.id.teda.iinetusage.phone.prefs";
    private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

    // Attribute names
    private static final String ATTR_DEFAULT_VALUE = "defaultValue";
    private static final String ATTR_MIN_VALUE = "minValue";
    private static final String ATTR_MAX_VALUE = "maxValue";

    // Default values for defaults
    private static final int DEFAULT_CURRENT_VALUE = 75;
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;

    // Real defaults
    private final int myDefaultValue;
    private final int myMaxValue;
    private final int myMinValue;
    
    // Current value
    private int myCurrentValue;
    
    // View elements
    private SeekBar mySeekBar;
    private TextView myValueText;

    public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		// Read parameters from attributes
		myMinValue = attrs.getAttributeIntValue(PREFERENCE_NS, ATTR_MIN_VALUE, DEFAULT_MIN_VALUE);
		myMaxValue = attrs.getAttributeIntValue(PREFERENCE_NS, ATTR_MAX_VALUE, DEFAULT_MAX_VALUE);
		myDefaultValue = attrs.getAttributeIntValue(ANDROID_NS, ATTR_DEFAULT_VALUE, DEFAULT_CURRENT_VALUE);
    }

    @Override
    protected View onCreateDialogView() {
		// Get current value from preferences
		myCurrentValue = getPersistedInt(myDefaultValue);
	
		// Inflate layout
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_slider, null);
	
		// Setup minimum and maximum text labels
		((TextView) view.findViewById(R.id.min_value)).setText(Integer.toString(myMinValue));
		((TextView) view.findViewById(R.id.max_value)).setText(Integer.toString(myMaxValue));
	
		// Setup SeekBar
		mySeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
		mySeekBar.setMax(myMaxValue - myMinValue);
		mySeekBar.setProgress(myCurrentValue - myMinValue);
		mySeekBar.setOnSeekBarChangeListener(this);
	
		// Setup text label for current value
		myValueText = (TextView) view.findViewById(R.id.current_value);
		myValueText.setText(Integer.toString(myCurrentValue));
	
		return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
	
		// Return if change was cancelled
		if (!positiveResult) {
		    return;
		}
		
		// Persist current value if needed
		if (shouldPersist()) {
		    persistInt(myCurrentValue);
		}
	
		// Notify activity about changes (to update preference summary line)
		notifyChanged();
    }

    @Override
    public CharSequence getSummary() {
		// Format summary string with current value
		String summary = super.getSummary().toString();
		int value = getPersistedInt(myDefaultValue);
		return String.format(summary, value);
    }
    
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
		// Update current value
		myCurrentValue = value + myMinValue;
		// Update label with current value
		myValueText.setText(Integer.toString(myCurrentValue));
    }

    public void onStartTrackingTouch(SeekBar seek) {
    	// Not used
    }

    public void onStopTrackingTouch(SeekBar seek) {
    	// Not used
    }
}