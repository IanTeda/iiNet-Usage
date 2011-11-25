package au.id.teda.volumeusage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import au.id.teda.volumeusage.R;

public class ArchiveArrayAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] names;

	public ArchiveArrayAdapter(Activity context, String[] names) {
		super(context, R.layout.archive_row_layout, names);
		this.context = context;
		this.names = names;
	}
	
	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row
		// layout

		ViewHolder holder;
		// Recycle existing view if passed as parameter
		// This will save memory and time on Android
		// This only works if the base layout for all classes are the same
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.archive_row_layout, null, true);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(R.id.archive_row_month);
				holder.imageView = (ImageView) rowView.findViewById(R.id.arch_list_icon);
				rowView.setTag(holder);
			} else {
				holder = (ViewHolder) rowView.getTag();
			}

			holder.textView.setText(names[position]);
			// Change the icon for Windows and iPhone
			String s = names[position];
			if (s.startsWith("Add")) {
				holder.imageView.setImageResource(R.drawable.arch_ic_plus);
			} else {
				holder.imageView.setImageResource(R.drawable.arch_ic_calendar);
			}

			return rowView;
		}
	
}
