package com.skyworth.focus;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.skyworth.utils.L;


public class FocusRecyclerView extends RecyclerView {
    public FocusRecyclerView(Context context) {
        super(context);
    }

    public FocusRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (FocusConfig.useKnobDirection()) {
            direction = FocusConfig.switchDirection(direction);
        }
        L.d("AutoFocusFinder",
                "NaviRecyclerView, focusSearch, focused=" + focused + ", direction=" + direction);
        return super.focusSearch(focused, direction);
    }
}