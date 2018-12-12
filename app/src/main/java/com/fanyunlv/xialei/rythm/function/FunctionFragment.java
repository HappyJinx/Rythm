package com.fanyunlv.xialei.rythm.function;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.fanyunlv.xialei.rythm.LocationIistener;
import com.fanyunlv.xialei.rythm.MainActivity;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RingmodePresenter;
import com.fanyunlv.xialei.rythm.WifiCheckPresenter;
import com.fanyunlv.xialei.rythm.location.LocationPresenter;
import com.fanyunlv.xialei.rythm.utils.FragmentUtil;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/11/24.
 */
public class FunctionFragment extends Fragment implements LocationIistener,View.OnClickListener{
    private static final String TAG = FunctionFragment.class.getSimpleName();

    private RecyclerView mrecyclerView;
    private ArrayList<XFunction> functions;
    private FunctionRecyclerAdapter madapter;

    private Button setting_by_time;
    private Button setting_by_location;
    private BDLocation bdLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationPresenter.getInstance(getContext()).addLocationListenr(this);
        functions = new ArrayList<>();
        for (int i = 0; i < FragmentUtil.Fragments.length; i++) {
            functions.add(new XFunction(FragmentUtil.Fragments[i]));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView ");

        View contentview = inflater.inflate(R.layout.function_fragment, container, false);
        setting_by_time = contentview.findViewById(R.id.setting_by_time);
        setting_by_location = contentview.findViewById(R.id.setting_by_location);
        setting_by_time.setOnClickListener(this);
        setting_by_location.setOnClickListener(this);
        mrecyclerView = contentview.findViewById(R.id.function_list);
        madapter = new FunctionRecyclerAdapter(functions,getContext());
        mrecyclerView.setAdapter(madapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mrecyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(new ColorDrawable(Color.RED));
        mrecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));//attention this is vertical
        return contentview;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideActionbar(false);
        ((MainActivity) getActivity()).setTitle("当前状态");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i(TAG, "onHiddenChanged true");
        } else {
            Log.i(TAG, "onHiddenChanged false");
        }
    }

    @Override
    public void onLocationReceived(BDLocation location) {
        madapter.notifyDataSetChanged();
        bdLocation = location;
        Log.i(TAG, "LineNum:96  Method:onLocationReceived--> handle task");
        RingmodePresenter.getInstance(getContext()).handletime();
        WifiCheckPresenter.getInstance(getContext()).handlewifi();
    }

    @Override
    public void onLocationNeedNotify(BDLocation bdLocation, float v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_by_time:
                getContext().startActivity(new Intent("xialei.action.start.settime"));
                break;
            case R.id.setting_by_location:
                Intent location = new Intent("xialei.action.start.setlocation");
                getContext().startActivity(location);

                break;
        }
    }
}
