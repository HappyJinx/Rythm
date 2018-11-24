package com.fanyunlv.xialei.rythm.function;

import android.content.Context;
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

import com.fanyunlv.xialei.rythm.R;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/11/24.
 */
public class FunctionFragment extends Fragment {
    private static final String TAG = FunctionFragment.class.getSimpleName();

    private RecyclerView mrecyclerView;
    private ArrayList<XFunction> functions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView ");
        functions = new ArrayList<>();
        functions.add(new XFunction("audio", true));
        functions.add(new XFunction("wifi", true));
        functions.add(new XFunction("location", true));

        View contentview = inflater.inflate(R.layout.function_fragment, container, false);
        mrecyclerView = contentview.findViewById(R.id.function_list);
        FunctionRecyclerAdapter madapter = new FunctionRecyclerAdapter(functions,getContext());
        mrecyclerView.setAdapter(madapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mrecyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(new ColorDrawable(Color.RED));
        mrecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        return contentview;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy ");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView ");
        super.onDestroyView();
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

}