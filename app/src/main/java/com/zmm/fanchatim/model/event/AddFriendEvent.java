package com.zmm.fanchatim.model.event;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午9:49
 */

public class AddFriendEvent {
    private String mFriendName;

    private String mReason;

    public AddFriendEvent(String friendName, String reason) {
        this.mFriendName = friendName;
        this.mReason = reason;
    }


    public String getFriendName() {
        return mFriendName;
    }

    public void setFriendName(String friendName) {
        this.mFriendName = friendName;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }
}
