package com.fanyunlv.xialei.rythm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.adapters.RythmTaskConfigAdapter;
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
        int i = hasAction ? 1 : 0;

        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:43  Method:ConfigrecyclerView--> i ="+i);

        for (; i < task_list.length; i++) {
            list.add(new TaskStateItem(task_list[i], 0));
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
