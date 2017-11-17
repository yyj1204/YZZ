package com.example.cm.juyiz.ui.fragment;
/*
商城首页 --- 首页类型---轮播图片段
*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.HomeMainCarouselBean;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;

public class HomeMainBannerFragment extends Fragment {
    private View view;
    private HomeMainCarouselBean carouselBean;

    public HomeMainBannerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_main_banner, container, false);
            initUI();
        }
        return view;
    }

    private void initUI() {
        ImageView iv_banner = (ImageView) view.findViewById(R.id.iv_homemain_banner);
        carouselBean = (HomeMainCarouselBean) getArguments().getSerializable(MyConstant.FRAGMENT_KEY);
        GlideUtils.loadImage(getActivity(), carouselBean.getImg_url(),R.drawable.loading_banner, iv_banner);

        iv_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyUtils().startActivityType(getActivity(),carouselBean.getLink_type(),carouselBean.getLink_id());
            }
        });
    }
}
