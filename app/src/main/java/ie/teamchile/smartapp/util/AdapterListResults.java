package ie.teamchile.smartapp.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.model.ServiceUser;
import timber.log.Timber;

/**
 * Created by user on 7/3/15.
 */
public class AdapterListResults extends BaseAdapter {
    private ViewHolder holder;
    private List<ServiceUser> serviceUsers;
    private LayoutInflater layoutInflater;

    public AdapterListResults(
            Context context,
            List<ServiceUser> serviceUsers){
        this.serviceUsers = serviceUsers;

       layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return serviceUsers.size();
    }

    @Override
    public ServiceUser getItem(int position) {
        return serviceUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return serviceUsers.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_layout_search_results, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        try {
            holder.tvName.setText(getItem(position).getPersonalFields().getName());
        } catch (NullPointerException e) {
            Timber.e(Log.getStackTraceString(e));
            holder.tvName.setText(null);
        }

        holder.tvDob.setText(getItem(position).getPersonalFields().getDob());
        holder.tvHospitalNumber.setText(getItem(position).getHospitalNumber());

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
}
