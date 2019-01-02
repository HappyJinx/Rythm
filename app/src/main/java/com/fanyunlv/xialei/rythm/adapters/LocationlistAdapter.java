package com.fanyunlv.xialei.rythm.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.utils.TaskUtil;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/11/27.
 */
public class LocationlistAdapter extends BaseQuickAdapter<MyLocation, BaseViewHolder> implements BaseQuickAdapter.OnItemChildLongClickListener{
    private static final String TAG = "LocationlistAdapter";
    private ArrayList<MyLocation> datas;
    public LocationlistAdapter(ArrayList<MyLocation> v) {
        super(R.layout.location_item, v);
        datas = v;
        setOnItemChildLongClickListener(this);
    }

    public void setDatas(ArrayList<MyLocation> v) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "setDatas "+v.size());
        datas = v;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLocation item) {
        helper.setText(R.id.location_info, item.getName() + ":  " + item.getDescription());
        helper.addOnLongClickListener(R.id.location_detail);
    }

    @Override
    public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
        DBhelper.getInstance(mContext).deletetask(TaskUtil.getInstance(mContext).getcode(getItem(position)));
        DBhelper.getInstance(mContext).deletelocation(getItem(position));
        return true;
    }
}
