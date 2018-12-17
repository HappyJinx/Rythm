package com.fanyunlv.xialei.rythm.adapters;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.TaskStateItem;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmTaskConfigAdapter extends RecyclerView.Adapter<RythmTaskConfigAdapter.RythmViewHolder>
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = "RythmTaskConfigAdapter";
    private static List<TaskStateItem> taskList = new ArrayList<>();
    private Context mcontext;
    private DBhelper dBhelper;
    private boolean isAddTime;
    private Resources resources;
    private String[] modearray;
    private int[] arrays = {
            R.array.Audiostring,
            R.array.Wifistring,
            R.array.Volumestring,
            R.array.NFCstring
    };

    private int hour=0;
    private int minute=0;

    public RythmTaskConfigAdapter(DBhelper Bhelper , Context context, List<TaskStateItem> itemlist){
        taskList = itemlist;
        mcontext = context;
        dBhelper = Bhelper;
        resources = mcontext.getResources();
        modearray = resources.getStringArray(R.array.task_list);
    }

    public void replaceList(List<TaskStateItem> list) {
        taskList.clear();
        taskList.addAll(list);
    }

    @Override
    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_info,null);
        RythmViewHolder holder = new RythmViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(RythmViewHolder holder, int position) {
        TaskStateItem taskStateItem = getStateItem(position);
        holder.item_time.setText(taskStateItem.getName());

        if (position == 0) {
            if (taskStateItem.getName().equals("时间")) {
                isAddTime = true;
                holder.state_spinner.setVisibility(View.GONE);
                holder.time_setted.setVisibility(View.VISIBLE);
                if (taskStateItem.getTime() == "") {
                    Calendar calendar = Calendar.getInstance();
                    int hourt;
                    if (calendar.get(Calendar.AM_PM) == 0) {
                        hourt = calendar.get(Calendar.HOUR);
                    } else {
                        hourt = calendar.get(Calendar.HOUR) + 12;
                    }
                    holder.time_setted.setText(hourt + ":" + calendar.get(Calendar.MINUTE));
                } else {
                    holder.time_setted.setText(taskStateItem.getTime());
                }
            }else {
                holder.state_spinner.setVisibility(View.VISIBLE);
            }
        }else {
            holder.state_spinner.setVisibility(View.VISIBLE);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, resources.getStringArray(getArrayID(position)));
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        holder.state_spinner.setAdapter(adapter);

        holder.state_spinner.setDropDownHorizontalOffset(-200);
        holder.state_spinner.setDropDownVerticalOffset(100);
        holder.state_spinner.setDropDownWidth(250);
        holder.state_spinner.setOnItemSelectedListener(this);
        holder.state_spinner.setTag(position);


        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
    }


    public int getArrayID(int position) {
        String name = getStateItem(position).getName();
//        switch (item.getName()) {       //SWITCH  和  if else 对比     switch 这里只能是常量
//            case modearray[1]:
//                return arrays[0];
//            case modearray[2]:
//                return arrays[1];
//            case modearray[3]:
//                return arrays[2];
//            case modearray[4]:
//                return arrays[3];
//        }
        if (name.equals(modearray[1])) {
            return arrays[0];
        }else if (name.equals(modearray[2])) {
            return arrays[1];
        }else if (name.equals(modearray[3])) {
            return arrays[2];
        }else if (name.equals(modearray[4])) {
            return arrays[3];
        }
        return R.array.NFCstring;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:106  Method:onItemSelected--> "+parent.getTag()+"postion="+position+" --id="+id+"--tag="+view.getTag());
        int itemposition = (int)parent.getTag();
        TaskStateItem item = getStateItem(itemposition);
        item.setState(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void onClick(View v) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:59  Method:onClick--> v.getId()="+v.getTag());
        if ((int)v.getTag() == 0 && isAddTime) {
            showTimepiackdialog();
            return;
        }
    }

    public class RythmViewHolder extends RecyclerView.ViewHolder {
        public TextView item_time;
        public TextView time_setted;
        public Spinner state_spinner;
        public RythmViewHolder(View itemView) {
            super(itemView);
            item_time = itemView.findViewById(R.id.task_name);
            time_setted = itemView.findViewById(R.id.time_setted);
            state_spinner = itemView.findViewById(R.id.task_state);
        }
    }

    private void showTimepiackdialog() {
        TimePickerDialog dialog =
                new TimePickerDialog(mcontext, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        TaskStateItem item = getStateItem(0);
                        item.setTime(i+":"+i1);
                        hour = i;
                        minute = i1;
                        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:119  Method:onTimeSet--> hour="+hour);
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
        if (hour == 0 && minute==0) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.AM_PM) == 0) {
                hour = calendar.get(Calendar.HOUR);
            } else {
                hour = calendar.get(Calendar.HOUR)+12;
            }
            minute = calendar.get(Calendar.MINUTE);
        }
        dBhelper.insertdb(hour,minute);
    }
    public void addtaskdetail() {
        if (hour == 0 && minute==0) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.AM_PM) == 0) {
                hour = calendar.get(Calendar.HOUR);
            } else {
                hour = calendar.get(Calendar.HOUR)+12;
            }
            minute = calendar.get(Calendar.MINUTE);
        }

        TaskItems taskDetails = new TaskItems(hour*100+minute);
        taskDetails.setName(taskDetails.getName());
        taskDetails.setAudio(getStateItem(1).getState());
        taskDetails.setWifi(getStateItem(2).getState());
        taskDetails.setVolume(getStateItem(3).getState());
        taskDetails.setNfc(getStateItem(4).getState());

        dBhelper.inserttaskDetails(taskDetails);
    }
    public void addtaskdetail(int hour,int minute) {

        TaskItems taskDetails = new TaskItems(hour*100+minute);
        taskDetails.setName(taskDetails.getName());
        taskDetails.setAudio(getStateItem(0).getState());
        taskDetails.setWifi(getStateItem(1).getState());
        taskDetails.setVolume(getStateItem(2).getState());
        taskDetails.setNfc(getStateItem(3).getState());

        dBhelper.inserttaskDetails(taskDetails);
    }
    public void updatetaskdetail(int hour,int minute) {

        TaskItems taskDetails = new TaskItems(hour*100+minute);
        taskDetails.setName(taskDetails.getName());
        taskDetails.setAudio(getStateItem(0).getState());
        taskDetails.setWifi(getStateItem(1).getState());
        taskDetails.setVolume(getStateItem(2).getState());
        taskDetails.setNfc(getStateItem(3).getState());

        dBhelper.updatetaskDetails(taskDetails);
    }

    public void updatelocationTask(int code ) {
        TaskItems taskDetails = new TaskItems(code);
        
        taskDetails.setName(taskDetails.getName());
        taskDetails.setAudio(getStateItem(0).getState());
        taskDetails.setWifi(getStateItem(1).getState());
        taskDetails.setVolume(getStateItem(2).getState());
        taskDetails.setNfc(getStateItem(3).getState());

        dBhelper.updatetaskDetails(taskDetails);
    }

    public TaskStateItem getStateItem(int position) {
        return taskList.get(position);
    }
}
