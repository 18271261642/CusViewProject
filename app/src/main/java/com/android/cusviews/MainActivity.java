package com.android.cusviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CusHeartView cusHeartView;
    private List<Integer> heartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        setData();


    }

    private void setData() {
        heartList = new ArrayList<>();
        heartList.add(70);
        heartList.add(80);
        heartList.add(67);
        heartList.add(78);
        heartList.add(90);
        heartList.add(100);
        heartList.add(50);
        heartList.add(66);
        heartList.add(77);
        heartList.add(88);

        cusHeartView.setShowX(true);
        cusHeartView.setShowY(true);
        cusHeartView.setDataList(heartList);
    }

    private void initViews() {
        cusHeartView = findViewById(R.id.cusHeartView);
    }
}
