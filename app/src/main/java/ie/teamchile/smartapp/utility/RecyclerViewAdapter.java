package ie.teamchile.smartapp.utility;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import ie.teamchile.smartapp.R;

/**
 * Created by user on 5/30/15.
 */
public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewPos;
        TextView textViewData;
        TextView tvTime;
        TextView tvName;
        TextView tvGest;
        ImageView ivAttend;
        Button btnChangeStatus;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_appt_list);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvGest = (TextView) itemView.findViewById(R.id.tv_gestation);
            ivAttend = (ImageView) itemView.findViewById(R.id.img_attended);
            btnChangeStatus = (Button) itemView.findViewById(R.id.btn_change_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewData.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Context mContext;
    private List<String> timeList;
    private List<String> nameList;
    private List<String> gestList;
    private List<Integer> idList;

    public RecyclerViewAdapter(Context context,
                               List<String> timeList,
                               List<String> nameList,
                               List<String> gestList,
                               List<Integer> idList) {
        this.mContext = context;
        this.timeList = timeList;
        this.nameList = nameList;
        this.gestList = gestList;
        this.idList = idList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointment_layout, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        if(idList.get(position).equals(0)){
            viewHolder.tvTime.setText(timeList.get(position));
            viewHolder.tvName.setText(nameList.get(position));
            viewHolder.tvGest.setText(gestList.get(position));

            viewHolder.tvName.setTextColor(Color.GREEN);
            viewHolder.tvName.setShadowLayer(1, 0, 0, Color.BLACK);
            mItemManger.bindView(viewHolder.itemView, position);
        } else {
            viewHolder.tvTime.setText(timeList.get(position));
            viewHolder.tvName.setText(nameList.get(position));
            viewHolder.tvGest.setText(gestList.get(position));
        }

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                Log.d("Recycle", "item swipped");
                Toast.makeText(mContext, "Item Swipped", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Log.d("Recycle", "item double clicked");
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.ivAttend.setBackgroundResource(R.color.green);
                mItemManger.closeAllItems();
                mItemManger.bindView(viewHolder.itemView, position);

                //mItemManger.closeItem(position);
            }
        });
        mItemManger.bindView(viewHolder.itemView, position);



       /* String item = timeList.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.textViewPos.setText((position + 1) + ".");
        viewHolder.textViewData.setText(item);
        mItemManger.bindView(viewHolder.itemView, position);*/
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_appt_list;
    }
}