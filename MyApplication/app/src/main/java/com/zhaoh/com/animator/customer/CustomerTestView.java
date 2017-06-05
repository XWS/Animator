package com.zhaoh.com.animator.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoh.com.animator.R;

import java.util.ArrayList;

/**
 * Created by vic on 2017/5/16.
 */

public class CustomerTestView extends AppCompatActivity {

    private ArrayList<View> viewList;
    private ArrayList<String> headTexts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_test_view);

        viewList = new ArrayList<>();
        headTexts = new ArrayList<>();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                TextView view = new TextView(this);
                view.setLayoutParams(layoutParams);
                view.setText("tab1");
                view.setTextSize(20);
                viewList.add(view);
            } else if (i == 1) {
                TextView view = new TextView(this);
                view.setLayoutParams(layoutParams);
                view.setText("tab2");
                view.setTextSize(20);
                viewList.add(view);
            } else {
                TextView view = new TextView(this);
                view.setLayoutParams(layoutParams);
                view.setText("tab3");
                view.setTextSize(20);
                viewList.add(view);
            }
            headTexts.add(i + "");
        }

        ListSelectorView listSelectorView = (ListSelectorView) findViewById(R.id.myView);
        listSelectorView.setListSelectorView(viewList, headTexts);
    }
}
