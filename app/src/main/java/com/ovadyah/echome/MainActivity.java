package com.ovadyah.echome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        this.initView();
    }

    private void initView() {
        findViewById(R.id.btn_ec_home1).setOnClickListener(this);
        findViewById(R.id.btn_ec_home2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId){
            case R.id.btn_ec_home1:
                ECHome1Activity.launch(this);
                break;
            case R.id.btn_ec_home2:
                ECHome2Activity.launch(this);
                break;
        }
    }
}
