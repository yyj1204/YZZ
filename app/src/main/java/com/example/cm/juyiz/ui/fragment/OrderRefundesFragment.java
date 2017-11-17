package com.example.cm.juyiz.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.ui.activity.RefundesDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderRefundesFragment extends Fragment {
    private View view;
    private ListView order_lv;
    private List<Integer> refundeslist;
    private ImageView iv;
    private String content;


    public OrderRefundesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order_menu, container, false);
            initUI();
        }
        return view;
    }

    /*
   初始化控件
    */
    private void initUI() {
        content = getArguments().getString("position");
        order_lv = (ListView) view.findViewById(R.id.order_list);
        iv = (ImageView) view.findViewById(R.id.order_none);


        refundeslist = new ArrayList<>();

        if (content.equals("申请中")) {
            for (int i = 0; i < 10; i++) {
                refundeslist.add(0);
            }
        } else if (content.equals("已完成")) {
            for (int i = 0; i < 10; i++) {
                refundeslist.add(1);
            }
        } else if (content.equals("驳回")) {
            for (int i = 0; i < 10; i++) {
                refundeslist.add(2);
            }
        }
        if (refundeslist.size() == 0 || refundeslist == null) {
            iv.setVisibility(View.VISIBLE);
            order_lv.setVisibility(View.GONE);
        } else {
            iv.setVisibility(View.GONE);
            order_lv.setVisibility(View.VISIBLE);
            OredrListViewAdapter adapter = new OredrListViewAdapter();
            order_lv.setAdapter(adapter);
            order_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), RefundesDetailActivity.class);
                    intent.putExtra("content", refundeslist.get(position));
                    startActivity(intent);
                }
            });
        }
    }

    class OredrListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return refundeslist.size();
        }

        @Override
        public Object getItem(int position) {
            return refundeslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder;

            if (convertView == null) {
                holder = new viewHolder();


                convertView = View.inflate(getActivity(), R.layout.order_refundes_list_item, null);
                holder.order_type = (TextView) convertView.findViewById(R.id.order_type);

                holder.order_name = (TextView) convertView.findViewById(R.id.order_name);
                holder.order_price = (TextView) convertView.findViewById(R.id.order_price);
                holder.order_num = (TextView) convertView.findViewById(R.id.order_num);


                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }

            holder.order_type.setText("待付款");

            return convertView;


        }
    }

    static class viewHolder {
        TextView order_type;

        TextView order_name;
        TextView order_price;
        TextView order_num;

        TextView order_close;


    }

}
