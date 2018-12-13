package com.fanyunlv.xialei.rythm.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.beans.TaskItems;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/12/11.
 */
public class LocationTaskAdapter extends RecyclerView.Adapter<LocationTaskAdapter.RythmViewHolder> implements View.OnClickListener {

    private Context mcontext;
    private ArrayList<TaskItems> taskArrayList;

    public LocationTaskAdapter(Context context, ArrayList<TaskItems> taskDetails) {
        mcontext = context;
        taskArrayList = taskDetails;
    }

    @Override
    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_full_item_layout,null);
        RythmViewHolder holder = new RythmViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(RythmViewHolder holder, int position) {
        holder.item_setted.setText(taskArrayList.get(position).getName());
        holder.item_setted_info.setText("now it is empty now it is empty now it is empty now it is empty");
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        int postion = (int)v.getTag();
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class RythmViewHolder extends RecyclerView.ViewHolder {

        public TextView item_setted;
        public TextView item_setted_info;
        public Button item_delete;
        public RythmViewHolder(View itemView) {
            super(itemView);
            item_setted = itemView.findViewById(R.id.item_setted);
            item_setted_info = itemView.findViewById(R.id.item_setted_info);
            item_delete = itemView.findViewById(R.id.delete_item);
        }
    }
}
