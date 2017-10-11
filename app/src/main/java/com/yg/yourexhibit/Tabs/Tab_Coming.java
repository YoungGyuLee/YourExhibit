package com.yg.yourexhibit.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.squareup.otto.Subscribe;
import com.yg.yourexhibit.Adapter.TabComingAdapter;
import com.yg.yourexhibit.App.ApplicationController;
import com.yg.yourexhibit.Datas.TabComingData;
import com.yg.yourexhibit.R;
import com.yg.yourexhibit.Retrofit.RetrofitGet.ExhibitComingResult;
import com.yg.yourexhibit.Util.EventBus;
import com.yg.yourexhibit.Util.EventCode;
import com.yg.yourexhibit.Util.NetworkController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 2yg on 2017. 10. 9..
 */

public class Tab_Coming extends Fragment{

    @BindView(R.id.tab_coming_list)
    RecyclerView comingList;

    private TabComingAdapter tabComingAdapter;
    private RequestManager requestManager;
    private LinearLayoutManager linearLayoutManager;
    private NetworkController networkController;
    private ArrayList<TabComingData> comingDatas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_home_coming, container, false);
        ButterKnife.bind(this, v);
        EventBus.getInstance().register(this);
        networkController = new NetworkController();
        networkController.getComingData();
        return v;
    }

    public void initFragment(){
        requestManager = Glide.with(this);
        comingList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        comingList.setLayoutManager(linearLayoutManager);
        editData();
        tabComingAdapter = new TabComingAdapter(comingDatas, requestManager);
        comingList.setAdapter(tabComingAdapter);
    }


    @Subscribe
    public void onEventLoad(ArrayList<ExhibitComingResult> exhibitComingResult){

       // initFragment();
        Log.v("통신 됨", "통신");
    }

    @Subscribe
    public void onEventLoad(Integer code){
        if(code == EventCode.EVENT_CODE_COMING_SUCESS){
            initFragment();
        }
    }


    public void editData(){
        ArrayList<ExhibitComingResult> exhibitComingResults;
        exhibitComingResults = ApplicationController.getInstance().getExhibitComingResult();

        comingDatas = new ArrayList<TabComingData>();
        for(int i = 0; i<exhibitComingResults.size(); i++){
            comingDatas.add(new TabComingData(exhibitComingResults.get(i).getExhibition_picture(),
                    //endPeriod(exhibitEndResults.get(i).getExhibition_stard_date(), exhibitEndResults.get(i).getExhibition_end_date()),
                    exhibitComingResults.get(i).getExhibition_stard_date(),
                    exhibitComingResults.get(i).getExhibition_name()
            ));
        }
    }
    @Override
    public void onDestroy() {
        EventBus.getInstance().unregister(this);
        super.onDestroy();
    }

}