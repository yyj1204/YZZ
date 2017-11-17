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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.ui.activity.OrderDetailActivity2;
import com.example.cm.juyiz.R;

import java.util.ArrayList;
import java.util.List;

public class OrderMenuFragment extends Fragment {
    private View view;
    private ListView order_lv;
    private List<Integer> orderlist;
    private ImageView iv;
    private String content;

    private RelativeLayout rl;

    public OrderMenuFragment() {
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


        orderlist = new ArrayList<>();


        //测试数据
        if (content.equals("全部订单")) {
            for (int i = 0; i < 10; i++) {
                int a = i % 3;
                if (a == 0) {
                    orderlist.add(0);
                } else {
                    orderlist.add(i);
                }

            }
        } else if (content.equals("待付款")) {
            for (int i = 0; i < 10; i++) {
                orderlist.add(0);
            }
        } else if (content.equals("待发货")) {
            for (int i = 0; i < 10; i++) {
                orderlist.add(1);
            }
        } else if (content.equals("待收货")) {
            for (int i = 0; i < 10; i++) {
                orderlist.add(2);
            }
        } else if (content.equals("交易完成")) {
            for (int i = 0; i < 10; i++) {
                orderlist.add(3);
            }
        } else if (content.equals("已关闭")) {
            for (int i = 0; i < 10; i++) {
                orderlist.add(4);
            }
        }
        if (orderlist.size() == 0 || orderlist == null) {
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
                    Intent intent = new Intent(getActivity(), OrderDetailActivity2.class);
                    intent.putExtra("content", orderlist.get(position));
                    startActivity(intent);
                }
            });

        }


    }

    class OredrListViewAdapter extends BaseAdapter {
        final int TYPE_1 = 0;
        final int TYPE_2 = 1;

        @Override
        public int getCount() {
            return orderlist.size();
        }

        @Override
        public Object getItem(int position) {
            return orderlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            int p = orderlist.get(position);
            if (p == 0)
                return TYPE_1;
            else
                return TYPE_2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder;
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new viewHolder();
                switch (type) {
                    case TYPE_1:
                        convertView = View.inflate(getActivity(), R.layout.order_obligation_item, null);
                        holder.order_type = (TextView) convertView.findViewById(R.id.order_type);
                        holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                        holder.order_name = (TextView) convertView.findViewById(R.id.order_name);
                        holder.order_price = (TextView) convertView.findViewById(R.id.order_price);
                        holder.order_num = (TextView) convertView.findViewById(R.id.order_num);
                        holder.order_count = (TextView) convertView.findViewById(R.id.order_count);
                        holder.order_close = (TextView) convertView.findViewById(R.id.order_close);
                        holder.order_pay = (TextView) convertView.findViewById(R.id.order_pay);
                        break;
                    case TYPE_2:
                        convertView = View.inflate(getActivity(), R.layout.order_paid_item, null);
                        holder.order_type = (TextView) convertView.findViewById(R.id.order_type);
                        holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                        holder.order_name = (TextView) convertView.findViewById(R.id.order_name);
                        holder.order_price = (TextView) convertView.findViewById(R.id.order_price);
                        holder.order_num = (TextView) convertView.findViewById(R.id.order_num);
                        holder.order_count = (TextView) convertView.findViewById(R.id.order_count);
                        break;
                    default:
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            switch (type) {
                case TYPE_1:
                    holder.order_type.setText("待付款");
                    holder.order_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case TYPE_2:
                    if (orderlist.get(position) == 1) {
                        holder.order_type.setText("待发货");
                    } else if (orderlist.get(position) == 2) {
                        holder.order_type.setText("待收货");
                    } else if (orderlist.get(position) == 3) {
                        holder.order_type.setText("交易完成");
                    } else if (orderlist.get(position) == 4) {
                        holder.order_type.setText("已关闭");
                    }
                    break;
            }
            return convertView;


        }
    }

    static class viewHolder {
        TextView order_type;
        TextView order_time;
        TextView order_name;
        TextView order_price;
        TextView order_num;
        TextView order_count;
        TextView order_close;
        TextView order_pay;


    }

}
