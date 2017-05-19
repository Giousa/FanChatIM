package com.zmm.fanchatim.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午10:42
 */

public interface ChatPresenter {
    void sendMessage(String userName, String message);

    List<EMMessage> getMessages();

    void loadMessages(String userName);

    void loadMoreMessages(String userName);

    void makeMessageRead(String userName);
}
