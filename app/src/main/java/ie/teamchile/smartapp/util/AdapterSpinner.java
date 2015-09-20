package ie.teamchile.smartapp.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import ie.teamchile.smartapp.R;

/**
 * Created by user on 6/11/15.
 */
public class AdapterSpinner extends ArrayAdapter<String> {
    List<String> list;
    Context context;
    int layoutResource;
    int tvResource;

    public AdapterSpinner(Context context, int layoutResource, List<String> list, int tvResource) {
        super(context, layoutResource, list);
        this.list = list;
        this.context = context;
        this.layoutResource = layoutResource;
        this.tvResource = tvResource;
    }

    public AdapterSpinner(Context context, int arrayResource, int layoutResource, int tvResource) {
        super(context, arrayResource, layoutResource);
        this.list = Arrays.asList(context.getResources().getStringArray(arrayResource));
        this.context = context;
        this.layoutResource = layoutResource;
        this.tvResource = tvResource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, layoutResource, null);
        TextView tvSpinnerItem = (TextView) convertView.findViewById(tvResource);
        tvSpinnerItem.setText(list.get(position));
        switch (position) {
            case 0:
                tvSpinnerItem.setTypeface(null, Typeface.ITALIC);
                tvSpinnerItem.setTextColor(ContextCompat.getColor(context, R.color.light_grey));
                break;
            default:
                tvSpinnerItem.setTextColor(ContextCompat.getColor(context, R.color.black));
                tvSpinnerItem.setTypeface(Typeface.DEFAULT);
                break;
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, layoutResource, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvSpinnerItem.setText(list.get(position));
        switch (position) {
            case 0:
                holder.tvSpinnerItem.setTypeface(null, Typeface.ITALIC);
                holder.tvSpinnerItem.setTextColor(ContextCompat.getColor(context, R.color.grey));
                break;
            default:
                holder.tvSpinnerItem.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.tvSpinnerItem.setTypeface(Typeface.DEFAULT);
                break;
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvSpinnerItem;

        public ViewHolder(View view) {
            tvSpinnerItem = (TextView) view.findViewById(tvResource);
        }
    }
}
