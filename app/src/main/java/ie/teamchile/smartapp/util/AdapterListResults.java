package ie.teamchile.smartapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ie.teamchile.smartapp.R;

/**
 * Created by user on 7/3/15.
 */
public class AdapterListResults extends BaseAdapter {
    private Context context;
    private List<String> resultName;
    private List<String> resultDob;
    private List<String> resultHospitalNumber;
    private LayoutInflater layoutInflater;


    public AdapterListResults(
            Context context,
            List<String> resultName,
            List<String> resultDob,
            List<String> resultHospitalNumber){
        this.context = context;
        this.resultName = resultName;
        this.resultDob = resultDob;
        this.resultHospitalNumber = resultHospitalNumber;

        layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return resultName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_layout_search_results, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_results_name);
        TextView tvDob = (TextView) convertView.findViewById(R.id.tv_results_dob);
        TextView tvHospitalNumber = (TextView) convertView.findViewById(R.id.tv_results_hospital_number);

        tvName.setText(resultName.get(position));
        tvDob.setText(resultDob.get(position));
        tvHospitalNumber.setText(resultHospitalNumber.get(position));
        return convertView;
    }
}
