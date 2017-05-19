package com.zmm.fanchatim.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.zmm.fanchatim.R;
import com.zmm.fanchatim.conf.CommonConfig;
import com.zmm.fanchatim.ui.activity.ChatActivity;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午11:24
 */

public class ConversationItemView extends RelativeLayout {

    @InjectView(R.id.avatar)
    ImageView mAvatar;
    @InjectView(R.id.user_name)
    TextView mUserName;
    @InjectView(R.id.last_message)
    TextView mLastMessage;
    @InjectView(R.id.timestamp)
    TextView mTimestamp;
    @InjectView(R.id.unread_count)
    TextView mUnreadCount;
    @InjectView(R.id.conversation_item_container)
    RelativeLayout mConversationItemContainer;

    public ConversationItemView(Context context) {
        this(context, null);
    }

    public ConversationItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConversationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_conversation_item, this);
        ButterKnife.inject(this, this);
    }

    public void bindView(final EMConversation emConversation) {
        mUserName.setText(emConversation.getUserName());
        updateLastMessage(emConversation);
        updateUnreadCount(emConversation);
        mConversationItemContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(CommonConfig.USER_NAME, emConversation.getUserName());
                getContext().startActivity(intent);
            }
        });
    }

    private void updateLastMessage(EMConversation emConversation) {
        EMMessage emMessage = emConversation.getLastMessage();
        if (emMessage.getBody() instanceof EMTextMessageBody) {
            mLastMessage.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
        } else {
            mLastMessage.setText(getContext().getString(R.string.no_text_message));
        }
        mTimestamp.setText(DateUtils.getTimestampString(new Date(emMessage.getMsgTime())));
    }

    private void updateUnreadCount(EMConversation emConversation) {
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if (unreadMsgCount > 0) {
            mUnreadCount.setVisibility(VISIBLE);
            mUnreadCount.setText(String.valueOf(unreadMsgCount));
        } else {
            mUnreadCount.setVisibility(GONE);
        }
    }
}
