package com.fanyunlv.xialei.rythm.location;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/11/27.
 */
public class LocationlistAdapter extends BaseAdapter {
    private static final String TAG = "LocationlistAdapter";
    private ArrayList<MyLocation> datas;
    private Context context;

    public LocationlistAdapter(Context context,ArrayList<MyLocation> v) {
        datas = v;
        this.context = context;
    }

    public void setDatas(ArrayList<MyLocation> v) {
        Log.i(TAG, "setDatas "+v.size());
        datas = v;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount ");
        return datas.size();
    }

    @Override
    public MyLocation getItem(int position) {
        Log.i(TAG, "getItem postion="+position);
        if (datas.size() == 0) {
            return null;
        }
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.i(TAG, "getView ");
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
            holder.locationinfo = convertView.findViewById(R.id.location_info);
            holder.locationinfo.setText(datas.get(position).getName()+":  "+datas.get(position).getDescription());
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
            holder.locationinfo.setText(datas.get(position).getName()+":  "+datas.get(position).getDescription());
        }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(TAG, "onLongClick ");
                DBhelper.getInstance(context).deletelocation(getItem(position));
                datas.remove(position);
                notifyDataSetChanged();
                return true;
            }
        });
        return convertView;
    }

    class ViewHolder{
        public TextView locationinfo;
    }
}
