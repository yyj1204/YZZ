package com.example.cm.juyiz.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.example.cm.juyiz.R;
import com.wang.avi.AVLoadingIndicatorView;


public class LoadingDialog extends Dialog {


    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }


    public static class Builder {
        private String message;
        private View contentView;
        private String positiveButtonText;
        private String negativeButtonText;
        private String singleButtonText;
        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;
        private View.OnClickListener singleButtonClickListener;

        private AVLoadingIndicatorView avi;

        private View layout;
        private LoadingDialog dialog;

        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new LoadingDialog(context, R.style.Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.loading_diolag, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 创建单按钮对话框
         *
         * @return
         */
        public LoadingDialog createSingleButtonDialog() {
            avi = (AVLoadingIndicatorView) layout.findViewById(R.id.avi);
            avi.setIndicator("LineSpinFadeLoaderIndicator");
            avi.show();
            dialog.setContentView(layout);
            dialog.setCancelable(false);              //用户不能点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);  //用户不能通过点击对话框之外的地方取消对话框显示
            dialog.getWindow().setDimAmount(0);       //去除区域外的灰色
            return dialog;
        }

        public void dismiss() {
            avi.hide();
        }
    }
}
