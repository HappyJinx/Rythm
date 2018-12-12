package com.fanyunlv.xialei.rythm;

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

import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/12/8.
 */
public class ConfigTimeTaskActivity extends AppCompatActivity {
    private static final String TAG = ConfigTimeTaskActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RythmTimeTaskAdapter rythmAdapter;

    private String[] task_list;
    private ArrayList<TimeTaskItem> list;
    private DBhelper dBhelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_task_layout);
        ActionBar actionBar = getSupportActionBar();
        Log.i(TAG, "onCreate: actionbar ="+actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.setting_by_time);

        dBhelper =  DBhelper.getInstance(this);

        recyclerView = findViewById(R.id.task_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        task_list =getResources().getStringArray(R.array.time_task_list);

        list = new ArrayList<>();
        for (int i = 0; i < task_list.length; i++) {
            list.add(new TimeTaskItem(task_list[i], false));
        }

        rythmAdapter = new RythmTimeTaskAdapter(dBhelper,this,list);
        recyclerView.setAdapter(rythmAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.rythmmenu_task,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: id ="+item.getItemId());
        switch (item.getItemId()) {
            case R.id.save_item:
                Log.i(TAG, "LineNum:69  Method:onOptionsItemSelected--> ");
                rythmAdapter.addTime();
                rythmAdapter.addtaskdetail();
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
