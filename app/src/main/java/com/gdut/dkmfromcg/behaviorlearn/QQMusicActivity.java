package com.gdut.dkmfromcg.behaviorlearn;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkmFromCG on 2018/5/20.
 * function:
 */

public class QQMusicActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_music);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        char tempChar = 'A';
        for (int i = 0; i < 100; i++) {
            StringBuilder tempStr = new StringBuilder();
            tempChar += 1;
            for (int j = 0; j < 5; j++) {
                tempStr.append(tempChar);
            }
            list.add(String.valueOf(tempStr));
        }
        recyclerView.setAdapter(new TempAdapter(list, this));

    }


    class TempAdapter extends RecyclerView.Adapter<TempAdapter.VH> {

        private List<String> stringList;
        private Context tx;

        public TempAdapter(List<String> stringList, Context tx) {
            this.stringList = stringList;
            this.tx = tx;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(tx).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            String str = stringList.get(position);
            holder.textView.setText(str);
        }

        @Override
        public int getItemCount() {
            return stringList == null ? 0 : stringList.size();
        }

        class VH extends RecyclerView.ViewHolder {
            TextView textView;

            VH(View itemView) {
                super(itemView);
                itemView.setBackgroundColor(Color.WHITE);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }

    }
}
