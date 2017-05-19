package com.zmm.fanchatim.presenter;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午11:20
 */

import com.hyphenate.chat.EMConversation;

import java.util.List;

public interface ConversationPresenter {

    void loadAllConversations();

    List<EMConversation> getConversations();
}
