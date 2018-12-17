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
public class ConfigLocationTaskActivity extends ConfigTaskActivity {
    private ArrayList<TaskStateItem> list;
    private RythmTaskConfigAdapter rythmAdapter;
    private int code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionTitle(R.string.setting_by_location);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
    }

    @Override
    public void ConfigrecyclerView() {
        list = new ArrayList<>();
        for (int i = 1; i < task_list.length; i++) {

            list.add(new TaskStateItem(task_list[i], 0));
        }
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:31  Method:ConfigrecyclerView--> size ="+list.size());
        rythmAdapter = new RythmTaskConfigAdapter(dBhelper, this, list);
        recyclerView.setAdapter(rythmAdapter);
    }

    @Override
    public void onsaveClick() {
        rythmAdapter.updatelocationTask(code);
        finish();
    }
}
