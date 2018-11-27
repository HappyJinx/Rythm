package com.fanyunlv.xialei.rythm.function;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.MainActivity;
import com.fanyunlv.xialei.rythm.R;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/11/24.
 */
public class FunctionRecyclerAdapter extends RecyclerView.Adapter<FunctionRecyclerAdapter.RecyclerHolder> {

    private ArrayList<XFunction> datas;
    private Context mContext;

    public FunctionRecyclerAdapter(ArrayList<XFunction> list, Context context) {
        datas = list;
        mContext = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.function_item_layout, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final String name = datas.get(position).getName();
        holder.functionName.setText(datas.get(position).getName());
        //holder.compatCheckBox.setChecked(datas.get(position).isIsenabled());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).onFragmentSelect(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {

        public TextView functionName;
        //public AppCompatCheckBox compatCheckBox;

        public RecyclerHolder(View itemView) {
            super(itemView);
            functionName = itemView.findViewById(R.id.function_name);
            //compatCheckBox = itemView.findViewById(R.id.function_state);
        }
    }

}
