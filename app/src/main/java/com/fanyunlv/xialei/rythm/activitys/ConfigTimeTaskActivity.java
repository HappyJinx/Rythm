package com.fanyunlv.xialei.rythm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.adapters.RythmTimeTaskAdapter;
import com.fanyunlv.xialei.rythm.beans.TaskStateItem;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/12/13.
 */
public class ConfigTimeTaskActivity extends ConfigTaskActivity {
    private ArrayList<TaskStateItem> list;
    private RythmTimeTaskAdapter rythmAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionTitle(R.string.setting_by_time);
    }

    @Override
    public void ConfigrecyclerView() {
        list = new ArrayList<>();
        for (int i = 0; i < task_list.length; i++) {
            list.add(new TaskStateItem(task_list[i], 0));
        }

        rythmAdapter = new RythmTimeTaskAdapter(dBhelper, this, list);
        recyclerView.setAdapter(rythmAdapter);
    }

    @Override
    public void onsaveClick() {
        rythmAdapter.addTime();
        rythmAdapter.addtaskdetail();
        finish();
    }
}
