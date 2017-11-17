package com.example.cm.juyiz.kf_moor.chat.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.cm.juyiz.R;
import com.example.cm.juyiz.kf_moor.chat.ChatActivity;
import com.example.cm.juyiz.kf_moor.chat.adapter.ChatAdapter;
import com.example.cm.juyiz.kf_moor.utils.DensityUtil;
import com.example.cm.juyiz.kf_moor.view.VoiceAnimImageView;
import com.moor.imkf.model.entity.FromToMessage;

/**
 * Created by longwei on 2016/3/9.
 */
public class VoiceViewHolder extends BaseHolder {

    public TextView contentTv;
    public TextView voicePlayAnim;
    public VoiceAnimImageView voiceAnim;
//    public FrameLayout voicePlayFrameLayout;
//    public CCPAnimImageView voiceLoading;
    public ProgressBar voiceSending;
//    public TextView voiceSendigBG;

    /**
     * @param type
     */
    public VoiceViewHolder(int type) {
        super(type);

    }

    public BaseHolder initBaseHolder(View baseView , boolean receive) {
        super.initBaseHolder(baseView);

        chattingTime = ((TextView) baseView.findViewById(R.id.chatting_time_tv));
        voicePlayAnim = ((TextView) baseView.findViewById(R.id.chatting_voice_play_anim_tv));
        uploadState = ((ImageView) baseView.findViewById(R.id.chatting_state_iv));
        contentTv = ((TextView) baseView.findViewById(R.id.chatting_content_itv));
//        voicePlayFrameLayout = ((FrameLayout) baseView.findViewById(R.id.chatto_content_layout));
        voiceAnim = ((VoiceAnimImageView) baseView.findViewById(R.id.chatting_voice_anim));
        voiceAnim.restBackground();

        if (receive) {
            type = 5;
            voiceAnim.setVoiceFrom(true);
//            voiceLoading = ((CCPAnimImageView) baseView.findViewById(R.id.chatting_voice_loading));
//            voiceLoading.setVoiceFrom(true);
//            voiceLoading.restBackground();
            return this;
        }

//        voiceSending = ((ProgressBar) baseView.findViewById(R.id.chatting_voice_sending));
        progressBar = ((ProgressBar) baseView.findViewById(R.id.uploading_pb));
//        voiceSendigBG = ((TextView) baseView.findViewById(R.id.chatting_voice_sending_bg));
        voiceAnim.setVoiceFrom(false);
        type = 6;
        return this;
    }

    /**
     *
     * @param holder
     * @param uploadVisibility
     * @param playVisibility
     * @param receive
     */
    private static void uploadVoiceStatus(VoiceViewHolder holder , int uploadVisibility , int playVisibility , boolean receive) {
        holder.uploadState.setVisibility(View.GONE);
        holder.contentTv.setVisibility(playVisibility);
//        holder.voicePlayFrameLayout.setVisibility(playVisibility);

        if(receive) {
//            holder.voiceLoading.setVisibility(uploadVisibility);
            return;
        }
//        holder.voiceSendigBG.setVisibility(uploadVisibility);
    }

    public static void initVoiceRow(VoiceViewHolder holder , FromToMessage detail , int position , ChatActivity activity , boolean receive) {
        if(holder == null) {
            return ;
        }

        int duration = (detail.recordTime).intValue();
        if(duration < 1) {
            duration = 1;
        }

        holder.voiceAnim.setVisibility(View.GONE);
        ViewHolderTag holderTag = ViewHolderTag.createTag(detail, ViewHolderTag.TagType.TAG_VOICE, position, holder.type, receive);
        holder.voicePlayAnim.setTag(holderTag);
        holder.voicePlayAnim.setOnClickListener(activity.getChatAdapter().getOnClickListener());

        ChatAdapter adapterForce = activity.getChatAdapter();
        if(adapterForce.mVoicePosition == position) {
            uploadVoiceStatus(holder, View.GONE, View.VISIBLE, receive);
            holder.voiceAnim.setVisibility(View.VISIBLE);
            holder.voiceAnim.startVoiceAnimation();
            holder.voiceAnim.setWidth(DensityUtil.fromDPToPix(activity, getTimeWidth(duration)));
            holder.contentTv.setTextColor(Color.parseColor("#7390A0"));
            holder.contentTv.setShadowLayer(2.0F, 1.2F, 1.2F, Color.parseColor("#ffffffff"));
            holder.contentTv.setVisibility(View.VISIBLE);
            holder.contentTv.setText(Math.round(detail.recordTime) + "\"");
            holder.voicePlayAnim.setWidth(DensityUtil.fromDPToPix(activity, getTimeWidth(duration)));
            return;
        } else {
            holder.voiceAnim.stopVoiceAnimation();
            holder.voiceAnim.setVisibility(View.GONE);

        }


        if(detail.sendState.equals("true")) {
            holder.contentTv.setTextColor(Color.parseColor("#7390A0"));
            holder.contentTv.setShadowLayer(2.0F, 1.2F, 1.2F, Color.parseColor("#ffffffff"));
            holder.contentTv.setVisibility(View.VISIBLE);
            holder.contentTv.setText(Math.round(detail.recordTime) + "\"");

            holder.voiceAnim.setWidth(DensityUtil.fromDPToPix(activity, getTimeWidth(duration)));
            holder.voicePlayAnim.setWidth(DensityUtil.fromDPToPix(activity, getTimeWidth(duration)));

            uploadVoiceStatus(holder, View.GONE, View.VISIBLE, receive);
        } else {
            holder.contentTv.setShadowLayer(0.0F, 0.0F, 0.0F, 0);

            if(detail.sendState.equals("false")) {
                uploadVoiceStatus(holder, View.GONE, View.VISIBLE, receive);
                holder.contentTv.setVisibility(View.GONE);
            } else {
                uploadVoiceStatus(holder, View.VISIBLE, View.GONE, receive);
            }
            holder.voiceAnim.setWidth(80);
            holder.voicePlayAnim.setWidth(80);

        }

        if(!receive) {
            holder.voiceAnim.setBackgroundResource(R.drawable.kf_chatto_bg_normal);
            holder.voicePlayAnim.setBackgroundResource(R.drawable.kf_chatto_bg_normal);
        } else {
            holder.voiceAnim.setBackgroundResource(R.drawable.kf_chatfrom_bg_normal);
            holder.voicePlayAnim.setBackgroundResource(R.drawable.kf_chatfrom_bg_normal);
        }

        holder.contentTv.setBackgroundColor(0);



    }

    /**
     * @param time
     * @return
     */
    public static int getTimeWidth(int time) {
        if (time <= 2)
            return 80;
        if (time < 10)
            return (80 + 9 * (time - 2));
        if (time < 60)
            return (80 + 9 * (7 + time / 10));
        return 204;
    }

}
