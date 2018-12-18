package com.fanyunlv.xialei.rythm.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/12/11.
 */
public class LocationTaskAdapter extends RecyclerView.Adapter<LocationTaskAdapter.RythmViewHolder> implements View.OnClickListener {
    private static final String TAG = LocationTaskAdapter.class.getSimpleName();
    private Context mcontext;
    private ArrayList<MyLocation> locationArrayList;
    private DBhelper dBhelper ;
    private String[] task_list;

    public LocationTaskAdapter(Context context) {
        mcontext = context;
        dBhelper = DBhelper.getInstance(mcontext);
        locationArrayList = dBhelper.getLocationList();
        task_list = mcontext.getResources().getStringArray(R.array.task_state_list);
    }

    @Override
    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout,null);
        RythmViewHolder holder = new RythmViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(RythmViewHolder holder, int position) {
        holder.item_setted.setText(locationArrayList.get(position).getName());
        holder.item_setted_info.setText(getLocationTaskinfo(locationArrayList.get(position)));
//        holder.itemView.setOnClickListener(this);
//        holder.itemView.setTag(position);
        holder.item_delete.setOnClickListener(this);
        holder.item_delete.setTag(position);
        holder.item_config.setOnClickListener(this);
        holder.item_config.setTag(position);
    }

    public String getLocationTaskinfo(MyLocation myLocation) {
        int code = getcode(myLocation);
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:57  Method:getLocationTaskinfo--> location ="+myLocation.getLati()+myLocation.getLongti());
        Cursor cursor = dBhelper.querytask(code);
        if (cursor.getCount() == 0) {
            return "无任务";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (cursor.moveToFirst()) {
            int audio = cursor.getInt(3);
            int wifi = cursor.getInt(4);
            int volume = cursor.getInt(5);
            int nfc = cursor.getInt(6);
            if (audio != 0) {
                stringBuffer.append(task_list[0]);
                stringBuffer.append(" ");
            }
            if (wifi != 0) {
                stringBuffer.append(task_list[1]);
                stringBuffer.append(" ");
            }
            if (volume != 0) {
                stringBuffer.append(task_list[2]);
                stringBuffer.append(" ");
            }
            if (nfc != 0) {
                stringBuffer.append(task_list[3]);
                stringBuffer.append(" ");
            }
            if (audio == 0 && wifi == 0 && volume == 0 && nfc == 0) {
                stringBuffer.append(" 无任务 ");
            }
        }
        cursor.close();
        return stringBuffer.toString();
    }


    public int getcode(MyLocation location) {
        return (int)((location.getLati() + location.getLongti()) * 1000000);
    }

    @Override
    public void onClick(View v) {
        int postion = (int)v.getTag();
        int id = v.getId();
        Log.i("location", "LineNum:52  Method:onClick--> id ="+id+"--postion ="+postion);
        if (id == R.id.delete_item) {
//            dBhelper.deletelocation(locationArrayList.get(postion));
            dBhelper.deletetask(getcode(locationArrayList.get(postion)));
//            locationArrayList.remove(postion);
            notifyDataSetChanged();
        } else if (id == R.id.sett_item) {
            Intent intent = new Intent("xialei.action.start.configlocation");
            intent.putExtra("code",getcode(locationArrayList.get(postion)));
            mcontext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return locationArrayList.size();
    }

    public class RythmViewHolder extends RecyclerView.ViewHolder {

        public TextView item_setted;
        public TextView item_setted_info;
        public ImageButton item_delete;
        public ImageButton item_config;
        public RythmViewHolder(View itemView) {
            super(itemView);
            item_setted = itemView.findViewById(R.id.item_setted);
            item_setted_info = itemView.findViewById(R.id.item_setted_info);
            item_delete = itemView.findViewById(R.id.delete_item);
            item_config = itemView.findViewById(R.id.sett_item);
        }
    }
}
