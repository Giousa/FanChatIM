package com.zmm.fanchatim.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zmm.fanchatim.presenter.ChatPresenter;
import com.zmm.fanchatim.utils.ThreadUtils;
import com.zmm.fanchatim.view.fragment.ChatView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/16
 * Time:上午10:42
 */

public class ChatPresenterImpl implements ChatPresenter{

    public static final int DEFAULT_PAGE_SIZE = 20;

    private ChatView mChatView;
    private List<EMMessage> mEMMessageList;

    private boolean hasMoreData = true;

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
        mEMMessageList = new ArrayList<>();
    }

    @Override
    public void sendMessage(final String userName, final String message) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMMessage emMessage = EMMessage.createTxtSendMessage(message, userName);
                emMessage.setStatus(EMMessage.Status.INPROGRESS);
                emMessage.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onSendMessageSuccess();
                            }
                        });
                    }

                    @Override
                    public void onError(int code, String error) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onSendMessageFailure();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
                mEMMessageList.add(emMessage);
                EMClient.getInstance().chatManager().sendMessage(emMessage);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
            }
        });
    }

    @Override
    public List<EMMessage> getMessages() {
        return mEMMessageList;
    }

    @Override
    public void loadMessages(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                if (conversation != null) {
                    //获取此会话的所有消息
                    List<EMMessage> messages = conversation.getAllMessages();
                    mEMMessageList.addAll(messages);
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onMessagesLoaded();
                    }
                });
            }
        });
    }

    @Override
    public void loadMoreMessages(final String userName) {
        if (hasMoreData) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                    EMMessage firstMessage = mEMMessageList.get(0);
                    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                    //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                    final List<EMMessage> messages = conversation.loadMoreMsgFromDB(firstMessage.getMsgId(), DEFAULT_PAGE_SIZE);
                    hasMoreData = (messages.size() == DEFAULT_PAGE_SIZE);
                    mEMMessageList.addAll(0, messages);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onMoreMessagesLoaded(messages.size());
                        }
                    });
                }
            });
        } else {
            mChatView.onNoMoreData();
        }
    }

    @Override
    public void makeMessageRead(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                conversation.markAllMessagesAsRead();
            }
        });
    }
}
