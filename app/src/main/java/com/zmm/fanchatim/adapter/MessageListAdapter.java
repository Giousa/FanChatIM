package com.zmm.fanchatim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.zmm.fanchatim.widget.ReceiveMessageItemView;
import com.zmm.fanchatim.widget.SendMessageItemView;

import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午10:59
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "MessageListAdapter";

    private Context mContext;

    private List<EMMessage> mMessages;

    private static final int ITEM_TYPE_SEND_MESSAGE = 0;
    private static final int ITEM_TYPE_RECEIVE_MESSAGE = 1;

    public MessageListAdapter(Context context, List<EMMessage> messages) {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_SEND_MESSAGE) {
            return new SendItemViewHolder(new SendMessageItemView(mContext));
        } else {
            return new ReceiveItemViewHolder(new ReceiveMessageItemView(mContext));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        boolean showTimestamp = false;
        if (position == 0 || shouldShowTimeStamp(position)) {
            showTimestamp = true;
        }
        if (holder instanceof SendItemViewHolder) {
            ((SendItemViewHolder) holder).mSendMessageItemView.bindView(mMessages.get(position), showTimestamp);
        } else {
            ((ReceiveItemViewHolder) holder).mReceiveMessageItemView.bindView(mMessages.get(position), showTimestamp);
        }
    }

    /**
     * 如果两个消息之间的时间太近，就不显示时间戳
     */
    private boolean shouldShowTimeStamp(int position) {
        long currentItemTimestamp = mMessages.get(position).getMsgTime();
        long preItemTimestamp = mMessages.get(position - 1).getMsgTime();
        boolean closeEnough = DateUtils.isCloseEnough(currentItemTimestamp, preItemTimestamp);
        return !closeEnough;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = mMessages.get(position);
        return message.direct() == EMMessage.Direct.SEND ? ITEM_TYPE_SEND_MESSAGE : ITEM_TYPE_RECEIVE_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addNewMessage(EMMessage emMessage) {
        mMessages.add(emMessage);
        notifyDataSetChanged();
    }

    public class ReceiveItemViewHolder extends RecyclerView.ViewHolder {

        public ReceiveMessageItemView mReceiveMessageItemView;

        public ReceiveItemViewHolder(ReceiveMessageItemView itemView) {
            super(itemView);
            mReceiveMessageItemView = itemView;
        }
    }

    public class SendItemViewHolder extends RecyclerView.ViewHolder {

        public SendMessageItemView mSendMessageItemView;

        public SendItemViewHolder(SendMessageItemView itemView) {
            super(itemView);
            mSendMessageItemView = itemView;
        }
    }
}