package com.fanyunlv.xialei.rythm.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.adapters.RythmWifiAdapter;
import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.utils.RythmDatabase;

public class SetwifiActivity extends AppCompatActivity {

    private final String TAG = "Rythm";

    private RecyclerView recyclerView;
    private RythmDatabase database;
    private SQLiteDatabase db;
    private RythmWifiAdapter rythmAdapter;

    private AlertDialog dialog;

    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwifi);
        database = RythmDatabase.getInstance(this);

        ActionBar actionBar = getSupportActionBar();
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreate: actionbar ="+actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        dBhelper = DBhelper.getInstance(SetwifiActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rythmAdapter = new RythmWifiAdapter(dBhelper.getWifiList());
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
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onOptionsItemSelected: id ="+item.getItemId());
        switch (item.getItemId()) {
            case R.id.add_new:
                showWifipiackdialog();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showWifipiackdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.setwifichoose);
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dBhelper.insertwifi(editText.getText().toString());
                rythmAdapter.replaceData(dBhelper.getWifiList());
//                rythmAdapter.replaceList(dBhelper.getWifiList());
//                rythmAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

}
