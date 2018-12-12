package com.fanyunlv.xialei.rythm;

import android.content.Context;
import android.database.Cursor;
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

    private final String TAG = "RythmTimeAdapter";
    private List<TimeItem> timeitemlist = new ArrayList<>();
    private Context mcontext;
    private DBhelper dBhelper;

    public RythmTimeAdapter(Context context, List<TimeItem> itemlist ,DBhelper dBhelper){
        timeitemlist = itemlist;
        mcontext = context;
        this.dBhelper = dBhelper;
    }

    public void replaceList(List<TimeItem> list) {
        timeitemlist.clear();
        timeitemlist.addAll(list);
    }

    @Override
    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_full_item_layout,null);
        RythmViewHolder holder = new RythmViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(RythmViewHolder holder, int position) {
        holder.item_setted.setText(timeitemlist.get(position).toString());
        Log.i(TAG, "LineNum:50  Method:onBindViewHolder--> ");
        holder.item_setted_info.setText(getTaskinfo(timeitemlist.get(position).getHour()*100+timeitemlist.get(position).getMinute()));
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
                DBhelper.getInstance(mcontext).deletetask(timeItem.getHour()*100+timeItem.getMinute());
                timeitemlist.remove((int)v.getTag());
                notifyDataSetChanged();
                break;
        }
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

    public String getTaskinfo(int code) {
        Cursor cursor = dBhelper.querytask(code);
        if (cursor.getCount() == 0) {
            return "无任务";
        }
        Log.i(TAG, "LineNum:91  Method:getTaskinfo--> cursor="+cursor.getCount());
        StringBuffer stringBuffer = new StringBuffer();
        if (cursor.moveToFirst()) {
            int audio = cursor.getInt(2);
            int wifi = cursor.getInt(3);
            int volume = cursor.getInt(4);
            int nfc = cursor.getInt(5);
            Log.i(TAG, "LineNum:98  Method:getTaskinfo--> audio="+audio+"-wifi="+wifi);
            if (audio == 1) {
                stringBuffer.append(" 声音模式 ");
            }
            if (wifi == 1) {
                stringBuffer.append(" 屏蔽wifi ");
            }
            if (volume == 1) {
                stringBuffer.append(" 调节音量 ");
            }
            if (nfc == 1) {
                stringBuffer.append(" 开关NFC ");
            }
            if (audio == 0 && wifi == 0 && volume == 0 && nfc == 0) {
                stringBuffer.append(" 无任务 ");
            }
        }
        cursor.close();
        return stringBuffer.toString();
    }

}
