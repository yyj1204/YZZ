package com.example.cm.juyiz.superadapter;
//暂时没有用的，可删
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.json.Product;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class MyAdapter extends BaseAdapter {
    private List<Product> productList;
        private Context context;;
        private LayoutInflater mInflater;
    private int selectNum;

        public MyAdapter(Context context,List<Product> productList,int selectNum) {
            this.context=context;
            this.mInflater = LayoutInflater.from(context);
            this.productList = productList;
            this.selectNum = selectNum;
        }

        @Override
        public int getCount() {
            return productList==null?0:productList.size();
        }

        @Override
        public Product getItem(int i) {
            return productList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View layout;
            final ViewHolder viewHolder;
            if (view==null) {
                layout = mInflater.inflate(R.layout.item_shopping_list, null);
                viewHolder = new ViewHolder(layout);
                layout.setTag(viewHolder);
            }else {
                layout=view;
                viewHolder = (ViewHolder) layout.getTag();
            }
            final Product product = productList.get(i);
            viewHolder.iv_img.setImageResource(product.getImgId());
            viewHolder.tv_name.setText(product.getTitle());
            viewHolder.tv_price.setText("¥"+product.getPrice());
            final boolean selected = product.isSelected();
            if (selected)
            {
                viewHolder.iv_selected.setImageResource(R.drawable.shopping_selected);
            }else {
                viewHolder.iv_selected.setImageResource(R.drawable.shopping_selected_not);
            }
            viewHolder.iv_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!selected)
                    {
                        viewHolder.iv_selected.setImageResource(R.drawable.shopping_selected);
                        selectNum++;
                    }else {
                        viewHolder.iv_selected.setImageResource(R.drawable.shopping_selected_not);
                        selectNum--;
                    }
                    product.setSelected(!selected);
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            return layout;
        }
    class ViewHolder {
        ImageView iv_selected;
        ImageView iv_img;
        ImageView iv_sub;
        ImageView iv_add;
        ImageView iv_delete;
        TextView tv_name;
        TextView tv_price;
        TextView tv_num;
        public ViewHolder(View view)
        {
            iv_selected= (ImageView) view.findViewById(R.id.iv_shopping_list_all);
            iv_img= (ImageView) view.findViewById(R.id.iv_shopping_list);
            iv_sub= (ImageView) view.findViewById(R.id.iv_shopping_list_sub);
            iv_add= (ImageView) view.findViewById(R.id.iv_shopping_list_add);
            iv_delete= (ImageView) view.findViewById(R.id.iv_shopping_list_delete);
            tv_name=(TextView) view.findViewById(R.id.tv_shopping_list_name);
            tv_price =(TextView) view.findViewById(R.id.tv_shopping_list_price);
            tv_num=(TextView) view.findViewById(R.id.tv_shopping_list_num);
        }
    }
}