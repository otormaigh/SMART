package ie.teamchile.smartapp.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
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
    int layoutresource;
    int tvResource;

    public AdapterSpinner(Context context, int layoutresource, List<String> list, int tvResource) {
        super(context, layoutresource, list);
        this.list = list;
        this.context = context;
        this.layoutresource = layoutresource;
        this.tvResource = tvResource;
    }

    public AdapterSpinner(Context context, int arrayResource, int layoutresource, int tvResource) {
        super(context, layoutresource, arrayResource);
        String[] resArrayId = context.getResources().getStringArray(arrayResource);
        List<String> list = Arrays.asList(resArrayId);
        Log.d("bugs", "list = " + list);
        this.list = list;
        this.context = context;
        this.layoutresource = layoutresource;
        this.tvResource = tvResource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, layoutresource, null);
        TextView tvSpinnerItem = (TextView) convertView.findViewById(tvResource);
        tvSpinnerItem.setText(list.get(position));
        switch (position) {
            case 0:
                tvSpinnerItem.setTypeface(null, Typeface.ITALIC);
                tvSpinnerItem.setTextColor(context.getResources().getColor(R.color.light_grey));
                break;
            default:
                tvSpinnerItem.setTextColor(context.getResources().getColor(R.color.black));
                tvSpinnerItem.setTypeface(Typeface.DEFAULT);
                break;
        }
        //return super.getDropDownView(position, convertView, parent);
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, layoutresource, null);
        TextView tvSpinnerItem = (TextView) convertView.findViewById(tvResource);
        tvSpinnerItem.setText(list.get(position));
        switch (position) {
            case 0:
                tvSpinnerItem.setTypeface(null, Typeface.ITALIC);
                tvSpinnerItem.setTextColor(context.getResources().getColor(R.color.grey));
                break;
            default:
                tvSpinnerItem.setTextColor(context.getResources().getColor(R.color.black));
                tvSpinnerItem.setTypeface(Typeface.DEFAULT);
                break;
        }
        //return super.getView(position, convertView, parent);
        return convertView;
    }
}
