package com.fanyunlv.xialei.rythm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SetwifiActivity extends Activity {

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

        ActionBar actionBar = getActionBar();
        Log.i(TAG, "onCreate: actionbar ="+actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        dBhelper = DBhelper.getInstance(SetwifiActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rythmAdapter = new RythmWifiAdapter(this,dBhelper.getWifiList());
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
                rythmAdapter.replaceList(dBhelper.getWifiList());
                rythmAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

}
