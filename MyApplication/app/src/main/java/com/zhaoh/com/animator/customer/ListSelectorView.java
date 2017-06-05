package com.zhaoh.com.animator.customer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoh.com.animator.R;

import java.util.List;

/**
 * Created by vic on 2017/5/16.
 * 自定义列表选择器
 */

public class ListSelectorView extends LinearLayout {

    private int textSize;
    private int textColor;//选中时tabMean字体颜色
    private int unChecktextColor = 0xff858585;
    private LinearLayout tabMeanView;
    private FrameLayout bottomView;
    private View maskView;//遮罩View
    private FrameLayout popWindows;//弹出的view的父布局
    private int menuBackgroundColor = 0xffffffff;
    private int underlineColor = 0xffcccccc;
    private int maskColor = 0x88888888;

    //当前选择的是第几个tab，值为1表示当前没有选中任何一个tab
    private int current_tab_position = -1;

    public ListSelectorView(Context context) {
        this(context, null);
    }

    public ListSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListSelectorView, defStyleAttr, 0);
        textSize = typedArray.getResourceId(R.styleable.ListSelectorView_textSize, -1);
        if (textSize == -1) {
            textSize = typedArray.getDimensionPixelSize(R.styleable.ListSelectorView_textSize, 14);
        }
        textColor = typedArray.getResourceId(R.styleable.ListSelectorView_textColor, -1);
        if (textColor == -1) {
            textColor = typedArray.getColor(R.styleable.ListSelectorView_textColor, 0xff890c85);
        }
        typedArray.recycle();

        tabMeanView = new LinearLayout(context);
        tabMeanView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(40)));
        tabMeanView.setOrientation(HORIZONTAL);
        tabMeanView.setGravity(Gravity.CENTER);
        tabMeanView.setPadding(0, dpTpPx(6), 0, dpTpPx(6));
        tabMeanView.setBackgroundColor(menuBackgroundColor);
        addView(tabMeanView, 0);

        View underLineView = new View(context);
        underLineView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
        underLineView.setBackgroundColor(underlineColor);
        addView(underLineView, 1);

        bottomView = new FrameLayout(context);
        bottomView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(bottomView, 2);
    }

    public void setListSelectorView(@NonNull List<View> viewLists, @NonNull List<String> headTexts) {

        if (viewLists.size() != headTexts.size()) {
            throw new IllegalArgumentException("the size of viewList should equal with size of headTexts");
        }

        for (int i = 0; i < headTexts.size(); i++) {
            addTab(headTexts, i);
        }

        maskView = new View(getContext());
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSelectView();
            }
        });
        bottomView.addView(maskView, 0);

        popWindows = new FrameLayout(getContext());
        popWindows.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < viewLists.size(); i++) {
            viewLists.get(i).setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(40)));
            popWindows.addView(viewLists.get(i), i);
        }
        bottomView.addView(popWindows, 1);
    }

    private void addTab(List<String> headTexts, final int index) {

        final TextView textView = new TextView(getContext());
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(textSize);
        textView.setTextColor(unChecktextColor);
        textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        textView.setGravity(Gravity.CENTER);
        textView.setText(headTexts.get(index));
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTab(textView);
            }
        });
        tabMeanView.addView(textView);

        bottomView.setVisibility(GONE);

        if (index < headTexts.size() - 1) {
            View underLineView = new View(getContext());
            underLineView.setLayoutParams(new LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
            underLineView.setBackgroundColor(underlineColor);
            tabMeanView.addView(underLineView);
        }
    }

    private void switchTab(TextView indexView) {
        for (int i = 0; i < tabMeanView.getChildCount(); i = i + 2) {

            //对每一个view做判断，判断是否是所点击的那一个
            if (indexView == tabMeanView.getChildAt(i)) {

                //表明点击的是同一个已经展开的列表
                if (current_tab_position == i) {
                    closeSelectView();
                } else {
                    if (current_tab_position == -1) {
                        bottomView.setVisibility(VISIBLE);
                        popWindows.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        popWindows.getChildAt(i / 2).setVisibility(VISIBLE);
                    } else {
                        popWindows.getChildAt(i / 2).setVisibility(VISIBLE);
                    }
                    ((TextView) tabMeanView.getChildAt(i)).setTextColor(textColor);
                    current_tab_position = i;
                }
            } else {
                ((TextView) tabMeanView.getChildAt(i)).setTextColor(unChecktextColor);
                popWindows.getChildAt(i / 2).setVisibility(GONE);
            }
        }
    }

    private void closeSelectView() {
        popWindows.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
        bottomView.setVisibility(GONE);
        ((TextView) tabMeanView.getChildAt(current_tab_position)).setTextColor(unChecktextColor);
        current_tab_position = -1;
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
