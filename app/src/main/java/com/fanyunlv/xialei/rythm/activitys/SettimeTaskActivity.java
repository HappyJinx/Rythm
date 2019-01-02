package com.fanyunlv.xialei.rythm.activitys;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.adapters.RythmTimeAdapter;
import com.fanyunlv.xialei.rythm.utils.DBhelper;


public class SettimeTaskActivity extends AppCompatActivity implements DBhelper.OnTaskDBchangeListener {

    private final String TAG = "SettimeTaskActivity";

    private RecyclerView recyclerView;
    private RythmTimeAdapter rythmAdapter;
    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);
        if (!((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        ActionBar actionBar = getSupportActionBar();
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreate: actionbar ="+actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.setting_by_time);

        dBhelper =  DBhelper.getInstance(this);
        dBhelper.addtListener(this);
        recyclerView = findViewById(R.id.recyclerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rythmAdapter = new RythmTimeAdapter(getResources(),dBhelper.getTimeList());
        recyclerView.setAdapter(rythmAdapter);
    }

    @Override
    public void onTaskChanged() {
        rythmAdapter.replaceData(dBhelper.getTimeList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.rythmmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onOptionsItemSelected: id ="+item.getItemId());
        switch (item.getItemId()) {
            case R.id.add_new:
                Intent intent = new Intent(SettimeTaskActivity.this, ConfigTimeTaskActivity.class);
                startActivityForResult(intent,128);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
