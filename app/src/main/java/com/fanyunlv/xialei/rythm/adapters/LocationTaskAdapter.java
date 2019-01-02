package com.fanyunlv.xialei.rythm.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.TimeItem;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xialei on 2018/12/11.
 */
public class LocationTaskAdapter extends BaseQuickAdapter<MyLocation, BaseViewHolder>
        implements BaseQuickAdapter.OnItemChildClickListener{

    private static final String TAG = LocationTaskAdapter.class.getSimpleName();
    private ArrayList<MyLocation> locationArrayList;
    private String[] task_list;

    public LocationTaskAdapter(Resources res,ArrayList<MyLocation> datas) {
        super(R.layout.task_item_layout,datas);
        locationArrayList = datas;
        task_list = res.getStringArray(R.array.task_state_list);
        setOnItemChildClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLocation item) {
        helper.setText(R.id.item_setted, item.getName());
        helper.setText(R.id.item_setted_info, getLocationTaskinfo(item));
        helper.addOnClickListener(R.id.delete_item);
        helper.addOnClickListener(R.id.sett_item);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.delete_item:
                MyLocation myLocation = locationArrayList.get(position);
                DBhelper.getInstance(mContext).updatetaskDetails(new TaskItems(myLocation.getName(),getcode(myLocation),0,0,0,0));
                break;
            case R.id.sett_item:
                Intent intent = new Intent("xialei.action.start.configlocation");
                intent.putExtra("code",getcode(locationArrayList.get(position)));
                mContext.startActivity(intent);
                break;
        }
    }

    public String getLocationTaskinfo(MyLocation myLocation) {
        int code = getcode(myLocation);
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:57  Method:getLocationTaskinfo--> location ="+myLocation.getLati()+"  "+myLocation.getLongti());
        Cursor cursor = DBhelper.getInstance(mContext).querytask(code);
        if (cursor.getCount() == 0) {
            return "无任务";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (cursor.moveToFirst()) {
            int audio = cursor.getInt(3);
            int wifi = cursor.getInt(4);
            int volume = cursor.getInt(5);
            int nfc = cursor.getInt(6);
            if (audio != 0) {
                stringBuffer.append(task_list[0]);
                stringBuffer.append(" ");
            }
            if (wifi != 0) {
                stringBuffer.append(task_list[1]);
                stringBuffer.append(" ");
            }
            if (volume != 0) {
                stringBuffer.append(task_list[2]);
                stringBuffer.append(" ");
            }
            if (nfc != 0) {
                stringBuffer.append(task_list[3]);
                stringBuffer.append(" ");
            }
            if (audio == 0 && wifi == 0 && volume == 0 && nfc == 0) {
                stringBuffer.append(" 无任务 ");
            }
        }
        cursor.close();
        return stringBuffer.toString();
    }

    public int getcode(MyLocation location) {
        int code = (int) ((location.getLati() + location.getLongti()) * 1000000);
        Log.i(TAG, "LineNum:99  Method:getcode--> code ="+code);
        return code;
    }

}
