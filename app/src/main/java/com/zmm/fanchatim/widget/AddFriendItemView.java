package com.zmm.fanchatim.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmm.fanchatim.R;
import com.zmm.fanchatim.model.AddFriendItem;
import com.zmm.fanchatim.model.event.AddFriendEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午10:01
 */

public class AddFriendItemView extends RelativeLayout {

    @InjectView(R.id.user_name)
    TextView mUserName;
    @InjectView(R.id.timestamp)
    TextView mTimestamp;
    @InjectView(R.id.add)
    Button mAdd;

    public AddFriendItemView(Context context) {
        this(context, null);
    }

    public AddFriendItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddFriendItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_add_friend_item, this);
        ButterKnife.inject(this, this);
    }

    @OnClick(R.id.add)
    public void onClick() {
        String friendName = mUserName.getText().toString().trim();
        String addFriendReason = getContext().getString(R.string.add_friend_reason);
        AddFriendEvent event = new AddFriendEvent(friendName, addFriendReason);
        EventBus.getDefault().post(event);
    }

    public void bindView(AddFriendItem addFriendItem){
        mUserName.setText(addFriendItem.userName);
        mTimestamp.setText(addFriendItem.timestamp);
        if(addFriendItem.isAdded){
            mAdd.setText(getContext().getString(R.string.added));
            mAdd.setEnabled(false);
        }else {
            mAdd.setText(getContext().getString(R.string.add));
            mAdd.setEnabled(true);
        }
    }
}
