package com.example.cm.juyiz.ui.fragment;
/**
 * 商品详情界面---轮播图
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.MyConstant;


public class DetailsBannerFragment extends Fragment {
    private View view;
    private String imgUrl;

    public DetailsBannerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_main_banner, container, false);
            initUI();
        }
        return view;
    }
    private void initUI() {
        ImageView iv_banner = (ImageView) view.findViewById(R.id.iv_homemain_banner);
         imgUrl = getArguments().getString(MyConstant.FRAGMENT_KEY);
        GlideUtils.loadIntoUseFitWidth(getContext(), imgUrl,R.drawable.loading_details_banner, iv_banner);
    }
}
