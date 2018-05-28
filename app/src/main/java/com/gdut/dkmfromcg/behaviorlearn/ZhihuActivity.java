package com.gdut.dkmfromcg.behaviorlearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by dkmFromCG on 2018/5/20.
 * function:
 */

public class ZhihuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ZhihuActivity.this,"click fab ... ",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.bottom_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ZhihuActivity.this,"click bottom layout ... ",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
