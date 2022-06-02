package com.example.todoplaceholder.utils.view_services;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class AlwaysMarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isFocused(){return true;}
}
