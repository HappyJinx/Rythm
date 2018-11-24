package com.fanyunlv.xialei.rythm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;


public class SettimeActivity extends AppCompatActivity {

    private final String TAG = "Rythm";

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
        Log.i(TAG, "onCreate: actionbar ="+actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        dBhelper =  DBhelper.getInstance(SettimeActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rythmAdapter = new RythmTimeAdapter(this,dBhelper.getTimeList());
        recyclerView.setAdapter(rythmAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.rythmmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: id ="+item.getItemId());
        switch (item.getItemId()) {
            case R.id.add_new:
                showTimepiackdialog();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTimepiackdialog() {
        TimePickerDialog dialog =
            new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    dBhelper.insertdb(i,i1);
                    rythmAdapter.replaceList(dBhelper.getTimeList());
                    rythmAdapter.notifyDataSetChanged();
                }
            }, 0, 0, true);
        dialog.setTitle(R.string.settimechoose);
        dialog.show();
    }

}
