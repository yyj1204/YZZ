package com.example.cm.juyiz.kf_moor.chat.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cm.juyiz.R;


/**
 * Created by longwei on 2016/3/9.
 */
public class TextViewHolder extends BaseHolder {

    private TextView tv_content;

    public TextViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        //通过baseview找到对应组件
        tv_content = (TextView) baseView.findViewById(R.id.chat_content_tv);

        if(isReceive) {
            type = 1;
            return this;
        }
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
        type = 2;
        return this;
    }

    public TextView getDescTextView() {
        if(tv_content == null) {
            tv_content = (TextView) getBaseView().findViewById(R.id.chat_content_tv);
        }
        return tv_content;
    }

}
