package com.fanyunlv.xialei.rythm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanyunlv.xialei.rythm.MainActivity;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.presenter.RingmodePresenter;
import com.fanyunlv.xialei.rythm.presenter.WifiCheckPresenter;
import com.fanyunlv.xialei.rythm.beans.XFunction;
import com.fanyunlv.xialei.rythm.presenter.LocationPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xialei on 2018/11/24.
 */
public class FunctionRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements BaseQuickAdapter.OnItemChildClickListener {
    private static final String TAG = FunctionRecyclerAdapter.class.getSimpleName();

    private List<String> mList;

    public FunctionRecyclerAdapter(List<String> list) {
        super(R.layout.function_item_layout, list);
        mList = list;
        setOnItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ((MainActivity) mContext).onFragmentSelect(mList.get(position));
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.function_name,item);
        helper.setText(R.id.state_info,getstateInfo(item));

        Log.i(TAG, "convert--> ");
        helper.addOnClickListener(R.id.function_detail);
    }

    public String getstateInfo(String name) {

        switch (mList.indexOf(name)+1) {
            case 1:
                return RingmodePresenter.getInstance(mContext).getCurrentMode();
            case 2:
                return WifiCheckPresenter.getInstance(mContext).getSSIDname();
            case 3:
                return LocationPresenter.getInstance(mContext).getLastLocationDecri();
        }
        return "";
    }

//    @Override
//    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.function_item_layout, parent, false);
//        return new RecyclerHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerHolder holder, int position) {
//        final String name = datas.get(position).getName();
//        holder.functionName.setText(datas.get(position).getName());
//
//        holder.staetinfo.setText(getstateInfo(position+1));
//        if (position!=0) { //audio fragment dont touch
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((MainActivity) mContext).onFragmentSelect(name);
//                }
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return datas.size();
//    }
//
//    class RecyclerHolder extends RecyclerView.ViewHolder {
//
//        public TextView functionName;
//        public TextView staetinfo;
//
//        public RecyclerHolder(View itemView) {
//            super(itemView);
//            functionName = itemView.findViewById(R.id.function_name);
//            staetinfo = itemView.findViewById(R.id.state_info);
//        }
//    }
}
