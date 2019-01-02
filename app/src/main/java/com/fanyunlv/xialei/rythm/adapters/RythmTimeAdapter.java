package com.fanyunlv.xialei.rythm.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.TimeItem;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmTimeAdapter extends BaseQuickAdapter<TimeItem, BaseViewHolder>
    implements BaseQuickAdapter.OnItemChildClickListener,BaseQuickAdapter.OnItemChildLongClickListener
{

    private final String TAG = "RythmTimeAdapter";
    private List<TimeItem> timeitemlist;
    private String[] task_list;

    public RythmTimeAdapter(Resources res,List<TimeItem> timeitemlist){
        super(R.layout.task_item_layout,timeitemlist);
        Log.i(TAG, "RythmTimeAdapter--> timeitemlist="+timeitemlist.size());
        this.timeitemlist = timeitemlist;
        task_list = res.getStringArray(R.array.task_state_list);
        setOnItemChildClickListener(this);
        setOnItemChildLongClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeItem item) {

        helper.setText(R.id.item_setted, item.toString());
        helper.setText(R.id.item_setted_info, getTaskinfo(item.getHour() * 100 + item.getMinute()));
        helper.addOnClickListener(R.id.delete_item);
        helper.addOnClickListener(R.id.sett_item);
        helper.addOnLongClickListener(R.id.delete_item);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.delete_item:
                TimeItem timeItem = timeitemlist.get(position);
                DBhelper.getInstance(mContext).updatetaskDetails(new TaskItems("",timeItem.getHour()*100+timeItem.getMinute(),0,0,0,0));
                break;
            case R.id.sett_item:
                TimeItem timeItems = timeitemlist.get(position);
                Intent intent = new Intent("xialei.action.start.configtime");
                intent.putExtra("hour",timeItems.getHour());
                intent.putExtra("minute",timeItems.getMinute());
                mContext.startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

        TimeItem timeItem = timeitemlist.get(position);
        DBhelper.getInstance(mContext).deleteitem(timeItem.getHour(),timeItem.getMinute());
        DBhelper.getInstance(mContext).deletetask(timeItem.getHour()*100+timeItem.getMinute());

        return true;
    }

//    @Override
//    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout,null);
//        RythmViewHolder holder = new RythmViewHolder(item);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RythmViewHolder holder, int position) {
//        holder.item_setted.setText(timeitemlist.get(position).toString());
//        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:50  Method:onBindViewHolder--> ");
//        holder.item_setted_info.setText(getTaskinfo(timeitemlist.get(position).getHour()*100+timeitemlist.get(position).getMinute()));
//        holder.item_delete.setOnClickListener(this);
//        holder.item_delete.setOnLongClickListener(this);
//        holder.item_delete.setTag(position);
//        holder.item_config.setOnClickListener(this);
//        holder.item_config.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return timeitemlist.size();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.delete_item:
//                if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onClick: v.getTag()="+(v.getTag()));
//                TimeItem timeItem = timeitemlist.get((int) v.getTag());
////                DBhelper.getInstance(mcontext).deleteitem(timeItem.getHour(),timeItem.getMinute());
//                DBhelper.getInstance(mcontext).updatetaskDetails(new TaskItems("",timeItem.getHour()*100+timeItem.getMinute(),0,0,0,0));
////                timeitemlist.remove((int)v.getTag());
//                notifyDataSetChanged();
//                break;
//            case R.id.sett_item:
//                if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onClick: v.getTag()="+(v.getTag()));
//                TimeItem timeItems = timeitemlist.get((int) v.getTag());
//                Intent intent = new Intent("xialei.action.start.configtime");
//                intent.putExtra("hour",timeItems.getHour());
//                intent.putExtra("minute",timeItems.getMinute());
//                mcontext.startActivity(intent);
//                break;
//        }
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        if (v.getId() == R.id.delete_item) {
//            TimeItem timeItem = timeitemlist.get((int) v.getTag());
//            DBhelper.getInstance(mcontext).deleteitem(timeItem.getHour(),timeItem.getMinute());
//            DBhelper.getInstance(mcontext).deletetask(timeItem.getHour()*100+timeItem.getMinute());
//            timeitemlist.remove((int)v.getTag());
//            notifyDataSetChanged();
//            return true;
//        }
//        return false;
//    }
//
//    public class RythmViewHolder extends RecyclerView.ViewHolder {
//        public TextView item_setted;
//        public TextView item_setted_info;
//        public ImageButton item_delete;
//        public ImageButton item_config;
//        public RythmViewHolder(View itemView) {
//            super(itemView);
//            item_setted = itemView.findViewById(R.id.item_setted);
//            item_setted_info = itemView.findViewById(R.id.item_setted_info);
//            item_delete = itemView.findViewById(R.id.delete_item);
//            item_config = itemView.findViewById(R.id.sett_item);
//        }
//    }

    public String getTaskinfo(int code) {
        Cursor cursor = DBhelper.getInstance(mContext).querytask(code);
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

}
