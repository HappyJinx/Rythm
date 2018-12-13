package com.fanyunlv.xialei.rythm.adapters;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.TimeTaskItem;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmTimeTaskAdapter extends RecyclerView.Adapter<RythmTimeTaskAdapter.RythmViewHolder> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String TAG = "RythmTimeTaskAdapter";
    private static List<TimeTaskItem> timeitemlist = new ArrayList<>();
    private Context mcontext;
    private DBhelper dBhelper;

    private int hour=0;
    private int minute=0;

    public RythmTimeTaskAdapter(DBhelper Bhelper ,Context context, List<TimeTaskItem> itemlist){
        timeitemlist = itemlist;
        mcontext = context;
        dBhelper = Bhelper;
    }

    public void replaceList(List<TimeTaskItem> list) {
        timeitemlist.clear();
        timeitemlist.addAll(list);
    }

    @Override
    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_info,null);
        RythmViewHolder holder = new RythmViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(RythmViewHolder holder, int position) {
        TimeTaskItem timeTaskItem = timeitemlist.get(position);

        holder.item_time.setText(timeTaskItem.getName());
        if (position == 0) {
            holder.checkBox.setVisibility(View.GONE);
            holder.time_setted.setVisibility(View.VISIBLE);
            if (timeTaskItem.getTime() == "") {
                Calendar calendar = Calendar.getInstance();
                int hourt;
                if (calendar.get(Calendar.AM_PM) == 0) {
                    hourt = calendar.get(Calendar.HOUR);
                } else {
                    hourt = calendar.get(Calendar.HOUR)+12;
                }
                holder.time_setted.setText(hourt+":"+calendar.get(Calendar.MINUTE));
            }else {
                holder.time_setted.setText(timeTaskItem.getTime());
            }
        }else {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        holder.checkBox.setChecked( (timeTaskItem.getEnabled()==1)?true:false);
        holder.checkBox.setOnCheckedChangeListener(this);
        holder.checkBox.setTag(position);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return timeitemlist.size();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "LineNum:59  Method:onClick--> v.getId()="+v.getTag());
        if ((int)v.getTag() == 0) {
            showTimepiackdialog();
            return;
        }
        TimeTaskItem item = timeitemlist.get((int) v.getTag());
        item.setEnabled((item.getEnabled()==1)?0:1);
        notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i(TAG, "LineNum:102  Method:onCheckedChanged--> button="+buttonView.getTag());

    }

    public class RythmViewHolder extends RecyclerView.ViewHolder {
        public TextView item_time;
        public TextView time_setted;
        public CheckBox checkBox;
        public RythmViewHolder(View itemView) {
            super(itemView);
            item_time = itemView.findViewById(R.id.task_name);
            time_setted = itemView.findViewById(R.id.time_setted);
            checkBox = itemView.findViewById(R.id.task_state);
        }
    }

    private void showTimepiackdialog() {
        TimePickerDialog dialog =
                new TimePickerDialog(mcontext, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        TimeTaskItem item = timeitemlist.get(0);
                        item.setTime(i+":"+i1);
                        hour = i;
                        minute = i1;
                        Log.i(TAG, "LineNum:119  Method:onTimeSet--> hour="+hour);
                        notifyDataSetChanged();
                    }
                }, 0, 0, true);
        Calendar calendar = Calendar.getInstance();
        int hours;
        if (calendar.get(Calendar.AM_PM) == 0) {
            hours = calendar.get(Calendar.HOUR);
        } else {
            hours = calendar.get(Calendar.HOUR)+12;
        }
        dialog.updateTime(hours,calendar.get(Calendar.MINUTE));
        dialog.setTitle(R.string.settimechoose);
        dialog.show();
    }

    public void addTime() {
        Log.i(TAG, "LineNum:134  Method:addtaskdetail--> hour="+hour);
        if (hour == 0 && minute==0) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.AM_PM) == 0) {
                hour = calendar.get(Calendar.HOUR);
            } else {
                hour = calendar.get(Calendar.HOUR)+12;
            }
            minute = calendar.get(Calendar.MINUTE);
        }
        Log.i(TAG, "LineNum:134  Method:addtaskdetail--> hour="+hour);

        dBhelper.insertdb(hour,minute);
    }

    public void addtaskdetail() {
        Log.i(TAG, "LineNum:149  Method:addtaskdetail--> hour=" + hour);
        if (hour == 0 && minute==0) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.AM_PM) == 0) {
                hour = calendar.get(Calendar.HOUR);
            } else {
                hour = calendar.get(Calendar.HOUR)+12;
            }
            minute = calendar.get(Calendar.MINUTE);
        }
        Log.i(TAG, "LineNum:149  Method:addtaskdetail--> hour=" + hour);

        TaskItems taskDetails = new TaskItems(hour*100+minute);
        taskDetails.setName(""+taskDetails.getTimecode());
        taskDetails.setAudio(timeitemlist.get(1).getEnabled());
        taskDetails.setWifi(timeitemlist.get(2).getEnabled());
        taskDetails.setVolume(timeitemlist.get(3).getEnabled());
        taskDetails.setNfc(timeitemlist.get(4).getEnabled());

        dBhelper.inserttaskDetails(taskDetails);
    }
}
