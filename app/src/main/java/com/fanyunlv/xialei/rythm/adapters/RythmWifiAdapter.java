package com.fanyunlv.xialei.rythm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmWifiAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements BaseQuickAdapter.OnItemChildClickListener {

    private final String TAG = "Rythm";
    private List<String> wifiitemlist ;

    public RythmWifiAdapter(List<String> itemlist){
        super(R.layout.wifi_item_layout, itemlist);
        wifiitemlist = itemlist;
        setOnItemChildClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.wifi_setted, item);
        helper.addOnClickListener(R.id.delete_item);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DBhelper.getInstance(mContext).deletewifi(getItem(position));
        remove(position);
    }

    //    @Override
//    public RythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_item_layout,null);
//        RythmViewHolder holder = new RythmViewHolder(item);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RythmViewHolder holder, int position) {
//        holder.item_wifi.setText(wifiitemlist.get(position));
//        holder.item_delete.setOnClickListener(this);
//        holder.item_delete.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return wifiitemlist.size();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.delete_item:
//                if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onClick: v.getTag()="+(v.getTag()));
//                String siten = wifiitemlist.get((int) v.getTag());
//                DBhelper.getInstance(mcontext).deletewifi(siten);
//                wifiitemlist.remove((int)v.getTag());
//                notifyDataSetChanged();
//                break;
//        }
//    }
//
//    public class RythmViewHolder extends RecyclerView.ViewHolder {
//        public TextView item_wifi;
//        public Button item_delete;
//        public RythmViewHolder(View itemView) {
//            super(itemView);
//            item_wifi = itemView.findViewById(R.id.wifi_setted);
//            item_delete = itemView.findViewById(R.id.delete_item);
//        }
//    }

}
