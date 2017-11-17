package com.example.cm.juyiz.ui.fragment;
/*
商城分类 --- 轮播图片段
*/

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cm.juyiz.ui.activity.DetailsActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.ClassCarouselBean;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.MyConstant;

public class ClassBannerFragment extends Fragment {
    private View view;
    private ClassCarouselBean carouselBean;

    public ClassBannerFragment() {
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
        carouselBean = (ClassCarouselBean) getArguments().getSerializable(MyConstant.FRAGMENT_KEY);
        GlideUtils.loadImage(getContext(), carouselBean.getImg_url(),R.drawable.loading_details_banner, iv_banner);
        iv_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(MyConstant.KEY_Details,carouselBean.getLink_good_id());
                startActivity(intent);
            }
        });
    }

}
