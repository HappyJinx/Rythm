package com.fanyunlv.xialei.rythm.activitys;

import android.content.Intent;
import android.os.Bundle;
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
import com.fanyunlv.xialei.rythm.adapters.LocationTaskAdapter;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

/**
 * Created by xialei on 2018/12/11.
 */
public class SetLocationTaskActivity extends AppCompatActivity implements DBhelper.OnTaskDBchangeListener{
    private final String TAG = "SettimeTaskActivity";

    private RecyclerView recyclerView;
    private LocationTaskAdapter locationTaskAdapter;
    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);

        ActionBar actionBar = getSupportActionBar();
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreate: actionbar ="+actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.setting_by_location);

        dBhelper =  DBhelper.getInstance(this);
        dBhelper.addtListener(this);

        recyclerView = findViewById(R.id.recyclerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        locationTaskAdapter = new LocationTaskAdapter(getResources(),dBhelper.getLocationList());
        recyclerView.setAdapter(locationTaskAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskChanged() {
        locationTaskAdapter.replaceData(dBhelper.getLocationList());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onOptionsItemSelected: id ="+item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:79  Method:onActivityResult--> code="+requestCode);
    }
}
