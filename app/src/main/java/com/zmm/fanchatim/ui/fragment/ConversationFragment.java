package com.zmm.fanchatim.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zmm.fanchatim.R;
import com.zmm.fanchatim.adapter.ConversationAdapter;
import com.zmm.fanchatim.adapter.EMMessageListenerAdapter;
import com.zmm.fanchatim.presenter.ConversationPresenter;
import com.zmm.fanchatim.presenter.impl.ConversationPresenterImpl;
import com.zmm.fanchatim.utils.ThreadUtils;
import com.zmm.fanchatim.utils.ToastUtils;
import com.zmm.fanchatim.view.fragment.ConversationView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Description:聊天界面
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午5:09
 */

public class ConversationFragment extends BaseFragment implements ConversationView {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.add)
    ImageView mAdd;
    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ConversationPresenter mConversationPresenter;
    private ConversationAdapter mConversationAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_messages;
    }

    @Override
    protected void init() {
        super.init();
        mConversationPresenter = new ConversationPresenterImpl(this);
        mTitle.setText(getString(R.string.messages));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mConversationAdapter = new ConversationAdapter(getContext(), mConversationPresenter.getConversations());
        mRecyclerView.setAdapter(mConversationAdapter);

        mConversationPresenter.loadAllConversations();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mConversationAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAllConversationsLoaded() {
//        ToastUtils.SimpleToast(getString(R.string.load_conversations_success));
        mConversationAdapter.notifyDataSetChanged();
    }

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ToastUtils.SimpleToast(getString(R.string.receive_new_message));
                    mConversationPresenter.loadAllConversations();
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
    }
}
