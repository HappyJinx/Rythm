package com.fanyunlv.xialei.rythm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmTimeAdapter extends RecyclerView.Adapter<RythmTimeAdapter.RythmViewHolder> implements View.OnClickListener{

    private final String TAG = "Rythm";
    private List<TimeItem> timeitemlist = new ArrayList<>();
    private Context mcontext;

    public RythmTimeAdapter(Context context, List<TimeItem> itemlist){
        timeitemlist = itemlist;
        mcontext = context;
    }

    public void replaceList(List<TimeItem> list) {
        timeitemlist.clear();
        timeitemlist.addAll(list);
    }

    @Override
    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item_layout,null);
        RythmViewHolder holder = new RythmViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(RythmViewHolder holder, int position) {
        holder.item_time.setText(timeitemlist.get(position).toString());
        holder.item_delete.setOnClickListener(this);
        holder.item_delete.setTag(position);
    }

    @Override
    public int getItemCount() {
        return timeitemlist.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_item:
                Log.i(TAG, "onClick: v.getTag()="+(v.getTag()));
                TimeItem timeItem = timeitemlist.get((int) v.getTag());
                DBhelper.getInstance(mcontext).deleteitem(timeItem.getHour(),timeItem.getMinute());
                timeitemlist.remove((int)v.getTag());
                notifyDataSetChanged();
                break;
        }
    }

    public class RythmViewHolder extends RecyclerView.ViewHolder {
        public TextView item_time;
        public Button item_delete;
        public RythmViewHolder(View itemView) {
            super(itemView);
            item_time = itemView.findViewById(R.id.time_setted);
            item_delete = itemView.findViewById(R.id.delete_item);
        }
    }

}
