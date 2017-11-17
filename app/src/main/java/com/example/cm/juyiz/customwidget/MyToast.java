package com.example.cm.juyiz.customwidget;

/**
自定义Toast
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.R;

public class MyToast {
    private Toast myToast;
    private MyToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_toast);
        textView.setText(text);
        myToast = new Toast(context);
        myToast.setDuration(duration);
        myToast.setView(v);
    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, duration);
    }
    public void show() {
        if (myToast != null) {
            myToast.show();
        }
    }
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (myToast != null) {
            myToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
