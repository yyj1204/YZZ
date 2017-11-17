package com.example.cm.juyiz.kf_moor.chat.chatrow;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.kf_moor.chat.ChatActivity;
import com.example.cm.juyiz.kf_moor.chat.holder.BaseHolder;
import com.example.cm.juyiz.kf_moor.chat.holder.InvestigateViewHolder;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.SubmitInvestigateListener;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.model.entity.Investigate;
import com.moor.imkf.model.entity.MsgInves;

import java.util.Collection;

/**
 * Created by longwei on 2016/3/9.
 */
public class InvestigateChatRow extends BaseChatRow {

    private Context context;

    public InvestigateChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, FromToMessage detail, int position) {
        this.context = context;
        InvestigateViewHolder holder = (InvestigateViewHolder) baseHolder;
        final FromToMessage message = detail;
        LinearLayout linearLayout = holder.getChat_investigate_ll();
        linearLayout.removeAllViews();
        if(message != null) {
            final Collection<MsgInves> investigates = message.investigates;
            for (final MsgInves investigate : investigates) {
                LinearLayout investigateItem = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.kf_investigate_item, null);
                TextView tv = (TextView) investigateItem.findViewById(R.id.investigate_item_tv_name);
                tv.setText(investigate.name);
                investigateItem.setTag(investigate);
                investigateItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, "点击了:"+investigate.name, Toast.LENGTH_SHORT).show();
                        Investigate inv = new Investigate();
                        inv.name = investigate.name;
                        inv.value = investigate.value;
                        IMChatManager.getInstance().submitInvestigate(inv, new SubmitInvestigateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(context, "评价成功", Toast.LENGTH_SHORT).show();
                                //删除该条数据
                                IMChatManager.getInstance().deleteInvestigateMsg(message);
                                ((ChatActivity) context).updateMessage();
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    }
                });
                linearLayout.addView(investigateItem);
            }
        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_investigate, null);
            InvestigateViewHolder holder = new InvestigateViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.INVESTIGATE_ROW_TRANSMIT.ordinal();
    }
}
