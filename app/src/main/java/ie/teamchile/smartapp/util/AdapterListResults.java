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
    private List<String> resultName;
    private List<String> resultDob;
    private List<String> resultHospitalNumber;
    private LayoutInflater layoutInflater;


    public AdapterListResults(
            Context context,
            List<String> resultName,
            List<String> resultDob,
            List<String> resultHospitalNumber){
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_layout_search_results, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvName.setText(resultName.get(position));
        holder.tvDob.setText(resultDob.get(position));
        holder.tvHospitalNumber.setText(resultHospitalNumber.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvDob;
        TextView tvHospitalNumber;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_results_name);
            tvDob = (TextView) view.findViewById(R.id.tv_results_dob);
            tvHospitalNumber = (TextView) view.findViewById(R.id.tv_results_hospital_number);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
