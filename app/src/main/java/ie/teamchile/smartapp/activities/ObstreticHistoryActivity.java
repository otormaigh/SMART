package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.retrofit.ApiRootModel;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ObstreticHistoryActivity extends BaseActivity {
	private ArrayList<String> items = new ArrayList<String>();
	public static final String defaultObsHistory = "NEED_SUGGESTION";
	private Button enter;
	private EditText edit;
	private ListView list;
	private String obsHistory, item, Obsname;
	private TextView name;

	// private String time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentForNav(R.layout.list_layout);
		
		enter = (Button) findViewById(R.id.button1);
		edit = (EditText) findViewById(R.id.editField);
		name = (TextView) findViewById(R.id.obs_name);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.activity_obstretic_history, R.id.textViewItem, items);

		list = (ListView) findViewById(R.id.notes);
		list.setAdapter(adapter);

		Obsname = ApiRootModel.getInstance().getServiceUsers().get(0).getPersonalFields().getName();
		name.setText(Obsname);

		obsHistory = ApiRootModel.getInstance().getServiceUsers().get(0).getClinicalFields().getPreviousObstetricHistory();

		items.add(obsHistory);
		if (items.contains(defaultObsHistory)) {
			items.removeAll(items);
		}
		enter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				item = edit.getText().toString();
				if (item != null && item.trim().length() > 0) {
					adapter.notifyDataSetChanged();
					items.add(item);
					edit.setText(null);
				}
			}
		});
	}
}