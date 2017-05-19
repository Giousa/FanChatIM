package com.zmm.fanchatim.view.fragment;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午10:41
 */

public interface ChatView {

    void onStartSendMessage();

    void onSendMessageSuccess();

    void onSendMessageFailure();

    void onMessagesLoaded();

    void onMoreMessagesLoaded(int size);

    void onNoMoreData();


}
