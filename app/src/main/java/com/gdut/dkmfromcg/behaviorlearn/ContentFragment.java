package com.gdut.dkmfromcg.behaviorlearn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkmFromCG on 2018/5/20.
 * function:
 */

public class ContentFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ContentAdapter mAdapter;
    private int mFragmentIndex;

    public String getName() {

        return "Tab" + (getArguments().getInt("fragmentIndex") + 1);
    }

    public static ContentFragment newInstance(int fragmentIndex) {

        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("fragmentIndex", fragmentIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        mFragmentIndex = bundle.getInt("fragmentIndex");

        View view = inflater.inflate(R.layout.fragment_content, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        List<String> list = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            list.add("Fragment " + mFragmentIndex + ", 第" + i + "条数据");
        }

        mAdapter = new ContentAdapter(getContext(), list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> mList;
        private Context mContext;

        public ContentAdapter(Context context, List<String> list) {

            mContext = context;
            mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ContentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ContentViewHolder viewHolder = (ContentViewHolder) holder;
            viewHolder.mTextView.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {

            return mList.size();
        }

        public String getItem(int position) {

            return mList.get(position);
        }

        class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            public ContentViewHolder(View itemView) {

                super(itemView);
                mTextView = (TextView) itemView;
            }
        }
    }
}
