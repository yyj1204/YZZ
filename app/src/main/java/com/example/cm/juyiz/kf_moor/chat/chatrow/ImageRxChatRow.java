package com.example.cm.juyiz.kf_moor.chat.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.kf_moor.chat.ImageViewLookActivity;
import com.example.cm.juyiz.kf_moor.chat.holder.BaseHolder;
import com.example.cm.juyiz.kf_moor.chat.holder.ImageViewHolder;
import com.moor.imkf.model.entity.FromToMessage;

/**
 * Created by longwei on 2016/3/10.
 */
public class ImageRxChatRow extends BaseChatRow {

    public ImageRxChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, FromToMessage detail, int position) {
        ImageViewHolder holder = (ImageViewHolder) baseHolder;
        final FromToMessage message = detail;
        if(message != null) {
            if(message.withDrawStatus) {
                holder.getWithdrawTextView().setVisibility(View.VISIBLE);
                holder.getContainer().setVisibility(View.GONE);
            }else {
                holder.getWithdrawTextView().setVisibility(View.GONE);
                holder.getContainer().setVisibility(View.VISIBLE);
                Glide.with(context).load(message.message+"?imageView2/0/w/200/h/140")
                        .centerCrop()
                        .crossFade()
                        .placeholder(R.drawable.kf_pic_thumb_bg)
                        .error(R.drawable.kf_image_download_fail_icon)
                        .into(holder.getImageView());

                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImageViewLookActivity.class);
                        intent.putExtra("imagePath", message.message);
                        context.startActivity(intent);
                    }
                });
            }

        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_image_rx, null);
            ImageViewHolder holder = new ImageViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.IMAGE_ROW_RECEIVED.ordinal();
    }
}
