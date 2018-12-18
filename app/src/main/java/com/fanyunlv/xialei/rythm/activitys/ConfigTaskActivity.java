package com.fanyunlv.xialei.rythm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

/**
 * Created by xialei on 2018/12/8.
 */
public abstract class ConfigTaskActivity extends AppCompatActivity {
    protected static final String TAG = ConfigTaskActivity.class.getSimpleName();
    protected RecyclerView recyclerView;
    protected String[] task_list;
    protected DBhelper dBhelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity_layout);
        dBhelper = DBhelper.getInstance(this);
        recyclerView = findViewById(R.id.task_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        task_list = getResources().getStringArray(R.array.task_list);
        ConfigrecyclerView();
    }

    public abstract void ConfigrecyclerView();

    public abstract void onsaveClick();

    public void setActionTitle(int resid) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(resid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.rythmmenu_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                onsaveClick();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getstateInt(TaskItems items,int position) {
        int result = 0;
        if (items == null) {
            return result;
        }
//        switch (position) {
//            case 1:
//                result = items.getAudio();               //这样写 又会case 穿透
//            case 2:
//                result = items.getWifi();
//            case 3:
//                result = items.getVolume();
//            case 4:
//                result = items.getNfc();
//        }

        switch (position) {
            case 1:
                result = items.getAudio();
                break;
            case 2:
                result = items.getWifi();
                break;
            case 3:
                result = items.getVolume();
                break;
            case 4:
                result = items.getNfc();
                break;
        }
        return result;
    }
}
