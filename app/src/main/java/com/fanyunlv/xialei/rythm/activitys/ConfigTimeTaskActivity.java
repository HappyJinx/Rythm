package com.fanyunlv.xialei.rythm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.adapters.RythmTaskConfigAdapter;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.TaskStateItem;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/12/13.
 */
public class ConfigTimeTaskActivity extends ConfigTaskActivity {
    private ArrayList<TaskStateItem> list;
    private RythmTaskConfigAdapter rythmAdapter;
    
    private int hour;
    private int minute;
    private boolean hasAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:30  Method:onCreate--> ");
            hasAction = true;

            hour = intent.getIntExtra("hour", 0);
            minute = intent.getIntExtra("minute", 0);
        }

        super.onCreate(savedInstanceState);
        setActionTitle(R.string.setting_by_time);

    }

    @Override
    public void ConfigrecyclerView() {
        list = new ArrayList<>();

        TaskItems item = dBhelper.getTask(hour * 100 + minute);
        if (!hasAction) {
            list.add(new TaskStateItem(task_list[0], 0));
        }
        for (int i = 1; i < task_list.length; i++) {
            list.add(new TaskStateItem(task_list[i], getstateInt(item,i)));
        }

        rythmAdapter = new RythmTaskConfigAdapter(dBhelper, this, list);
        recyclerView.setAdapter(rythmAdapter);
    }

    @Override
    public void onsaveClick() {
        if (hasAction) {
            rythmAdapter.updatetaskdetail(hour,minute);
        } else {
            rythmAdapter.addTime();
            rythmAdapter.addtaskdetail();
        }
        finish();
    }
}
