package com.fanyunlv.xialei.rythm.fragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.adapters.FunctionRecyclerAdapter;
import com.fanyunlv.xialei.rythm.interfaces.LocationIistener;
import com.fanyunlv.xialei.rythm.MainActivity;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.presenter.RingmodePresenter;
import com.fanyunlv.xialei.rythm.presenter.WifiCheckPresenter;
import com.fanyunlv.xialei.rythm.beans.XFunction;
import com.fanyunlv.xialei.rythm.presenter.LocationPresenter;
import com.fanyunlv.xialei.rythm.utils.FragmentUtil;
import com.fanyunlv.xialei.rythm.utils.TaskUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xialei on 2018/11/24.
 */
public class FunctionFragment extends Fragment implements LocationIistener,View.OnClickListener{
    private static final String TAG = FunctionFragment.class.getSimpleName();

    private RecyclerView mrecyclerView;
    private List<String> functions;

    private FunctionRecyclerAdapter madapter;
    private Button setting_by_time;
    private Button setting_by_location;

    private static int receiveCount = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationPresenter.getInstance(getContext()).addLocationListenr(this);
        String[] framgents = getActivity().getResources().getStringArray(R.array.fragments_list);
        functions = Arrays.asList(framgents);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreateView ");

        View contentview = inflater.inflate(R.layout.function_fragment, container, false);

        setting_by_time = contentview.findViewById(R.id.setting_by_time);
        setting_by_location = contentview.findViewById(R.id.setting_by_location);
        setting_by_time.setOnClickListener(this);
        setting_by_location.setOnClickListener(this);

        mrecyclerView = contentview.findViewById(R.id.function_list);
        madapter = new FunctionRecyclerAdapter(functions);
        mrecyclerView.setAdapter(madapter);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mrecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));//attention this is vertical

        return contentview;
    }

    public void updateStateInfo() {
        if (madapter != null) {
            madapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideActionbar(false);
        getActivity().setTitle("当前状态  "+receiveCount);
    }

    @Override
    public void onLocationReceived(BDLocation location) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:99  Method:onLocationReceived--> location="+location.getLatitude()+"--"+location.getLongitude()+"--"+location.getLocationDescribe());

        madapter.notifyItemChanged(2);

        receiveCount += 1;
        getActivity().setTitle("当前状态    次数:"+receiveCount+"--海拔:"+location.getAltitude()+"米");

        TaskUtil.getInstance(getContext()).checkTimeandLocation(location);
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
